package com.emc.prometheus.parser.persist;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.emc.prometheus.parser.BaseTest;
import com.emc.prometheus.parser.dedupe.TsAndMsg;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;

public class FilePersistenceTest extends BaseTest {
	
	@Autowired
	FilePersistenceProcessor fpp;

	@Test
	public void test1() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585858L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.MESSAGES, tsAndMsgs);
	}
	
	@Test
	public void test2() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585859L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.MESSAGES, tsAndMsgs);
	}
	
	@Test
	public void test3() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585860L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.MESSAGES, tsAndMsgs);
	}
	
	@Test
	public void test4() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585861L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.MESSAGES, tsAndMsgs);
	}
	
	@Test
	public void test5() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585862L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.MESSAGES, tsAndMsgs);
	}
	
	
	@Test
	public void test6() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585863L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.MESSAGES, tsAndMsgs);
	}
	
	
	@Test
	public void test7() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585864L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.MESSAGES, tsAndMsgs);
	}
	
	@Test
	public void test8() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585865L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.MESSAGES, tsAndMsgs);
	}
	
	
	@Test
	public void test9() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585866L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.MESSAGES, tsAndMsgs);
	}
	
	
	@Test
	public void test10() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585858L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.KERN_INFO, tsAndMsgs);
	}
	
	
	@Test
	public void test11() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585859L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.KERN_INFO, tsAndMsgs);
	}
	
	
	@Test
	public void test12() throws Exception{
		LogInfo logInfo = new LogInfo();
		logInfo.setAsupId(58585866L);
		List<TsAndMsg> tsAndMsgs = PrepareTestData.prepare();
		
		fpp.persist(logInfo, LOG_TYPE.KERN_INFO, tsAndMsgs);
	}
}
