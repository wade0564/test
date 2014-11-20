package com.emc.prometheus.parser.main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.dao.LogDao;
import com.emc.prometheus.parser.dedupe.DedupProcessor;
import com.emc.prometheus.parser.dedupe.TsAndMsg;
import com.emc.prometheus.parser.parse.Parser;
import com.emc.prometheus.parser.persist.FilePersistenceProcessor;
import com.emc.prometheus.parser.pojo.CompositeLogInfo;
import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.emc.prometheus.parser.util.FileUtils;

@Component
public class ParserTask implements Runnable {

	@Autowired
	LogDao logDao;
	
	@Autowired
	Parser parser;

	@Autowired
	DedupProcessor dedupProcessor;
	
	@Autowired
	FilePersistenceProcessor filePersistenceProcessor;
	
	CompositeLogInfo compositeLogInfo ;

	@Override
	public void run() {

		while (true) {

			compositeLogInfo = logDao.getLogInfos();
			
			// if no file to parse , task over
			if(!compositeLogInfo.isEmpty()){
				try {
					process();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	private void process() throws IOException {
		
		List<LogInfo> logInfos = compositeLogInfo.getLogInfos();
	
		for (LogInfo logInfo : logInfos) {
			try {
				process(logInfo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
	}
	
	
	private void process(LogInfo logInfo) throws IOException {
		
		Map<File, LOG_FILE_TYPE> logFileMap = null;
		if(logInfo.getType()==LOG_FILE_TYPE.ASUP){
			logFileMap = FileUtils.getAsupLogFileMap(logInfo);
		}else {
			logFileMap = FileUtils.getSubLogFileMap(logInfo,compositeLogInfo.getSubLogMap().get(logInfo));
		}

		for (Entry<File, LOG_FILE_TYPE> logFileEntry : logFileMap.entrySet()) {

			parse(logInfo, logFileEntry);
			// get ts and msg order by ts
			Map<LOG_TYPE, List<TsAndMsg>> tsAndMsgMap = getTsAndMsg(logInfo);

			dedupe(logInfo, tsAndMsgMap);

			persist(logInfo, tsAndMsgMap);

		}
	}

	private Map<LOG_TYPE, List<TsAndMsg>> getTsAndMsg(LogInfo logInfo) {
		
		//Timerhandler get Ts
		
		return null;
	}

	private void parse(LogInfo logInfo, Entry<File, LOG_FILE_TYPE> logFileEntry) throws IOException {
		parser.parse(logInfo, logFileEntry);
		
	}

	private List<LogInfo> getLogInfo() {

		return null;

	}

	private void persist(LogInfo logInfo, Map<LOG_TYPE, List<TsAndMsg>> tsAndMsgMap) {
		// TODO Auto-generated method stub

	}

	private void dedupe(LogInfo logInfo, Map<LOG_TYPE, List<TsAndMsg>> tsAndMsgMap) {
		

	}


}
