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
public class KernelErrorLogSectionManager {

	public static Match getManagerSection(ParsedLogs parsedLogs) {
					
		Match parentStart = new IndivMatchSimple("KERN.ERROR START",KernelLogExInfo.KernelErrorstart);
		Match parentEnd = new IndivMatchSimple("KERN.ERROR END",KernelLogExInfo.KernelErrorend);
		
		ParentMatchSimple parent = new ParentMatchSimple("KERN.ERROR",parentStart,parentEnd);
		
		parent.addMatch(getLogTs(parsedLogs.getMatchResultsStorage(LOG_TYPE.KERN_ERROR, SECTION_SEGMENT.GENERATED_DATE)));
		parent.addMatch(getLogMsg(parsedLogs.getMatchResultsStorage(LOG_TYPE.KERN_ERROR, SECTION_SEGMENT.BODY)));
		
		return parent;
	}
	

	private static Match getLogMsg(List<String> resultsStorage) {
		Match msg = new IndivMatchSimple("KERN.ERROR msg",KernelLogExInfo.anylog_data,resultsStorage);
		return msg;
	}

	//Generated Date match
	private static Match getLogTs(List<String> resultsStorage) {
		Match msg = new IndivMatchSimple("KERN.ERROR generated date",KernelLogExInfo.Generated,resultsStorage);
		return msg;
	}

}
