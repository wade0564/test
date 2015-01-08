package cn.edu.ustc.wade.proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface AnInterface {
    public void doSth();
}

interface AnotherInterface {
    public void anotherdo();
}

class AClass implements AnInterface, AnotherInterface {
    public void doSth() {
        System.out.println("inside AClass.doSth");
    }

    public void anotherdo() {
        System.out.println("inside AClass.anotherdo");
    }
}

class SimpleInvocationHandler implements InvocationHandler {
    private Object realObject;

    public SimpleInvocationHandler(Object realObject) {
        this.realObject = realObject;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        System.out.println("Before calling: " + method.getName());
        result = method.invoke(realObject, args);
        System.out.println("After calling: " + method.getName());
        System.out.println(proxy.getClass().getName());
        return result;
    }
}

public class ProxyTest {
    public static void main(String[] args) {
        AnInterface realSubject = new AClass();
        AnInterface anProxy = (AnInterface) Proxy.newProxyInstance(realSubject.getClass().getClassLoader(), new Class[] { AnInterface.class },
                new SimpleInvocationHandler(realSubject));

        anProxy.doSth();
        System.out.println(Proxy.isProxyClass(anProxy.getClass()));
        System.out.println(anProxy instanceof AnInterface);

        AnotherInterface anotherProxy = (AnotherInterface) Proxy.newProxyInstance(realSubject.getClass().getClassLoader(),
                new Class[] { AnotherInterface.class }, new SimpleInvocationHandler(realSubject));

        anotherProxy.anotherdo();
        System.out.println(Proxy.isProxyClass(anotherProxy.getClass()));
        System.out.println(anotherProxy instanceof AnotherInterface);

    }
}