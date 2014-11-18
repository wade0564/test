package com.emc.prometheus.parser.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;

/** 
* @author wade 
* @version Nov 12, 2014 10:10:05 PM 
*/

public class FileUtils {
	
	
	
	final static Logger log = LoggerFactory.getLogger(FileUtils.class);
	final static String GZ = ".gz";
	final static String CUSTOMER_EMAILS_DIR = "emails/";
	final static String CUSTOMER_ALERTS_DIR = "alerts/";
	final static String SOURCE_INTERNAL_EMAILS_DIR = "internal/";
	final static String SOURCE_INTERNAL_ALERTS_DIR = "internal/";
	final static String DEST_INTERNAL_EMAILS_DIR = "internal/emails/";
	final static String DEST_INTERNAL_ALERTS_DIR = "internal/alerts/";
	static String LOG_ROOT_DIRECTORY=null ;
	{
		try {
			LOG_ROOT_DIRECTORY = PropertiesLoaderUtils.loadAllProperties("logParser.properties").getProperty("LOG.ROOT.DIRECTORY");
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			log.error("Can't find the configuration files {}","logParser.properties");
		}
	}
	

	public static Map<File, LOG_FILE_TYPE> getLogFileMap(LogInfo logInfo) {
		
		
		if(logInfo.getType()==LOG_FILE_TYPE.ASUP){
			
		}else {
			
		}
		
		
		return null;
	}
} 
