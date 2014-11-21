package com.emc.prometheus.parser.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.dao.LogDao;
import com.emc.prometheus.parser.dedupe.DedupProcessor;
import com.emc.prometheus.parser.dedupe.TsAndMsg;
import com.emc.prometheus.parser.parse.LogTimeHandler;
import com.emc.prometheus.parser.parse.Parser;
import com.emc.prometheus.parser.persist.FilePersistenceProcessor;
import com.emc.prometheus.parser.pojo.CompositeLogInfo;
import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.emc.prometheus.parser.pojo.ParsedLogs;
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
	
    public	CompositeLogInfo compositeLogInfo ;

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

	public void process() throws IOException {
		
		List<LogInfo> logInfos = compositeLogInfo.getLogInfos();
	
		for (LogInfo logInfo : logInfos) {
			try {
				process(logInfo);
			} catch (Exception e) {
				//TODO handler the exception
				e.printStackTrace();
			}
		}
				
	}
	
	
	//TODO convert to private after been tested
	public void process(LogInfo logInfo) throws Exception {
		
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

	private Map<LOG_TYPE, List<TsAndMsg>> getTsAndMsg(LogInfo logInfo) throws Exception {
		
		
		Map<LOG_TYPE, List<TsAndMsg>>  tsAndMsgMap = new HashMap<LOG_TYPE, List<TsAndMsg>>();
		
		//Timerhandler get Ts
		LogTimeHandler logTimeHandler = LogTimeHandler.getTimeHandlerInstance(logInfo.getType());
		
		ParsedLogs parsedLogs = logInfo.getParsedLogs();
		
		Map<LOG_TYPE, List<String>> parsedLogMap = parsedLogs.getParsedLogMap();
		Map<LOG_TYPE, List<String>> generatedDateMap = parsedLogs.getGeneratedDateMap();
		
		Set<LOG_TYPE> logTypeSet = parsedLogMap.keySet();
		
		Long epoch = logInfo.getEpoch();
		
		//different type message kern_info kern_error bios vtl_info
		for (LOG_TYPE type : logTypeSet) {
			
			List<TsAndMsg> tsAndMsgList = new ArrayList<>(); 
			String genDate=null;
			if(!generatedDateMap.get(type).isEmpty()){
				genDate = generatedDateMap.get(type).get(0);
			}
			List<String> msgs = parsedLogMap.get(type);
			
			long[] genDateHandler = logTimeHandler.genDateHandler(genDate, epoch, msgs);
			
			for (int i = 0; i < genDateHandler.length; i++) {
				TsAndMsg tsAndMsgObj = new TsAndMsg();
				tsAndMsgObj.setTs(genDateHandler[i]);
				tsAndMsgObj.setMsg(msgs.get(i));
				tsAndMsgList.add(tsAndMsgObj);
			}
			
			
			//sort
			
			Collections.sort(tsAndMsgList,new Comparator<TsAndMsg>() {
				@Override
				public int compare(TsAndMsg tsAndMsg1, TsAndMsg tsAndMsg2) {
					return tsAndMsg1.getTs().compareTo(tsAndMsg2.getTs());
				}
			});
			
			tsAndMsgMap.put(type,tsAndMsgList);
			
		}
		
		//set parsedLogs null , free memory
		parsedLogs=null;
		
		return tsAndMsgMap;
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
