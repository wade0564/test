package com.emc.prometheus.parser.util;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.junit.Test;

import com.emc.prometheus.parser.dedupe.Range;
import com.emc.prometheus.parser.dedupe.RangeNode;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.StoreFile;

/** 
 * @author wade 
 * @version Nov 23, 2014 10:12:47 AM 
 */
public class DBUtilsTest {
	
	
	
	
	@Test
	public void testPut(){
		
		String key =LOG_TYPE.MESSAGES.toString();
		
		StoreFile value =  new StoreFile(new File(new Date().toString()));
		
		DBUtils.update(key, value);
		
	}
	
	@Test
	public void testPutList() throws InterruptedException{
		
		String key ="AI201418_MESSAGE";
		
		RangeNode  start = new RangeNode(new Date().getTime(), "Start", 1);
		Thread.sleep(1000);
		RangeNode  end = new RangeNode(new Date().getTime(), "End", -1);
		
		Range range = new Range(start, end); 
		
		List<Range> list = new ArrayList<>();
		list.add(range);
		DBUtils.update(key,list);
		
	}
	
	@Test
	public void testDelete(){
		String key =LOG_TYPE.MESSAGES.toString();
//		String key ="test";
		DBUtils.delete(key);
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
		String key3 ="AI201418_MESSAGE";
//		StoreFile value =(StoreFile) DBUtils.get(key, StoreFile.class);
//		StoreFile value2 =(StoreFile) DBUtils.get(key2, StoreFile.class);
		List<Range> ranges=(List<Range>) DBUtils.get(key3, Range.class);
		
//		System.out.println("======"+value.getStoreFile().getName());
//		System.out.println("======"+value2.getStoreFile().getName());
		for (Range range : ranges) {
			System.out.println(range);
		}
		
	}
	
	@Test 
	public void printAllEntryTest(){
		DBUtils.printAllDataBaseEntry();
	}
	

}
