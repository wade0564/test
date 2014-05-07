package cn.edu.ustc.wade.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/app-test-context.xml" })
public class GpUploadDao2Test extends TestCase {
	
	@Autowired
	GpUploadDao dao ;

	@Autowired
	DataSource ds ;
	@Test
	public void testGetUniqueAsupID() throws SQLException, InterruptedException {
		
		long timeTestStart=System.currentTimeMillis();
//		
//		
		for (int i = 0; i < 30; i++) {
			new Thread(new Runnable() {
				
				public void run() {
					try {
						long timeTestStart=System.currentTimeMillis();
						System.out.println("start:"+timeTestStart);
						ds.getConnection().createStatement().executeQuery("select asup_unique_id from asup.asup where asup_unique_id ='asdf'");
						long timeTestEnd=System.currentTimeMillis();//记录结束时间
						System.out.println("end:"+timeTestEnd);
						System.out.println("运行时间是"+(timeTestEnd-timeTestStart));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}).start();
//			dao.getUniqueAsupID("asdfas");
		}
		
		Thread.sleep(13*3600);
		
		long timeTestEnd=System.currentTimeMillis();//记录结束时间
		
		
//		System.out.println("运行时间是"+(timeTestEnd-timeTestStart));
		
	}
}
