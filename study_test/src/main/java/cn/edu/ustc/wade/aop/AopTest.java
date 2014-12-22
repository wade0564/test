package cn.edu.ustc.wade.aop;

import java.util.Map;

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/** 
 * @author wade 
 * @version Nov 29, 2014 9:29:36 PM 
 */
@Component
public class AopTest {
	
	
	public void test(int n) throws InterruptedException{
		
		System.out.println("==============="+n);
		Thread.sleep(1000);
		
		inTest();
		
	}
	
	
	public void inTest(){
		System.out.println("intest========");
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		
		ApplicationContext context =new ClassPathXmlApplicationContext("app-test-context.xml");

//		context.getBean(AopTest.class).test(1);
		context.getBean(AopTest.class).inTest();;
		
		Map<String, Long> methodTimeMap = TimeProfilerAspect.methodTimeMap;
		
		for (Map.Entry<String, Long> methodTime : methodTimeMap.entrySet()) {
			
			System.out.println(methodTime.getKey()+":"+ methodTime.getValue()+"ms");
			
		}
		
		
	}

}
