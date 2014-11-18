package com.emc.prometheus.parser.persist;

import java.io.File;
import java.util.List;

import com.emc.prometheus.parser.dedupe.TsAndMsg;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;

/** 
 * @author wade 
 * @version Nov 18, 2014 9:39:06 AM 
 */
public class FilePersistenceProcessor {
	
	
	
	public  static void persist(LogInfo logInfo,LOG_TYPE logType , List<TsAndMsg> tsAndMsgs ){
		
		
		
		File toBeWrittenFile = getWritenFile(logType);
		
		
		//write log entry to file 
		//the format as follow, asupid|ts(date,not long)|msg
		
		
	}

	private static File getWritenFile(LOG_TYPE logType) {
		// TODO Auto-generated method stub
		return null;
	}


	

}
