package com.emc.prometheus.parser.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.emc.prometheus.parser.BaseTest;
import com.emc.prometheus.parser.pojo.LogInfo;

/** 
 * @author wade 
 * @version Nov 16, 2014 7:42:44 PM 
 */
public class LogDaoTest  extends BaseTest{

	
	@Autowired
	LogDao logDao;
	
	@Test
	public void testGetLogInfos() {
		
		
		List<LogInfo> logInfos = logDao.getLogInfos();
		
		for (LogInfo logInfo : logInfos) {
			System.out.println(logInfo);
		}
		
	}

}
