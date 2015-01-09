package cn.edu.ustc.wade.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * CGlib采用非常底层的字节码技术，可以为一个类创建子类， 并在子类中采用方法拦截技术拦截父类方法的调用，并顺势进行增强，即是织入横切逻辑
 * 
 * @author tufu
 */
class CglibProxy implements MethodInterceptor {
	private Enhancer enhancer = new Enhancer();

	// 覆盖MethodInterceptor接口的getProxy()方法，设置
	public Object getProxy(Class clazz) {
		enhancer.setSuperclass(clazz); // 设者要创建子类的类
		enhancer.setCallback(this); // 设置回调的对象
		return enhancer.create(); // 通过字节码技术动态创建子类实例,
	}

	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		System.out.println("模拟代理增强方法");

		// 通过代理类实例调用父类的方法，即是目标业务类方法的调用
		Object result = proxy.invokeSuper(obj, args);

		System.out.println("模拟代理增强方法结束");
		return result;
	}
}

public class TestCglibProxy {
	public static void main(String args[]) {
		CglibProxy proxy = new CglibProxy();

		// 动态生成子类的方法创建代理类
		ForumServiceImpl fsi = (ForumServiceImpl) proxy
				.getProxy(ForumServiceImpl.class);

		fsi.removeForum(10);
		fsi.removeTopic(2);
	}
}