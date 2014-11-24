package com.emc.prometheus.parser.persist;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.dedupe.TsAndMsg;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.emc.prometheus.parser.pojo.StoreFile;
import com.emc.prometheus.parser.util.DBUtils;

@Component
public class FilePersistenceProcessor {

	private @Value("${LOGFILE.ASUPID.CAPACITY}") Integer asupIdCapacity;
	
	private @Value("${LOGFILE.STORAGE.ROOT}") String rootDirectory;
	
	private @Value("${LOGFILE.FOLDER.SIZE}") Integer folderSize;
	
	private File newlyCreatedStoreFile = null;
	private File newlyCreatedLogDirectory = null;
	private File newlyCreatedLogTypeDirectory = null;

	public void persist(LogInfo logInfo, LOG_TYPE logType, List<TsAndMsg> tsAndMsgs) throws Exception {

		StoreFile fileToBeWritten = null;
		try{
			fileToBeWritten = getFile(logInfo, logType);
			// write log entry to file
			fileToBeWritten = writeLogs(logInfo, tsAndMsgs, fileToBeWritten);
			fileToBeWritten = renameStoreFile(logInfo, logType, fileToBeWritten);
		}catch(Exception e){
			erase(fileToBeWritten);
			throw e;
		}
		updateDB(logType, fileToBeWritten);
	}
	

	private StoreFile getFile(LogInfo logInfo, LOG_TYPE logType) throws Exception {

		StoreFile storeFile = null;
		File logTypeDirectory = new File(rootDirectory , logType.getValue());
		File logDirectory = null;
		
		//check if log type directory exists
		if(logTypeDirectory.exists()){
			storeFile = DBUtils.getStoreFile(logType);
			
			if(storeFile == null){
				logDirectory = createNewDirectory(logInfo, logTypeDirectory.getAbsolutePath(), logType);
				return new StoreFile(createNewStoreFile(logInfo, logDirectory.getAbsolutePath(), logType));
			}
			//get storeFile's directory, see if exceeds LOGFILE.FOLDER.SIZE
			int size = getCurrentFolderSize(new File(storeFile.getLocation().substring(0, storeFile.getLocation().lastIndexOf('\\'))));
			if(size > folderSize){
				logDirectory = createNewDirectory(logInfo, logTypeDirectory.getAbsolutePath(), logType);
				return new StoreFile(createNewStoreFile(logInfo, logDirectory.getAbsolutePath(), logType));
			}else{
				//get files content, see if exceeds asupIdCapacity
				int content = storeFile.getSymptomDataCount();
				if(content >= asupIdCapacity && size < folderSize){
					logDirectory = new File(storeFile.getLocation().substring(0, storeFile.getLocation().lastIndexOf('\\')));
					return new StoreFile(createNewStoreFile(logInfo, logDirectory.getAbsolutePath(), logType));
				}else if(content >= asupIdCapacity && size == folderSize){
					logDirectory = createNewDirectory(logInfo, logTypeDirectory.getAbsolutePath(), logType);
					return new StoreFile(createNewStoreFile(logInfo, logDirectory.getAbsolutePath(), logType));
				}else{
					return storeFile;
				}
			}
		}else{
			logTypeDirectory.mkdir();
			this.newlyCreatedLogTypeDirectory = logTypeDirectory;
			logDirectory = createNewDirectory(logInfo, logTypeDirectory.getAbsolutePath(), logType);
			return new StoreFile(createNewStoreFile(logInfo, logDirectory.getAbsolutePath(), logType));
		}
	}


	private StoreFile writeLogs(LogInfo logInfo, List<TsAndMsg> tsAndMsgs, StoreFile fileToBeWritten) throws Exception {

		// if throws exception ,catch it and erase what be written just now
		// store a record which means last log's last line position
		File file = fileToBeWritten.getStoreFile();
		Long preLastPosition = fileToBeWritten.getLastPos();

		RandomAccessFile raf = new RandomAccessFile(file.getAbsoluteFile(), "rw");
		try{
			raf.seek(preLastPosition);
			for (TsAndMsg tsAndMsg : tsAndMsgs) {
				// the format as follow, asupid|ts(date,not long)|msg, notice the ts zone must be UTC
				String logStr = String.format("%s|%s|%s", logInfo.getAsupId(), getUTCTime(tsAndMsg.getTs()), tsAndMsg.getMsg());
				raf.writeBytes(logStr);
				raf.writeBytes("\r\n");
			}
		}catch(Exception e){
			throw e;
		}finally{
			raf.close();
		}
		fileToBeWritten.setCurrentPos(file.length());
		return fileToBeWritten;
	}
	

