package com.emc.prometheus.parser.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileAlreadyExistsException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.parse.exception.TimeHandlerException;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.StoreFile;
import com.emc.prometheus.parser.util.DBUtils;
import com.sleepycat.je.DatabaseException;

/**
 * 
 * @author wade
 *
 */
@Component
public class LogParser {
	
	final static Logger log = LoggerFactory.getLogger(LogParser.class);

	private @Value("${LOGPARSER.LOCKBASEDIR}")
	String lock_base_dir;

	private @Value("${LOGPARSER.LOCKFILE}")
	String lock_name;
	
	private @Value("${LOGFILE.STORAGE.ROOT}")
	String storageRootDir;
	
	private @Value("${LOGPARSER.SLEEP}")
	Integer sleepMinutes;
	
	
	private @Autowired 
	ParserTask parserTask;
	
	public static void main(String[] args) {
		
		ApplicationContext context =
				new ClassPathXmlApplicationContext("applicationContext.xml");
		
	    LogParser parser =	context.getBean(LogParser.class);
	    
//	    if (!parser.lock())
//			return;
	    
	    try {
			parser.checkDirtyShutDown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Exception while checking the Dirty files");
			log.error(e.getMessage(),e);
			System.exit(-1);
		}
	    
//		while (true) {
			try {
				parser.start();
				parser.sleep();
//			}catch(DatabaseException e){
//				log.error("Exception while Processing In-Memory DB: {}", e.getMessage());
//				log.error(e.getMessage(),e);
////				break;
//			}catch(TimeHandlerException e){
//				log.error("Exception while parsing time: {}", e.getMessage());
//				log.error(e.getMessage(),e);
//				log.info("skip the asupid :{}",e.getLogInfo().getAsupId());
//				DBUtils.update(DBUtils.LAST_ASUPID, e.getLogInfo().getAsupId());
//				parser.sleep();
//			}catch(DataAccessException e){
//				log.error("Exception while Processing GreenPlum: {}", e.getMessage());
//				log.error(e.getMessage(),e);
//				parser.sleep();
//			}catch(FileNotFoundException e){
//				log.error("Exception while handling file: {}", e.getMessage());
//				log.error(e.getMessage(),e);
//				DBUtils.incrementLastAsupId();
//				parser.sleep();
//			}catch(IOException e){
//				log.error("Exception while handling file: {}", e.getMessage());
//				log.error(e.getMessage(),e);
//				if(e.getMessage().contains("No space left")){
////					break;
//				}else {
//					parser.sleep();
//				}
			}catch (Exception e) {
				log.error("Exception while Processing parsing: {}", e.getMessage());
				log.error(e.getMessage(),e);
//				me.subHandleService.shutdownNow();
//				me.emailRevService.shutdown();
//				// ------------------------------------------------------------------------
//				// Release Lock
//				// ------------------------------------------------------------------------
//				if (!parser.remitLock())
//					log.error("Completely Unable to release Lock");
//				break;
			}
//		}
	}
	
	public void checkDirtyShutDown() throws IOException {
		
		Long lastAsupid = DBUtils.getLastAsupId();
		log.info("Last AsupId {}",lastAsupid);
		if(lastAsupid==null)
			return ;
		
		for (LOG_TYPE type: LOG_TYPE.values()) {
		 	StoreFile storeFile = DBUtils.getStoreFile(type);
		 	if(storeFile==null)
		 		continue;
		 	checkabnormalDir(type,lastAsupid);
		 	checkabnormalFile(type,storeFile,lastAsupid);
		 	
		}
	}

	private void checkabnormalFile(LOG_TYPE type,StoreFile storeFile,Long lastAsupId) throws IOException {
		
		File normalfile = storeFile.getStoreFile();
		
		//storeFIle exists ,but the length is not correct
		//erase the overlap
		if (normalfile.exists() && storeFile.getLastPos() != normalfile.length()) {
			RandomAccessFile raf = null;
			try {
				log.info("erase the overlap of the file :{}",normalfile.getAbsolutePath());
				raf = new RandomAccessFile(normalfile, "rw");
				raf.setLength(storeFile.getLastPos());
			} finally {
				raf.close();
			}
		}
				
		//check the sblings
		File parent = normalfile.getParentFile();
		String[] sblings = parent.list();
		for (String sbling : sblings) {
			String[] asupids = sbling.split("-");
			Long startAsupId = Long.parseLong(asupids[0]);
			Long endAsupId = Long.parseLong(asupids[1]);
			
			if(endAsupId.compareTo(lastAsupId)>0){
				File abnormalFile =new File(parent,sbling);
				// old filename : 1-9
				// new filename : 10-10 ,it's to be deleted
				if(startAsupId.equals(endAsupId)){
					if(abnormalFile.delete()){
						log.info("abnormal directory {} was deleted",abnormalFile.getAbsoluteFile());
					}else {
						log.error("abnormal directory {} can't be deleted",abnormalFile.getAbsoluteFile());
						throw new IOException();
					}	
				}else {
					//new filename: 1-10 ,erase the overlap
					//then rename
					RandomAccessFile raf =null;
					try{
						raf = new RandomAccessFile(abnormalFile, "rw");
						raf.setLength(storeFile.getLastPos());
					}finally{
						raf.close();
					}
					abnormalFile.renameTo(normalfile);
				}
			}
		}
		
	}

	private void checkabnormalDir(LOG_TYPE type,Long lastAsupId) throws IOException {
		File typeRootDir = new File(storageRootDir,type.getValue());
		String[] childrenNames = typeRootDir.list();
		for (String name : childrenNames) {
			Long nameLong =null;
			nameLong = Long.parseLong(name);
			if(nameLong.compareTo(lastAsupId)>0){
				File toBeDeletedFile =new File(typeRootDir,name);
				if(toBeDeletedFile.delete()){
					log.info("abnormal directory {} was deleted",toBeDeletedFile.getAbsoluteFile());
				}else {
					log.error("abnormal directory {} can't be deleted",toBeDeletedFile.getAbsoluteFile());
					throw new IOException();
				}
			}
		}
	}

	

	private boolean lock() {
		if (!ConcurrencyControl.grabLock(lock_base_dir, lock_name)) {
			log.warn("Cannot grab lockfile {}/{}.... Exiting", lock_base_dir,lock_name);
			return false;
		} else {
			return true;
		}
	}
	
	private boolean remitLock() {
		Boolean lock_released = false;
		for (int i = 0; i < 10; i++) {
			if (!ConcurrencyControl.releaseLock(lock_base_dir, lock_name)) {
				log.warn("Cannot release lockfile {}/{}.... Try " + i + " of 10", lock_base_dir, lock_name);
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
				continue;
			} else {
				lock_released = true;
				break;
			}
		}
		return lock_released;
	}
	
	public void start() throws Exception{
		parserTask.run();
	}
	
	public void sleep(){
		log.info("this round end");
		log.info("Sleep {} minutes....." ,sleepMinutes);
		try {
			Thread.sleep(TimeUnit.MINUTES.toMillis(sleepMinutes));
		} catch (InterruptedException e) {
			//no need to handle
		}
	}

}
