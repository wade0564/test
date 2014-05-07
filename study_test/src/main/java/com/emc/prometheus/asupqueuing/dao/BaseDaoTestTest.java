package com.emc.prometheus.asupqueuing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.spi.DirStateFactory.Result;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class BaseDaoTestTest extends TestCase {
	

	@Test
	public void testGetUniqueAsupID() throws SQLException, InterruptedException {
		
		
		BaseDao dao =new BaseDao();
		
		ResultSet rs=dao.query("select count(*) cnt from asup.kernel_error_log ");
		
		System.out.println(rs.getInt("cnt"));
		
		
		
	}
}
