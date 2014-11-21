package com.emc.prometheus.parser.pojo;

import java.util.List;
import java.util.Map;

import com.emc.prometheus.parser.parse.section.SECTION_SEGMENT;

public class ParsedLogs {

	Map<LOG_TYPE, List<String>> parsedLogMap;
	Map<LOG_TYPE, List<String>> generatedDateMap; // To keep consistent with parsedLogMap in Match method ,define the value of the map as List<String>

	public ParsedLogs() {
	}

	public List<String> getParsedLogs(LOG_TYPE type) {
		return parsedLogMap.get(type);
	}

	public Map<LOG_TYPE, List<String>> getParsedLogMap() {
		return parsedLogMap;
	}

	public void setParsedLogMap(Map<LOG_TYPE, List<String>> parsedLogMap) {
		this.parsedLogMap = parsedLogMap;
	}


	public Map<LOG_TYPE, List<String>> getGeneratedDateMap() {
		return generatedDateMap;
	}

	public void setGeneratedDateMap(Map<LOG_TYPE, List<String>> generatedDateMap) {
		this.generatedDateMap = generatedDateMap;
	}

	public List<String> getMatchResultsStorage(LOG_TYPE logType,SECTION_SEGMENT segment) {
		
		List<String> storage = null;
		
		switch (segment) {
		case BODY:
			storage=parsedLogMap.get(logType);
			break;
		case GENERATED_DATE:
			storage=generatedDateMap.get(logType);
		default:
			break;
		}
		
		return storage;
	}

	
	
	

}
