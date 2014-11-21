package com.emc.prometheus.parser.parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.dao.LogDao;
import com.emc.prometheus.parser.main.ParserTask;
import com.emc.prometheus.parser.pojo.CompositeLogInfo;
import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.emc.prometheus.parser.pojo.ParsedLogs;
import com.google.common.io.Files;

/**
 * 
 * @author wade
 *
 */


@Component
public class ParserTest {
	
//	public static void main(String[] args) throws IOException {
//		
//		ApplicationContext context =
//				new ClassPathXmlApplicationContext("applicationContext.xml");
//		
//
//		ParserTask task = context.getBean(ParserTask.class);
//		
//		LogDao dao = context.getBean(LogDao.class);
//		
//		CompositeLogInfo compositeLogInfo = dao.getLogInfos();
//		
//		task.compositeLogInfo= compositeLogInfo;
//		
//		task.process();
//	    
//	}
//	
	
	@Test
	public void parseBIOS() throws IOException{
		
		Parser  parser = new Parser();
		
		LogInfo logInfo = new LogInfo();
		
		File bios = new File("C:/logs/bios_test.txt");
	
		SimpleEntry< File, LOG_FILE_TYPE> entry = new SimpleEntry<File,LOG_FILE_TYPE>(bios,LOG_FILE_TYPE.BIOS);
		parser.parse(logInfo, entry);
		
		ParsedLogs parsedLogs = logInfo.getParsedLogs();
		
		Map<LOG_TYPE, List<String>> parsedLogMap = parsedLogs.getParsedLogMap();
		List<String> list = parsedLogMap.get(LOG_TYPE.BIOS);
		for (String string : list) {
			System.out.println(string);
		}
	}
	
	
	@Test
	public void parseVTL() throws IOException{
		
		Parser  parser = new Parser();
		
		LogInfo logInfo = new LogInfo();
		
		File bios = new File("C:/logs/vtl_test.info");
	
		SimpleEntry< File, LOG_FILE_TYPE> entry = new SimpleEntry<File,LOG_FILE_TYPE>(bios,LOG_FILE_TYPE.VTL_INFO);
		parser.parse(logInfo, entry);
		
		ParsedLogs parsedLogs = logInfo.getParsedLogs();
		
		Map<LOG_TYPE, List<String>> parsedLogMap = parsedLogs.getParsedLogMap();
		List<String> list = parsedLogMap.get(LOG_TYPE.VTL_INFO);
		for (String string : list) {
			System.out.println(string);
		}
	}
	
	@Test
	public void parseAsup() throws IOException{
		
		Parser  parser = new Parser();
		
		LogInfo logInfo = new LogInfo();
		
		File asup = new File("C:/logs/autosupport_vtl.test");
	
		SimpleEntry< File, LOG_FILE_TYPE> entry = new SimpleEntry<File,LOG_FILE_TYPE>(asup,LOG_FILE_TYPE.ASUP);
		parser.parse(logInfo, entry);
		
		ParsedLogs parsedLogs = logInfo.getParsedLogs();
		
		Map<LOG_TYPE, List<String>> parsedLogMap = parsedLogs.getParsedLogMap();
		
		File output =  new File("C:/logs/output.log");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		
		for (Entry<LOG_TYPE, List<String>> parsedLogEntry : parsedLogMap.entrySet()) {
			
			System.out.println("================"+parsedLogEntry.getKey());
			bw.write("================"+parsedLogEntry.getKey()+"\n");
			for (String log : parsedLogEntry.getValue()) {
				System.out.println(log);
				bw.write(log+"\n");
			}
			
		}
		
		bw.close();
	}

}
