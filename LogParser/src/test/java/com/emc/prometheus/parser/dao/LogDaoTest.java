package com.emc.prometheus.parser.dao;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.emc.prometheus.parser.BaseTest;
import com.emc.prometheus.parser.pojo.CompositeLogInfo;
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
		
		
		 CompositeLogInfo logInfos = logDao.getLogInfos();
		
		 
		 List<LogInfo> logInfosFromGeninfo = logInfos.getLogInfos();
		 
		 System.out.println("==============From Geninfo");
		 for (LogInfo logInfo : logInfosFromGeninfo) {
			 System.out.println(logInfo);
		}
		 
		 Map<LogInfo, List<LogInfo>> subLogMap = logInfos.getSubLogMap();
		 if(subLogMap.isEmpty()){
			 return ;
		 }
		 System.out.println("==============From SubContent");
		 for (List<LogInfo> logInfo : subLogMap.values()) {
			 for (LogInfo info : logInfo) {
				 System.out.println(info);
			}
		}
		
	}

}
