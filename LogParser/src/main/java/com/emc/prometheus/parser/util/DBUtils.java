package com.emc.prometheus.parser.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.emc.prometheus.parser.dedupe.Range;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.emc.prometheus.parser.pojo.StoreFile;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Cursor;
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
	
	public final static String LAST_ASUPID ="lastAsupid";
	
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
		// Ranges
		StoredClassCatalog rangeCatalog = new StoredClassCatalog(catalogDatabase); 
		EntryBinding<ArrayList> rangeBinding = new SerialBinding(rangeCatalog, ArrayList.class); 
		//StoreFile
		StoredClassCatalog storeFileCatalog = new StoredClassCatalog(catalogDatabase); 
		EntryBinding<StoreFile> storeFileBinding = new SerialBinding<StoreFile>(storeFileCatalog, StoreFile.class); 
		//lastAsupid
		StoredClassCatalog asupIdCatalog = new StoredClassCatalog(catalogDatabase); 
		EntryBinding<Long> asupIdBinding = new SerialBinding<Long>(asupIdCatalog, Long.class); 
		classBindingMap.put(Range.class, rangeBinding);
		classBindingMap.put(StoreFile.class, storeFileBinding);
		classBindingMap.put(Long.class, asupIdBinding);
		
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
		log.info("In-Memeory DB begin transaction");
		transaction = environment.beginTransaction(null, null);
	}
	
	public static void commit(){
		transaction.commit();
		log.info("In-Memeory DB commit");
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
	
	//update in memory db, wade TODO:
		public static boolean update(Object key, Collection value ,Class c){
			
			 EntryBinding valueBinding =classBindingMap.get(c);
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
		return (StoreFile) get(logType.toString(), StoreFile.class);
	}
	
	public static List<Range> getRanges(LogInfo logInfo,LOG_TYPE type) {
		
		//PSNT process
		
		List<Range> ranges = new ArrayList<>();
		ranges = (List<Range>) get(logInfo.getSn()+"_"+type.toString(), Range.class);
		if(ranges == null){
			//chassis sn have been stored ,but new sn have not updated 
			if(!logInfo.getSn().equals(logInfo.getChassis_sn())){
				ranges = (List<Range>) get(logInfo.getChassis_sn()+"_"+type.toString(), Range.class);
				if(ranges!=null){
					//update new sn in db
					beginTransaction();
					//add new sn
					update(logInfo.getSn()+"_"+type.toString(), ranges ,Range.class);
					//delete old sn
					delete(logInfo.getChassis_sn()+"_"+type.toString());
					commit();
				}
			}
			
			//other case
			//sn  not been stored  before , no handler
			
			
		}
		
		if(logInfo.getChassis_sn()==null){
			get(logInfo.getSn()+"_"+type.toString(), Range.class);
		}else {
			get(logInfo.getSn()+"_"+type.toString(), Range.class);
		}
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
	
//	public static void clear(){
//		database.
//	}
	
	public static void printAllDataBaseEntry(){
		
		Cursor cursor = database.openCursor(transaction, null);
		
	    // Cursors need a pair of DatabaseEntry objects to operate. These hold
	    // the key and data found at any given position in the database.
	    DatabaseEntry foundKey = new DatabaseEntry();
	    DatabaseEntry foundData = new DatabaseEntry();

	    // To iterate, just call getNext() until the last database record has been 
	    // read. All cursor operations return an OperationStatus, so just read 
	    // until we no longer see OperationStatus.SUCCESS
	    while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) ==
	        OperationStatus.SUCCESS) {
	        String keyString = new String(foundKey.getData());
	        
	        LOG_TYPE[] types = LOG_TYPE.values();
	        int i ;
	        for (i = 0; i < types.length; i++) {
				LOG_TYPE type = types[i];
				if(keyString.equals(type.toString())){
					StoreFile storeFile = (StoreFile) classBindingMap.get(StoreFile.class).entryToObject(foundData);
					log.info("====================KEY :{}\n{}" ,type.toString(),storeFile.toString());
					break;
				}
			}
	        if(i<types.length){
	        	continue;
	        }
	        
	        if(keyString.endsWith(LAST_ASUPID)){
	        	log.info(LAST_ASUPID + ":" + classBindingMap.get(Long.class).entryToObject(foundData));
	        	continue;
	        }
	        
	        //Ranges
	        List<Range> ranges =(List<Range>) classBindingMap.get(Range.class).entryToObject(foundData);
	        for (Range range : ranges) {
				log.info(range.toString());
			}
	        
	    }
	}

	public static void delete(String key) {
		 OperationStatus status = database.delete(null, new DatabaseEntry(key.getBytes()));
		 log.info("Delete Status :"+status .toString());
	}

}
