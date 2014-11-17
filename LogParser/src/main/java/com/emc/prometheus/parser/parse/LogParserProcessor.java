package com.emc.prometheus.parser.parse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LogParserProcessor {
	
	@Autowired
	ApplicationContext ac;

//	public void parse(LogInfo logInfo, Entry<File, LOG_TYPE> logFileEntry) throws IOException {
//		
////		Parser parser = null;
//		switch (logInfo.getType()) {
//		case ASUP:
//			parser = ac.getBean(AsupParser.class);
//			break;
//		case SUB:
//			parser = ac.getBean(SubParser.class);
//			break;
//		default:
//			break;
//		}
//
//		parser.parse(logInfo,logFileEntry);
//
//	}

}
