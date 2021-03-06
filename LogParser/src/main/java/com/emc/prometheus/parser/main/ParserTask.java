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
import com.emc.prometheus.parser.dedupe.Range;
import com.emc.prometheus.parser.dedupe.TsAndMsg;
import com.emc.prometheus.parser.parse.LogTimeProcessor;
import com.emc.prometheus.parser.parse.Parser;
import com.emc.prometheus.parser.parse.exception.TimeHandlerException;
import com.emc.prometheus.parser.persist.FilePersistenceProcessor;
import com.emc.prometheus.parser.pojo.CompositeLogInfo;
import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.emc.prometheus.parser.util.DBUtils;
import com.emc.prometheus.parser.util.FileUtils;

@Component
public class ParserTask{

	@Autowired
	LogDao logDao;
	@Autowired
	Parser parser;
	@Autowired
	FilePersistenceProcessor filePersistenceProcessor;
	
    public	CompositeLogInfo compositeLogInfo ;

	public void run() throws Exception {

		while (true) {
			compositeLogInfo = logDao.getLogInfos();
			// if no file to parse , task over
			if(!compositeLogInfo.isEmpty()){
				process();
				clean();
			}else {
				break;
			}
		}
	}

	public void process() throws Exception {
		
		List<LogInfo> logInfos = compositeLogInfo.getLogInfos();
	
		for (LogInfo logInfo : logInfos) {
				process(logInfo);
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
			//transaction begin
			DBUtils.beginTransaction();
			
			dedupe(logInfo, tsAndMsgMap);

			persist(logInfo, tsAndMsgMap);
			
			updateDB(logInfo);
			//commit
			DBUtils.commit();
		}
	
	}



	private void parse(LogInfo logInfo, Entry<File, LOG_FILE_TYPE> logFileEntry) throws IOException {
		parser.parse(logInfo, logFileEntry);
	}

	private Map<LOG_TYPE, List<TsAndMsg>> getTsAndMsg(LogInfo logInfo) throws TimeHandlerException {
		
		Map<LOG_TYPE, List<TsAndMsg>>  tsAndMsgMap = LogTimeProcessor.getTsAndMsg(logInfo);
		
		return tsAndMsgMap;
	}

	private void dedupe(LogInfo logInfo, Map<LOG_TYPE, List<TsAndMsg>> tsAndMsgMap) {
		
		for (Entry<LOG_TYPE, List<TsAndMsg>> entry : tsAndMsgMap.entrySet()) {
			if(entry.getValue().isEmpty()){
				continue;
			}
			
			String snKey = logInfo.getSn()+"_"+entry.getKey().toString();
			List<Range> ranges = logDao.getExistedRanges(snKey);
			DedupProcessor.dedup(ranges, entry.getValue());
			//update
			DBUtils.update(snKey, ranges,Range.class);
		}
		
	}
	
	private void persist(LogInfo logInfo, Map<LOG_TYPE, List<TsAndMsg>> tsAndMsgMap) throws Exception {
		for (Entry<LOG_TYPE, List<TsAndMsg>> entry : tsAndMsgMap.entrySet()) {
			if(entry.getValue().isEmpty()){
				continue;
			}
			filePersistenceProcessor.persist(logInfo, entry.getKey(), entry.getValue());
		}

	}
	
	private void updateDB(LogInfo logInfo) {
		//update asupid
		DBUtils.update(DBUtils.LAST_ASUPID, logInfo.getAsupId());
	}
	
	private void clean() {
		
		compositeLogInfo = null;
		
	}
}

