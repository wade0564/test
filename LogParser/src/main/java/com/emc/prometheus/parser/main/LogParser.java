package com.emc.prometheus.parser.main;

import java.awt.Checkbox;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.StoreFile;
import com.emc.prometheus.parser.util.DBUtils;

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
				new ClassPathXmlApplicationContext("app-context.xml");
		
	    LogParser parser =	context.getBean(LogParser.class);
	    
	    if (!parser.lock())
			return;
	    
	    try {
			parser.checkDirtyShutDown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		while (true) {
//			try {
				parser.start();
				parser.sleep();
//			}catch(DataAccessException e){
//				log.error(Logging.Preparser,"Exception while Processing SUB: {}", e.getMessage());
//				log.error(e.getMessage(),e);
//				me.sleep();
//			}catch (Exception e) {
//				log.error(Logging.Preparser,"Exception while Processing SUB: {}", e.getMessage());
//				log.error(e.getMessage(),e);
//				me.subHandleService.shutdownNow();
//				me.emailRevService.shutdown();
//				// ------------------------------------------------------------------------
//				// Release Lock
//				// ------------------------------------------------------------------------
//				if (!me.remitLock())
//					log.error(Logging.Preparser,"Completely Unable to release Lock");
//				break;
//			}
		}
	}
	
	private  void checkDirtyShutDown() throws IOException {
		
		Long lastAsupid = (Long) DBUtils.get(DBUtils.LAST_ASUPID, Long.class);
		Map<LOG_TYPE, StoreFile> storeFileMap = new HashMap<LOG_TYPE, StoreFile>();
		for (LOG_TYPE type: LOG_TYPE.values()) {
		 	StoreFile storeFile = DBUtils.getStoreFile(type);
		 	
		 	
		 	checkabnormalDir(type,lastAsupid);
		 	
		 	checkabnormalFile(type,storeFile,lastAsupid);
		 	
		}
		
		
	}

	private void checkabnormalFile(LOG_TYPE type,StoreFile storeFile,Long lastAsupId) throws IOException {
		
		File file = storeFile.getStoreFile();
		
		// sbling 
		removeAbnormalFile(file.getParentFile(),lastAsupId);
		
		//storeFIle itself
		if(storeFile.getLastPos()!=file.length()){
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.setLength(storeFile.getLastPos());
		}
		
	}

	private void checkabnormalDir(LOG_TYPE type,Long lastAsupId) {
		File typeRootDir = new File(storageRootDir,type.getValue());
		removeAbnormalFile(typeRootDir, lastAsupId);
		
	}

	
	private void removeAbnormalFile(File dir, Long lastAsupId) {
		String[] childrenNames = dir.list();
		for (String name : childrenNames) {
			Long nameLong = Long.parseLong(name);
			if(nameLong.compareTo(lastAsupId)>0){
				File toBeDeletedFile =new File(dir,name);
				if(toBeDeletedFile.delete()){
					log.info("abnormal directory {} was deleted",toBeDeletedFile.getAbsoluteFile());
				}else {
					log.error("abnormal directory {} can't be deleted",toBeDeletedFile.getAbsoluteFile());;
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
	
	public void start(){
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
