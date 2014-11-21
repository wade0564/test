package com.emc.prometheus.parser.parse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.parse.match.Match;
import com.emc.prometheus.parser.parse.section.SectionManager;
import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.emc.prometheus.parser.pojo.ParsedLogs;
import com.google.common.io.Files;

@Component
public class Parser {

	private final static Pattern capture = Pattern
			.compile("([^\\r\\n\\x00]++)[\\r\\n]*");

	public void parse(LogInfo logInfo, Entry<File, LOG_FILE_TYPE> logFileEntry)
			throws IOException {

		setParsedLog(logInfo, logFileEntry.getValue());

		Match root = SectionManager.getRootSection(logInfo.getParsedLogs(),
				logFileEntry.getValue());
		// get log file content,it is a string which contains whole log file
		String logContent = getLogContent(logFileEntry.getKey());
		// parse log
		parseLog(root, logContent);
	}

	private void setParsedLog(LogInfo logInfo, LOG_FILE_TYPE logFileType) {

		ParsedLogs parsedLogs = new ParsedLogs();
		HashMap<LOG_TYPE, List<String>> parsedLogMap = new HashMap<LOG_TYPE, List<String>>();
		switch (logFileType) {
		case ASUP:
			parsedLogMap.put(LOG_TYPE.MESSAGES, new ArrayList<String>());
			parsedLogMap.put(LOG_TYPE.KERN_INFO, new ArrayList<String>());
			parsedLogMap.put(LOG_TYPE.KERN_ERROR, new ArrayList<String>());
			parsedLogMap.put(LOG_TYPE.BIOS, new ArrayList<String>());
			parsedLogMap.put(LOG_TYPE.VTL_INFO, new ArrayList<String>());
			//Generated Date
			Map<LOG_TYPE, List<String>> generatedDateMap = new HashMap<LOG_TYPE, List<String>>();
			generatedDateMap.put(LOG_TYPE.MESSAGES, new ArrayList<String>());
			generatedDateMap.put(LOG_TYPE.KERN_INFO, new ArrayList<String>());
			generatedDateMap.put(LOG_TYPE.KERN_ERROR, new ArrayList<String>());
			generatedDateMap.put(LOG_TYPE.BIOS, new ArrayList<String>());
			generatedDateMap.put(LOG_TYPE.VTL_INFO, new ArrayList<String>());
			parsedLogs.setGeneratedDateMap(generatedDateMap);
			break;
		case MESSAGES_ENGINEERING:
			parsedLogMap.put(LOG_TYPE.MESSAGES, new ArrayList<String>());
			break;
		case KERN_INFO:
			parsedLogMap.put(LOG_TYPE.KERN_INFO, new ArrayList<String>());
			break;
		case KERN_ERROR:
			parsedLogMap.put(LOG_TYPE.KERN_ERROR, new ArrayList<String>());
			break;
		case BIOS:
			parsedLogMap.put(LOG_TYPE.BIOS, new ArrayList<String>());
			break;
		case VTL_INFO:
			parsedLogMap.put(LOG_TYPE.VTL_INFO, new ArrayList<String>());
			break;
		default:
			break;
		}
		parsedLogs.setParsedLogMap(parsedLogMap);

		logInfo.setParsedLogs(parsedLogs);

	}

	private void parseLog(Match root, String logContent) {
		Matcher m = capture.matcher(logContent);
		while (m.find()) {
			// match every no blank line
			root.match(m.group(1));
		}
	}

	private String getLogContent(File logFile) throws IOException {
		// get log file content,maybe it's a 500M+ string
		String logContent = Files.toString(logFile, StandardCharsets.UTF_8);
		return logContent;
	}

}
