package cn.edu.ustc.wade.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestJDKProxy {
	public static void main(String args[]) {
		ForumService target = new ForumServiceImpl();// 要进行代理的目标业务类

		PerformanceHandler handler = new PerformanceHandler(target);// 用代理类把目标业务类进行编织

		// 创建代理实例，它可以看作是要代理的目标业务类的加多了横切代码(方法)的一个子类
		ForumService proxy = (ForumService) Proxy.newProxyInstance(target
				.getClass().getClassLoader(),
				target.getClass().getInterfaces(), handler);

		proxy.removeForum(10);
		proxy.removeTopic(20);
	}
}

class PerformanceHandler implements InvocationHandler {
	private Object target; // 要进行代理的业务类的实例

	public PerformanceHandler(Object target) {
		this.target = target;
	}

	// 覆盖java.lang.reflect.InvocationHandler的方法invoke()进行织入（增强）的操作
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("Object target proxy:" + target);
		System.out.println("模拟代理加强的方法...");
		Object obj = method.invoke(target, args); // 调用目标业务类的方法
		System.out.println("模拟代理加强的方法执行完毕...");
		return obj;
	}
}