package com.emc.prometheus.parser.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.StoreFile;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;


/** 
 * @author wade 
 * @version Nov 12, 2014 4:05:57 PM 
 */
public class DBUtils {
	
	final static Logger log = LoggerFactory.getLogger(DBUtils.class);

	
	
	private static Environment createEnvironment(String envHome) throws Exception {
		EnvironmentConfig envCfg = new EnvironmentConfig();
		envCfg.setAllowCreate(true);
		envCfg.setReadOnly(false);
		envCfg.setTransactional(true);

		Environment mydbEnv = new Environment(new File(envHome), envCfg);
		
		System.out.println("Env Config: " + mydbEnv.getConfig());
		
		return mydbEnv;
	}

	//update in memory db, wade TODO:
	public static void update(Object key, Object value){
		
	}

	public static StoreFile getStoreFile(LOG_TYPE logType) {
		// TODO Auto-generated method stub
		return null;
	}


}
