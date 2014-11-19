package com.emc.prometheus.parser.persist;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.dao.DB;
import com.emc.prometheus.parser.dedupe.TsAndMsg;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.emc.prometheus.parser.pojo.StoreFile;

@Component
public class FilePersistenceProcessor {

	private @Value("${LOGFILE.ASUPID.CAPACITY}") Integer asupIdCapacity;
	
	private @Value("${LOGFILE.ROOT}") String rootDirectory;

	public void persist(LogInfo logInfo, LOG_TYPE logType, List<TsAndMsg> tsAndMsgs) throws Exception {

		StoreFile fileToBeWritten = getFile(logInfo, logType);

		// write log entry to file
		writeLogs(logInfo, tsAndMsgs, fileToBeWritten);

		renameStoreFile(logInfo, logType, fileToBeWritten);

		updateDB();
	}
	

	private StoreFile getFile(LogInfo logInfo, LOG_TYPE logType) throws IOException {

		StoreFile storeFile = null;
		// generate the directory date folder base on epoch
		String dateDiretory = String.format("%1$tY/%1$tm/%1$td", getUTCTime(logInfo.getEpoch()));
		
		//check if directory exists
		File directory = new File(rootDirectory + dateDiretory);
		if(directory.exists()){ // if exists, get from db
			storeFile = DB.getStoreFile(logType);
		}else{ //else create new date folder and store log file
			//TODO: test if directory can be made directly
			directory.mkdir();
			storeFile = createNewStoreFile(logInfo, rootDirectory + dateDiretory, logType);
			//update to db <LOG_TYPE, storeFile>
			DB.update(logType, storeFile);
		}
		
		// TODO:
		// if exceed the capacity, create a new store file, and after writing the
		// logs , update the DB
		Integer content = storeFile.getContent();

		if (content >= asupIdCapacity) {
			// create new file
			storeFile = createNewStoreFile(logInfo, rootDirectory + dateDiretory, logType);
			DB.update(logType, storeFile);
		}

		return storeFile;
	}

	private void updateDB() throws Exception {

		// Some Items need to be updated in DB
		// current logType asup count
		// the last line position in stored file
		// any else ?
	}

	private void renameStoreFile(LogInfo logInfo, LOG_TYPE logType,
			StoreFile toBeWrittenFile) throws Exception {
		// The store file name format startAsupId_endAsupid.logType is eg.
		// 1000-30000.message

		// NOTICE: The file can't rename when other application reading or using
		// it will throw IOException,catch it then do it again 1000 times maybe

	}

	private void writeLogs(LogInfo logInfo, List<TsAndMsg> tsAndMsgs,
			StoreFile toBeWrittenFile) throws Exception {

		// TODO
		// if throws exception ,catch it and erase what be written just now
		// store a record which means last log's last line position
		for (TsAndMsg tsAndMsg : tsAndMsgs) {
			// the format as follow, asupid|ts(date,not long)|msg
			// notice the Ts zone must be UTC
			String logStr = String.format("%s|%s|%s", logInfo.getAsupId(),
					new Date(tsAndMsg.getTs()), tsAndMsg.getMsg());
			// use buffered writer to write
			// close the writer
		}

	}
	

	// when throw exception ,erase the written content in stored file
	private void erase() {

	}
	
	
	private Date getUTCTime(Long ts){
		Calendar cal = java.util.Calendar.getInstance();
		cal.setTimeInMillis(ts);
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		
		return new Date(cal.getTimeInMillis());
	}
	
	private StoreFile createNewStoreFile(LogInfo logInfo, String directory, LOG_TYPE logType) throws IOException{
		File f = new File(directory , logInfo.getAsupId() + "-" + logInfo.getAsupId() + "." + logType);
		f.createNewFile();
		return new StoreFile(f);
	}

}
