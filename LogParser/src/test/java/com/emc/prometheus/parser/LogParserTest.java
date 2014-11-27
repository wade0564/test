package com.emc.prometheus.parser;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.dao.LogDao;
import com.emc.prometheus.parser.main.LogParser;
import com.emc.prometheus.parser.main.ParserTask;
import com.emc.prometheus.parser.pojo.CompositeLogInfo;

/**
 * 
 * @author wade
 *
 */


@Component
public class LogParserTest {
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context =
				new ClassPathXmlApplicationContext("applicationContext.xml");
		

		ParserTask task = context.getBean(ParserTask.class);
		
		LogDao dao = context.getBean(LogDao.class);
		
		CompositeLogInfo compositeLogInfo = dao.getLogInfos();
		
		task.compositeLogInfo= compositeLogInfo;
		
		task.process();
	    
	}
	
	
	
	@Test
	public void checkDirtyShutDownTest() throws IOException {
		ApplicationContext context =
				new ClassPathXmlApplicationContext("applicationContext.xml");

		LogParser logParser = context.getBean(LogParser.class);
		//old : 1-1
		//new01	:1-1+ 
		//new02	:1-2
		//new03	:1-1,2-2
		logParser.checkDirtyShutDown();
		
	}

}
