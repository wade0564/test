package cn.edu.ustc.projects.asupqueuing.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextTest {
	
	
	
public static void main(String[] args) throws InterruptedException {
	for (int i = 0; i < 10; i++) {
		
		createAC();
		System.out.println(i);
	}
	
	Thread.sleep(9*3600);
	
}

private static void createAC() {
	
	ApplicationContext context =new ClassPathXmlApplicationContext("app-context.xml");
	
	
}
	
	
	

}
