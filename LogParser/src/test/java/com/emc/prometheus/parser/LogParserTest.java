package com.emc.prometheus.parser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 
 * @author wade
 *
 */


@Component
public class LogParserTest {
	
	public static void main(String[] args) {
		
		ApplicationContext context =
				new ClassPathXmlApplicationContext("applicationContext.xml");
		
		//lock file
		
		LogParserTest parser =	context.getBean(LogParserTest.class);
		
	    
	    
		
	}
	
	
	

}
