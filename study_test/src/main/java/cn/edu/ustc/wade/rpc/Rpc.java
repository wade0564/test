package cn.edu.ustc.wade.rpc;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public enum Rpc {
        //使用enum实现单例
        RPC;
        
        //暴露服务
        private java.util.concurrent.ExecutorService executorService = Executors.newCachedThreadPool();
        public void export(final Object service, final int port) {
                try {
                        final Socket request = new ServerSocket(port).accept();
                        executorService.submit(new Runnable() {
                                @Override
                                public void run() {
                                        try (final ObjectInputStream os = new ObjectInputStream(request.getInputStream());
                                                        final ObjectOutputStream oos = new ObjectOutputStream(request
                                                                        .getOutputStream());) {
                                                final String methodName = os.readUTF();
                                                // 参数
                                                final Object[] args = (Object[]) os.readObject();
                                                // 方法的参数类型
                                                final Class<?>[] paramTypes = (Class<?>[]) os.readObject();
                                                // 获取到方法对象
                                                final Method method = service.getClass().getMethod(methodName,
                                                                paramTypes);

                                                final Object result = method.invoke(service, args);
                                                oos.writeObject(result);
                                                request.close();  
                                        } catch (Exception e) {
                                                e.printStackTrace();
                                        }
                                }
                        });
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
        
        //引用服务
        @SuppressWarnings("unchecked")
        public <T> T ref(Class<T> interfaceClass, final String host, final int port) {
                        //使用动态代理拦截所有的方法
                return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
                                new InvocationHandler() {
                                        @Override
                                        //对接口方法的调用都会调到该方法上 
                                        public Object invoke(Object proxy, Method method, Object[] args)
                                                        throws Throwable {
                                                //进行socket调用
                                                try (final Socket socket = new Socket(host, port);
                                                                final ObjectOutputStream oos = new ObjectOutputStream(
                                                                                socket.getOutputStream());
                                                                final ObjectInputStream ois = new ObjectInputStream(
                                                                                socket.getInputStream());) {
                                                        //写入调用方法名
                                                        oos.writeUTF(method.getName());
                                                        //写入参数
                                                        oos.writeObject(args);
                                                          //写入参数类型
                                                        oos.writeObject(method.getParameterTypes());
                                                        final Object result = ois.readObject();
                                                        return result;
                                                } catch (Exception e) {
                                                        return e;
                                                }
                                        }
                                });
        }
        //测试
        public static void main(String[] args) {
//                 RPC.export(new HelloRpcImpl(), 9999);
                final HelloRpc ref = RPC.ref(HelloRpc.class, "127.0.0.1", 9999);
                final String hello = ref.hello("liuyi");
                System.out.println(hello); // hello liuyi
        }
}

interface HelloRpc {
        public String hello(String name);
}

class HelloRpcImpl implements HelloRpc {

        @Override
        public String hello(String name) {
                return "hello" + name;
        }
}
