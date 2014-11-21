package com.emc.prometheus.parser.parse.section;

import java.util.List;

import com.emc.prometheus.parser.parse.match.IndivMatchSimple;
import com.emc.prometheus.parser.parse.match.Match;
import com.emc.prometheus.parser.parse.match.ParentMatchSimple;
import com.emc.prometheus.parser.parse.regex.KernelLogExInfo;
import com.emc.prometheus.parser.parse.regex.RegExMajorSections;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.ParsedLogs;

/**
 * For parent section: "==========  MESSAGES  ==========".
 */
public class MessagesSectionManager {

	public static Match getManagerSection(ParsedLogs parsedLogs) {
					
		Match parentStart = new IndivMatchSimple("MESSAGE START",RegExMajorSections.MESSAGES);
		Match parentEnd = new IndivMatchSimple("MESSAGE END",RegExMajorSections.Kern_Info1);
		parentEnd.setConsumeTarget(false);
		ParentMatchSimple parent = new ParentMatchSimple("MESSAGE",parentStart,parentEnd);
		
		parent.addMatch(getLogTs(parsedLogs.getMatchResultsStorage(LOG_TYPE.MESSAGES, SECTION_SEGMENT.GENERATED_DATE)));
		parent.addMatch(getLogMsg(parsedLogs.getMatchResultsStorage(LOG_TYPE.MESSAGES, SECTION_SEGMENT.BODY)));
		
		return parent;
	}
	
	
	public static Match getSingleManagerSection(ParsedLogs parsedLogs) {
		
		ParentMatchSimple parent = new ParentMatchSimple("MESSAGE");
		parent.addMatch(getLogMsg(parsedLogs.getMatchResultsStorage(LOG_TYPE.MESSAGES, SECTION_SEGMENT.BODY)));
		
		return parent;
	}
	

	private static Match getLogMsg(List<String> resultsStorage) {
		Match msg = new IndivMatchSimple("MESSAGE msg",KernelLogExInfo.anylog_data,resultsStorage);
		return msg;
	}

	//Generated Date match
	private static Match getLogTs(List<String> resultsStorage) {
		Match msg = new IndivMatchSimple("MESSAGE generated date",KernelLogExInfo.Generated,resultsStorage);
		return msg;
	}

}
