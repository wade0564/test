package com.emc.prometheus.parser.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.emc.prometheus.parser.dedupe.Range;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.StoreFile;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;


/** 
 * @author wade 
 * @version Nov 12, 2014 4:05:57 PM 
 */
public class DBUtils {
	
	final static Logger log = LoggerFactory.getLogger(DBUtils.class);
	
	private static String ENV_PATH ;
	
	private static Environment environment;
	
	private static Database database;
	
	private  static Database catalogDatabase;

	private static Transaction transaction;
	
	private static Map<Class,EntryBinding> classBindingMap ;
	
	
	static{
		try {
			ENV_PATH = PropertiesLoaderUtils.loadAllProperties("logParser.properties").getProperty("LOG.PARSER.IN_MEMORY_DB.EVN_PATH");
			environment = createEnvironment(ENV_PATH);
			database = createDatabase(environment,"Binding Database");
			catalogDatabase=createDatabase(environment, "Catalog database");
			createClassBindingMap();
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			log.error("Can't find the configuration files {}","logParser.properties");
		}catch (Exception e) {
			log.error(e.getMessage(),e);
			log.error("Can't create In-Memory Database");
		}
	}

	private static void createClassBindingMap() {
		classBindingMap= new HashMap<Class, EntryBinding>();
		StoredClassCatalog rangeCatalog = new StoredClassCatalog(catalogDatabase); 
		EntryBinding<Range> rangeBinding = new SerialBinding<Range>(rangeCatalog, Range.class); 
		StoredClassCatalog storeFileCatalog = new StoredClassCatalog(catalogDatabase); 
		EntryBinding<StoreFile> storeFileBinding = new SerialBinding<StoreFile>(storeFileCatalog, StoreFile.class); 
		
		classBindingMap.put(Range.class, rangeBinding);
		classBindingMap.put(StoreFile.class, storeFileBinding);
		
	}
	
	private static Environment createEnvironment(String envHome) throws Exception {
		EnvironmentConfig envCfg = new EnvironmentConfig();
		envCfg.setAllowCreate(true);
		envCfg.setReadOnly(false);
		envCfg.setTransactional(true);

		Environment mydbEnv = new Environment(new File(envHome), envCfg);
		System.out.println("Env Config: " + mydbEnv.getConfig());
		
		return mydbEnv;
	}
	
	
	private static Database createDatabase(Environment dbEnv,String databaseName) throws Exception {
		DatabaseConfig dbCfg = new DatabaseConfig();
		dbCfg.setAllowCreate(true);
		dbCfg.setReadOnly(false);
		dbCfg.setTransactional(true);
		
		Database database = dbEnv.openDatabase(null, databaseName, dbCfg);
		log.info("Database Name: {}" , database.getDatabaseName());
		
		return database;
	}
	
	
	public static void beginTransaction(){
		transaction = environment.beginTransaction(null, null);
	}
	
	public static void commit(){
		transaction.commit();
		transaction=null;
	}
		
	
	//update in memory db, wade TODO:
	public static boolean update(Object key, Object value){
		 EntryBinding valueBinding =classBindingMap.get(value.getClass());
		 DatabaseEntry keyEntry = new DatabaseEntry(key.toString().getBytes());  
	     DatabaseEntry valueEntry = new DatabaseEntry();  
	     valueBinding.objectToEntry(value, valueEntry);
	     OperationStatus status = database.put(transaction, keyEntry, valueEntry);  
	     
	     if(status==OperationStatus.SUCCESS){
	    	 log.info("In memeroy DB update SUCCESS:{}",value);
	    	 return true;
	     }else {
	    	 log.info("In memeroy DB update FAIL :{}",value);
			return false;
		}
	
	}
	
	
	public static Object get(String key,Class c){
		DatabaseEntry keyEntry =  new DatabaseEntry(key.toString().getBytes());
		DatabaseEntry valueEntry = new DatabaseEntry();
		OperationStatus operationStatus = database.get(transaction, keyEntry, valueEntry, LockMode.DEFAULT);
		
		if(operationStatus==OperationStatus.SUCCESS){
			Object o = classBindingMap.get(c).entryToObject(valueEntry);
			return  o;
		}else{
			return  null;
		}
	}

	public static StoreFile getStoreFile(LOG_TYPE logType) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public static void close() {
		
		//close database
		if (database != null) {
			database.close();
			database = null;
		}

		// close database environment
		if (environment != null) {
			environment.sync();// sync to disk
			environment.cleanLog();// clean log
			environment.close();
			environment = null;
		}
	}

}
