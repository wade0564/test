package com.emc.prometheus.asupqueuing.dao;

import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/app-context.xml" })
public class GpUploadDaoTest extends TestCase {
	
	@Autowired
	GpUploadDao dao ;
	
	@Test
	public void testGetUniqueAsupID() throws SQLException {
		
		
		
		long timeTestStart=System.currentTimeMillis();
//		
		BaseDao dao2 =new BaseDao();
//		
		for (int i = 0; i < 2; i++) {
			// System.out.println(dao.getUniqueAsupID("3FZ2147030-1392386848"));
			long timeTestStart2 = System.currentTimeMillis();
			dao2.query(
					"select asup_unique_id from asup.asup where asup_unique_id =?",
					"v");
			long timeTestEnd = System.currentTimeMillis();// 记录结束时间
			System.out.println("运行时间是" + (timeTestEnd - timeTestStart2));
		}
		
		long timeTestEnd=System.currentTimeMillis();//记录结束时间
		
		
		System.out.println("运行时间是"+(timeTestEnd-timeTestStart));
		
	}
	
	
	@Test
	public void testbatchpuload(){
		dao.testBatchUpdate();
	}
}
