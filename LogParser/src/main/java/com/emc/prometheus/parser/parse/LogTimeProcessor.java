package com.emc.prometheus.parser.parse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.emc.prometheus.parser.dedupe.TsAndMsg;
import com.emc.prometheus.parser.parse.regex.BiosParserRegExInfo;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.emc.prometheus.parser.pojo.ParsedLogs;

/** 
 * @author wade 
 * @version Nov 23, 2014 2:44:15 PM 
 */

public class LogTimeProcessor {
	
	
	private static Pattern pipeDataPattern =  Pattern.compile(BiosParserRegExInfo.Data_Info_Reg); 
	private static Pattern aliveDataPattern =  Pattern.compile(BiosParserRegExInfo.File_write_Reg); 
	private static SimpleDateFormat pipeDateFormat = new SimpleDateFormat("MM/dd/yyyy | hh:mm:ss");
	private static SimpleDateFormat aliveDateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
	
	
	public static Map<LOG_TYPE, List<TsAndMsg>> getTsAndMsg(LogInfo logInfo) throws Exception {

		
		Map<LOG_TYPE, List<TsAndMsg>>  tsAndMsgMap = new HashMap<LOG_TYPE, List<TsAndMsg>>();
		
		ParsedLogs parsedLogs = logInfo.getParsedLogs();
		
		Map<LOG_TYPE, List<String>> parsedLogMap = parsedLogs.getParsedLogMap();
		Map<LOG_TYPE, List<String>> generatedDateMap = parsedLogs.getGeneratedDateMap();
		
		Set<LOG_TYPE> logTypeSet = parsedLogMap.keySet();
		
		Long epoch = logInfo.getEpoch();
		
		//different type message kern_info kern_error bios vtl_info
		for (LOG_TYPE type : logTypeSet) {
			List<TsAndMsg> tsAndMsgList = new ArrayList<>(); 
			List<String> msgs = parsedLogMap.get(type);
			long[] genDateHandler =null;
			if(type!=LOG_TYPE.BIOS){
				//Timerhandler get Ts
				LogTimeHandler logTimeHandler = LogTimeHandler.getTimeHandlerInstance(type);
				String genDate=null;
				if(!generatedDateMap.get(type).isEmpty()){
					genDate = generatedDateMap.get(type).get(0);
				}
				genDateHandler = logTimeHandler.genDateHandler(genDate, epoch, msgs);
			}else{
				//Bios no need Timehandler
				genDateHandler = getBiosDateHandler(msgs);
			}
			
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
		
		//set parsedLogs clear , free memory
		parsedLogs.clear();
		
		return tsAndMsgMap;
		
	}

	private static long[] getBiosDateHandler(List<String> msgs) throws ParseException {
		
		long[] timestamps = new long[msgs.size()];
	
		for (int i = 0; i < msgs.size(); i++) {
			Matcher m = aliveDataPattern.matcher(msgs.get(i));
			if(m.find()){
				//eg. Fri Oct  7 06:00:01 BST 2011 
				timestamps[i]= aliveDateFormat.parse(m.group(2)).getTime();
			}else {
				//eg. 08/22/2011 | 19:20:52 
				m=pipeDataPattern.matcher(msgs.get(i));
				if(m.find()){
					timestamps[i]= pipeDateFormat.parse(m.group(2)).getTime();
				}
			}
		}
				
		return timestamps;
	}

}
