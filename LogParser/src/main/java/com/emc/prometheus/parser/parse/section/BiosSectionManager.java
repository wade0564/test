package com.emc.prometheus.parser.parse.section;

import java.util.List;

import com.emc.prometheus.parser.parse.match.IndivMatchSimple;
import com.emc.prometheus.parser.parse.match.Match;
import com.emc.prometheus.parser.parse.match.ParentMatchSimple;
import com.emc.prometheus.parser.parse.regex.BiosParserRegExInfo;
import com.emc.prometheus.parser.parse.regex.KernelLogExInfo;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.ParsedLogs;

/**
 * For parent section: "==========  BIOS_TXT  ==========".
 */
public class BiosSectionManager {

	public static Match getManagerSection(ParsedLogs parsedLogs) {
					
		Match parentStart = new IndivMatchSimple("BIOS_TXT START",BiosParserRegExInfo.BiosStart);
		Match parentEnd = new IndivMatchSimple("BIOS_TXT END",BiosParserRegExInfo.BiosEnd);
		parentEnd.setConsumeTarget(false);
		ParentMatchSimple parent = new ParentMatchSimple("BIOS_TXT",parentStart,parentEnd);
		//bios file no need generated date
		parent.addMatch(getPipeMsg(parsedLogs.getMatchResultsStorage(LOG_TYPE.BIOS, SECTION_SEGMENT.BODY)));
		parent.addMatch(getAliveMsg(parsedLogs.getMatchResultsStorage(LOG_TYPE.BIOS, SECTION_SEGMENT.BODY)));
		parent.setContinueOnFail(true);
		return parent;
	}
	
	
	public static Match getSingleManagerSection(ParsedLogs parsedLogs) {
		
		ParentMatchSimple parent = new ParentMatchSimple("BIOS");
		parent.addMatch(getPipeMsg(parsedLogs.getMatchResultsStorage(LOG_TYPE.BIOS, SECTION_SEGMENT.BODY)));
		parent.addMatch(getAliveMsg(parsedLogs.getMatchResultsStorage(LOG_TYPE.BIOS, SECTION_SEGMENT.BODY)));
		parent.setContinueOnFail(true);
		return parent;
	}
	

	private static Match getAliveMsg(List<String> resultsStorage) {
		IndivMatchSimple match = new IndivMatchSimple("bios alive msg",BiosParserRegExInfo.File_Write_Pattern,resultsStorage);
		match.setRepeating(true);
		return match;
	}
	
	private static Match getPipeMsg(List<String> resultsStorage) {
		IndivMatchSimple match= new IndivMatchSimple("bios pipe msg",BiosParserRegExInfo.Data_Info_Pattern,resultsStorage);
		match.setRepeating(true);
		return match;
	}
	

}
