package com.emc.prometheus.parser.util;

import java.awt.print.Printable;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import sun.util.logging.resources.logging;

import com.emc.prometheus.parser.BaseTest;
import com.emc.prometheus.parser.dao.LogDao;
import com.emc.prometheus.parser.pojo.CompositeLogInfo;
import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;

/** 
 * @author wade 
 * @version Nov 16, 2014 7:42:44 PM 
 */
public class FileUtilsTest  extends BaseTest{

	
	@Autowired
	LogDao logDao;
	
	@Test
	public void testGetLogInfos() {
		
		
		 CompositeLogInfo compositeLogInfo = logDao.getLogInfos();
		
		 
		 List<LogInfo> logInfosFromGeninfo = compositeLogInfo.getLogInfos();
		 
		 LogInfo logInfo =logInfosFromGeninfo.get(0);
		 
		 //sub
		 Map<File, LOG_FILE_TYPE> asupLogFileMap = FileUtils.getSubLogFileMap(logInfo,compositeLogInfo.getSubLogMap().get(logInfo));
		 //asup
//		 Map<File, LOG_FILE_TYPE> asupLogFileMap = FileUtils.getAsupLogFileMap(logInfosFromGeninfo.get(1));
		 
		 print(asupLogFileMap);
		 
//		 System.out.println("==============From Geninfo");
//		 for (LogInfo logInfo : logInfosFromGeninfo) {
//			 System.out.println(logInfo);
//		}
//		 
//		 Map<LogInfo, List<LogInfo>> subLogMap = logInfos.getSubLogMap();
//		 if(subLogMap.isEmpty()){
//			 return ;
//		 }
//		 System.out.println("==============From SubContent");
//		 for (List<LogInfo> logInfo : subLogMap.values()) {
//			 for (LogInfo info : logInfo) {
//				 System.out.println(info);
//			}
//		}
		
	}

	private void print(Map<File, LOG_FILE_TYPE> asupLogFileMap) {
		
		for (Entry<File, LOG_FILE_TYPE> entry  : asupLogFileMap.entrySet()) {
			
			System.out.println(entry.getKey().getAbsolutePath()+"|"+entry.getValue());
			
		}
		
	}

}
