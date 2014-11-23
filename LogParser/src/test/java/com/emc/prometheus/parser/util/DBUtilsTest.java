package com.emc.prometheus.parser.util;

import java.io.File;
import java.util.Date;

import javax.management.RuntimeErrorException;

import org.junit.Test;

import com.emc.prometheus.parser.pojo.StoreFile;

/** 
 * @author wade 
 * @version Nov 23, 2014 10:12:47 AM 
 */
public class DBUtilsTest {
	
	
	
	
	@Test
	public void testPut(){
		
		String key ="test";
		
		StoreFile value =  new StoreFile(new File(new Date().toString()));
		
		DBUtils.update(key, value);
		
		
	}
	
	@Test
	public void testTransaction(){
		
		String key ="test";
		String key2 ="test2";
		
		DBUtils.beginTransaction();
		StoreFile value =  new StoreFile(new File("v1=="+new Date().toString()));
		StoreFile value2 =  new StoreFile(new File("v2=="+new Date().toString()));
		DBUtils.update(key, value);
		
		//cause exception manually
		int a= 1/0;
		
		DBUtils.update(key2, value2);
		DBUtils.commit();
		
	}
	@Test
	public void testGet(){
		
		String key ="test";
		String key2 ="test2";
		
		StoreFile value =(StoreFile) DBUtils.get(key, StoreFile.class);
		StoreFile value2 =(StoreFile) DBUtils.get(key2, StoreFile.class);
		
		System.out.println("======"+value.getStoreFile().getName());
		System.out.println("======"+value2.getStoreFile().getName());
		
	}
	

}