	private StoreFile renameStoreFile(LogInfo logInfo, LOG_TYPE logType, StoreFile fileToBeWritten) throws Exception {
		// The store file name format startAsupId_endAsupid.logType is eg.
		// 1000-30000.message

		// NOTICE: The file can't rename when other application reading or using
		// it will throw IOException,catch it then do it again 1000 times maybe
		int flag = 0;
		File newFile = null;
		
		while(flag <= 1000){
			try{
				File file = fileToBeWritten.getStoreFile();
				String preFileLocation = file.getAbsolutePath();
				String firstAsupId = file.getName().substring(0, file.getName().indexOf('-'));
				String newFileLocation = preFileLocation.substring(0, preFileLocation.lastIndexOf('\\'));
				
				newFile = new File(newFileLocation + "\\" + firstAsupId + "-" + logInfo.getAsupId());
				file.renameTo(newFile);
				break;
			}catch(Exception e){
				if(flag >= 1000){
					throw e;
				}else{
					Thread.sleep(1000);
					flag++;
				}
			}
		}
		
		return new StoreFile(newFile.getAbsolutePath(), fileToBeWritten.getSymptomDataCount(), fileToBeWritten.getLastPos(), fileToBeWritten.getCurrentPos());
	}
	
	
	private void erase(StoreFile storeFile) throws Exception {
		File file = null;
		if(storeFile != null){
			file = storeFile.getStoreFile();
		}
		if(newlyCreatedStoreFile == null && file != null){
			try{
				RandomAccessFile raf = new RandomAccessFile(file.getAbsoluteFile(), "rw");
				raf.setLength(storeFile.getLastPos());
				raf.close();
			}catch(IOException e){
				throw new Exception("The log was written unsuccessfully, but could not restore to the preversion successfully.");
			}
		}else if(newlyCreatedStoreFile != null){
			newlyCreatedStoreFile.delete();
		}
		
		if(newlyCreatedLogDirectory != null){
			if(!newlyCreatedLogDirectory.delete()){
				throw new Exception("The log was written unsuccessfully, but the newly created log directory was not deleted.");
			}
		}
		if(newlyCreatedLogTypeDirectory != null){
			if(!newlyCreatedLogTypeDirectory.delete()){
				throw new Exception("The log was written unsuccessfully, but the newly created log type directory was not deleted.");
			}
		}
	}
	
	
	private void updateDB(LOG_TYPE logType, StoreFile fileToBeWritten) throws Exception {

		// Some Items need to be updated in DB
		// current logType asup count
		// the last line position in stored file
		// any else ?
		fileToBeWritten.setLastPos(fileToBeWritten.getCurrentPos());
		fileToBeWritten.setSymptomDataCount(fileToBeWritten.getSymptomDataCount() + 1);
		System.out.println(fileToBeWritten.getCurrentPos());
		DBUtils.update(logType, fileToBeWritten);
	}
	
	private File createNewDirectory(LogInfo logInfo, String directory, LOG_TYPE logType) throws IOException{
		File f = new File(directory, logInfo.getAsupId().toString());
		if(f.mkdir()){
			this.newlyCreatedLogDirectory = f;
			return f;
		}else{
			return null;
		}
	}
	
	private String createNewStoreFile(LogInfo logInfo, String directory, LOG_TYPE logType) throws IOException{
		File f = new File(directory, logInfo.getAsupId() + "-" + logInfo.getAsupId());
		if(f.createNewFile()){
			this.newlyCreatedStoreFile = f;
			return f.getAbsolutePath();
		}else{
			return null;
		}
	}
	
	private int getCurrentFolderSize(File f){
		String files[] = f.list();
		return files.length;
	}
	
	private Date getUTCTime(Long ts){
		Calendar cal = java.util.Calendar.getInstance();
		cal.setTimeInMillis(ts);
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		
		return new Date(cal.getTimeInMillis());
	}
	
}
