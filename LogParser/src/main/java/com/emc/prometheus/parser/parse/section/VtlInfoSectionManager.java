package com.emc.prometheus.parser.parse.section;

import java.util.List;

import com.emc.prometheus.parser.parse.match.IndivMatchMerge;
import com.emc.prometheus.parser.parse.match.IndivMatchSimple;
import com.emc.prometheus.parser.parse.match.Match;
import com.emc.prometheus.parser.parse.match.ParentMatchSimple;
import com.emc.prometheus.parser.parse.regex.VtlInfoParserRegExInfo;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.ParsedLogs;

/**
 * For parent section: "==========  VTL.INFO  ==========".
 */
public class VtlInfoSectionManager {

	public static Match getManagerSection(ParsedLogs parsedLogs) {
					
		Match parentStart = new IndivMatchSimple("VTL.INFO START",VtlInfoParserRegExInfo.VtlStart);
		Match parentEnd = new IndivMatchSimple("VTL.INFO",VtlInfoParserRegExInfo.VtlEnd);
		parentEnd.setConsumeTarget(false);
		ParentMatchSimple parent = new ParentMatchSimple("VTL.INFO",parentStart,parentEnd);
		
		parent.addMatch(getLogTs(parsedLogs.getMatchResultsStorage(LOG_TYPE.VTL_INFO, SECTION_SEGMENT.GENERATED_DATE)));
		parent.addMatch(getLogMsg(parsedLogs.getMatchResultsStorage(LOG_TYPE.VTL_INFO, SECTION_SEGMENT.BODY)));
		parent.addMatch(getMergedLogMsg(parsedLogs.getMatchResultsStorage(LOG_TYPE.VTL_INFO, SECTION_SEGMENT.BODY)));
		return parent;
	}
	
	
	public static Match getSingleManagerSection(ParsedLogs parsedLogs) {
		
		ParentMatchSimple parent = new ParentMatchSimple("VTL.INFO");
		parent.addMatch(getLogMsg(parsedLogs.getMatchResultsStorage(LOG_TYPE.VTL_INFO, SECTION_SEGMENT.BODY)));
		parent.addMatch(getMergedLogMsg(parsedLogs.getMatchResultsStorage(LOG_TYPE.VTL_INFO, SECTION_SEGMENT.BODY)));
		return parent;
	}
	
	private static Match getMergedLogMsg(List<String> resultsStorage) {
		IndivMatchMerge match = new IndivMatchMerge("VTL.INFO merged msg",VtlInfoParserRegExInfo.Msg_Reg_Sub_Pattern,resultsStorage);
		match.setRepeating(true);
		return match;
	}

	private static Match getLogMsg(List<String> resultsStorage) {
		IndivMatchSimple match = new IndivMatchSimple("VTL.INFO msg",VtlInfoParserRegExInfo.Msg_Reg_Pattern,resultsStorage);
		match.setRepeating(true);
		return match;
	}

	//Generated Date match
	private static Match getLogTs(List<String> resultsStorage) {
		Match match = new IndivMatchSimple("VTL.INFO generated date",VtlInfoParserRegExInfo.Generated,resultsStorage);
		return match;
	}

}
