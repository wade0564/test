package com.emc.prometheus.parser;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.dao.LogDao;
import com.emc.prometheus.parser.main.ParserTask;
import com.emc.prometheus.parser.pojo.CompositeLogInfo;

/**
 * 
 * @author wade
 *
 */


@Component
public class LogParserTest {
	
	public static void main(String[] args) throws IOException {
		
		ApplicationContext context =
				new ClassPathXmlApplicationContext("applicationContext.xml");
		

		ParserTask task = context.getBean(ParserTask.class);
		
		LogDao dao = context.getBean(LogDao.class);
		
		CompositeLogInfo compositeLogInfo = dao.getLogInfos();
		
		task.compositeLogInfo= compositeLogInfo;
		
		task.process();
	    
	}
	
	
	

}
