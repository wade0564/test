package com.emc.prometheus.parser.parse.impl;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.emc.prometheus.parser.parse.ParsedLogs;
import com.emc.prometheus.parser.parse.Parser;
import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;

public class AsupParserTest {
	
	
	Parser  parser = new Parser();

	@Test
	public void testAsupParse() throws IOException {
		LogInfo logInfo = new LogInfo();
		
		File file = new File("C:\\logs\\test.asup");
//		File file = new File("C:\\logs\\asup.message.1");
		
		logInfo.setLocation("C:\\logs\\asup.message.1");
		logInfo.setType(LOG_FILE_TYPE.ASUP);
		logInfo.setAsupId(1L);
		
		
		Entry<File, LOG_FILE_TYPE> fileEntry = new SimpleEntry(file, LOG_FILE_TYPE.ASUP);
		parser.parse(logInfo, fileEntry);
		
		print (logInfo.getParsedLogs());
		
	}
	
	

	@Test
	public void testKernInfoParse() throws IOException {
		LogInfo logInfo = new LogInfo();
		
		File file = new File("C:\\logs\\kern.info");
		logInfo.setLocation("C:\\logs\\kern.info.1");
		
		logInfo.setType(LOG_FILE_TYPE.KERN_INFO);
		logInfo.setAsupId(1L);
		
		
		Entry<File, LOG_FILE_TYPE> fileEntry = new SimpleEntry(file, LOG_FILE_TYPE.KERN_INFO);
		parser.parse(logInfo, fileEntry);
		print (logInfo.getParsedLogs());
		
	}
	
	@Test
	public void testMessageParse() throws IOException {
		LogInfo logInfo = new LogInfo();
		
		File file = new File("C:\\logs\\messages.engineering.1");
		logInfo.setLocation("C:\\logs\\messages.engineering");
		
		logInfo.setType(LOG_FILE_TYPE.SUB);
		logInfo.setAsupId(1L);
		
		Entry<File, LOG_FILE_TYPE> fileEntry = new SimpleEntry(file, LOG_FILE_TYPE.MESSAGES_ENGINEERING);
		parser.parse(logInfo, fileEntry);
		print (logInfo.getParsedLogs());
		
	}
	
	public void print(ParsedLogs parsedLogs){
	//print message
		
		Map<LOG_TYPE, List<String>> generatedDateMap = parsedLogs.getGeneratedDateMap();
			if(generatedDateMap!=null){
				
			System.out.println("-------------------------Generated Date :");
			for (Entry<LOG_TYPE, List<String>> generateDateEntry : generatedDateMap.entrySet()) {
				
				System.out.println(generateDateEntry.getKey());
				System.out.println(Arrays.toString(generateDateEntry.getValue().toArray()));
				System.out.println("---------------------");
				
			}
		}
		
		
		Map<LOG_TYPE, List<String>> parsedLogMap = parsedLogs.getParsedLogMap();
		System.out.println("-------------------------Log :");
		for (Entry<LOG_TYPE, List<String>> logEntry : parsedLogMap.entrySet()) {
			
			System.out.println(logEntry.getKey()+":"+logEntry.getValue().size());
			System.out.println(Arrays.toString(logEntry.getValue().toArray()));
			System.out.println("---------------------");
			
		}
	}
	

}
