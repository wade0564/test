package com.emc.prometheus.parser.main;

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
public class LogParser {
	
	public static void main(String[] args) {
		
		ApplicationContext context =
				new ClassPathXmlApplicationContext("app-context.xml");
		
		//lock file
		
	    LogParser parser =	context.getBean(LogParser.class);
		
	    parser.start();
		
	}
	
	public void start(){
		
		
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		
//		service.s
		
	}
	
	

}
