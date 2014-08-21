package com.emc.prometheus.asupqueuing.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.emc.prometheus.asupqueuing.pojo.ASUPQUEUEOBJ_STATUS;
import com.emc.prometheus.asupqueuing.pojo.AsupQueueObj;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/app-context.xml" })
public class GpUploadDaoSpringTest extends TestCase {
	
	@Autowired
	GpUploadDao dao ;
	
	
	
	@Test
	public void testGetUniqueAsupID() throws SQLException {
		
		
		
		long timeTestStart=System.currentTimeMillis();
//		
//		
		for (int i = 0; i < 2000; i++) {
			long timeTestStart2 = System.currentTimeMillis();
			 System.out.println(dao.getUniqueAsupID("3FZ2147030-1392386848"));
			long timeTestEnd = System.currentTimeMillis();// 记录结束时间
			System.out.println("运行时间是" + (timeTestEnd - timeTestStart2));
		}
		
		long timeTestEnd=System.currentTimeMillis();//记录结束时间
		
		
		System.out.println("运行时间是"+(timeTestEnd-timeTestStart));
		
	}
	
	
	@Test
	public void getAsupQueueObjsTest() throws IOException{
		
		List<AsupQueueObj> asupQueueObjs = dao.getAsupQueueObjs(1);
		
		Map<BigInteger, AsupQueueObj> maps =new HashMap<BigInteger, AsupQueueObj>();
		
		BigInteger index =new BigInteger(0+"");
		
		for (AsupQueueObj asupQueueObj : asupQueueObjs) {
			maps.put(index.add(new BigInteger(1+"")), asupQueueObj);
			index=index.add(new BigInteger(1+""));
//			asupQueueObj.setAsupid(index);
			System.out.println(asupQueueObj.getFilehandle());
		}
		
//		dao.writeToExternalFile(maps);
		
	}
	
	@Test
	public void genUpdateSql(){
		
		Map< BigInteger, AsupQueueObj> maps= new HashMap<BigInteger, AsupQueueObj>();
		
		for(int i =0 ;i<5;i++){
			AsupQueueObj obj =new AsupQueueObj();
			obj.setFilehandle("2014-04-09_22-12-15.10717");
		if(i==2){
			obj.setParse_status(ASUPQUEUEOBJ_STATUS.PARSE_FAILD);
		}else if(i==3){
			obj.setParse_status("DUPLICATED");
		}else{
			obj.setParse_status(ASUPQUEUEOBJ_STATUS.PARSE_SUCCESS);
		}
			obj.setId(BigInteger.valueOf(i));
			obj.setFileSource("customer");
			obj.setSn("2FZ2338141");
			obj.setFileType("autosupoort");
			obj.setModel("");
			maps.put(BigInteger.valueOf(i), obj);
		}
		
		String[] genUpdateSql = dao.genUpdateSql(maps);
		
		for (String string : genUpdateSql) {
			System.out.println(string);
		}
		
	}
	
	@Test
	public void writeToExternalFileTest() throws IOException {
		Map<BigInteger, AsupQueueObj> maps = new HashMap<BigInteger, AsupQueueObj>();

		for (int i = 0; i < 5; i++) {
			AsupQueueObj obj = new AsupQueueObj();
			obj.setFilehandle("2014-04-09_22-12-15.10717");
			if (i == 2) {
				obj.setParse_status(ASUPQUEUEOBJ_STATUS.PARSE_FAILD);
			} else if (i == 3) {
				obj.setParse_status("DUPLICATED");
			} else {
				obj.setParse_status(ASUPQUEUEOBJ_STATUS.PARSE_SUCCESS);
			}
			obj.setId(BigInteger.valueOf(i));
			obj.setFileSource("customer");
			obj.setSn("2FZ2338141");
			obj.setFileType("autosupoort");
			obj.setModel("");
			maps.put(BigInteger.valueOf(i), obj);
		}

		dao.writeToExternalFile(maps);

	}
	
	@Test
	public void testCreateExternalTable() throws SQLException{
		dao.createExternalTable();
	}
	
	@Test
	public void testSQL() throws SQLException{
		dao.insert();
	}
	
}
