package com.emc.prometheus.parser.persist;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.dao.DB;
import com.emc.prometheus.parser.dedupe.TsAndMsg;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.emc.prometheus.parser.pojo.StoreFile;

/** 
 * @author wade 
 * @version Nov 18, 2014 9:39:06 AM 
 */
@Component
public class FilePersistenceProcessor {
	
	
	private @Value("${LOGFILE.ASUPID.CAPACITY}") Integer asupIdCapacity;
	
	public  void persist(LogInfo logInfo,LOG_TYPE logType , List<TsAndMsg> tsAndMsgs ) throws Exception {
		
		
		StoreFile toBeWrittenFile = getWritenFile(logInfo,logType);

		//write log entry to file 
		writeLogs(logInfo,tsAndMsgs,toBeWrittenFile);
		
		renameStoreFile(logInfo,logType,toBeWrittenFile);
		
		updateDB();
		
	}




	private void updateDB() throws Exception {
		
		//Some Items need to be updated in DB
		//current logType content
		//the last line position in stored file
		//any else ?
	}




	private void renameStoreFile(LogInfo logInfo, LOG_TYPE logType,
			StoreFile toBeWrittenFile) throws Exception {
		//The store file name format startAsupId_endAsupid.logType is eg. 1000-30000.message
		
		//NOTICE: The file can't rename when other application reading or using 
		//it will throw IOException,catch it then do it again 1000 times maybe   
		
		
	}



	private void writeLogs(LogInfo logInfo, List<TsAndMsg> tsAndMsgs,
			StoreFile toBeWrittenFile) throws Exception {
		
		//TODO
		//if throws exception ,catch it and erase what be written just now
		//store a record which means  last log's last line position 
		for (TsAndMsg tsAndMsg : tsAndMsgs) {
			//the format as follow, asupid|ts(date,not long)|msg
			//notice the Ts zone must be UTC
			String logStr = String.format("%s|%s|%s", logInfo.getAsupId(),new Date(tsAndMsg.getTs()),tsAndMsg.getMsg());
			//use buffered writer  to write
			//close the writer
		}
		
	}

	private StoreFile getWritenFile(LogInfo logInfo,LOG_TYPE logType) {
		
		
		StoreFile storeFile = DB.getStoreFile(logType);
		
		//TODO
		//if exceed the capacity, create a new store file,And after writing the logs , update the DB
		Integer content = storeFile.getContent();

		if(content >= asupIdCapacity){
			//create new file
		}
		
		return storeFile;
	}

	//when throw exception ,erase the written content in stored file
	private void erase(){

	}
	

}
