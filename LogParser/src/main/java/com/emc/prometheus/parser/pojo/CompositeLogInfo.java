package com.emc.prometheus.parser.pojo;

import java.util.List;
import java.util.Map;

/** 
 * @author wade 
 * @version Nov 18, 2014 11:35:41 AM 
 */
public class CompositeLogInfo {
	
	List<LogInfo> logInfos;//These from geninfo table,exclude the asup which in sub file
	
	Map<LogInfo, List<LogInfo>> subLogMap;//This contains the asup,message,kern,bios log file in sub

	public List<LogInfo> getLogInfos() {
		return logInfos;
	}

	public void setLogInfos(List<LogInfo> logInfos) {
		this.logInfos = logInfos;
	}

	public Map<LogInfo, List<LogInfo>> getSubLogMap() {
		return subLogMap;
	}

	public void setSubLogMap(Map<LogInfo, List<LogInfo>> subLogMap) {
		this.subLogMap = subLogMap;
	}
	
	public boolean isEmpty(){
		if(logInfos==null || logInfos.isEmpty()){
			return true;
		}else {
			return false;
		}
	}
	
	
}
