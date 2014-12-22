package cn.edu.ustc.wade.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class TimeProfilerAspect {

	public static Map<String, Long> methodTimeMap = new HashMap<String, Long>();

	@Around("execution(* cn.edu.ustc.wade.aop.AopTest.*(..))")
	public void profileTime(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		Long before = System.currentTimeMillis();
		joinPoint.proceed(); // continue on the intercepted method
		
		System.out.println("Aop : "+methodName);
		
//		Long oldValue = methodTimeMap.get(methodName)==null?0:methodTimeMap.get(methodName);
//		
//		methodTimeMap.put(methodName, oldValue + System.currentTimeMillis() - before);
	}

}