package com.emc.prometheus.parser.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.google.common.collect.ImmutableList;

/** 
* @author wade 
* @version Nov 12, 2014 10:10:05 PM 
*/

public class FileUtils {
	
	
	
	final static Logger log = LoggerFactory.getLogger(FileUtils.class);
	final static String GZ = ".gz";
	final static String SUBFILE_SUFFIX="tar.gz|tgz";
	public static final ImmutableList<String> asupSubDirectory = ImmutableList.of("emails/", "alerts/", "internal/", "internal/", "internal/emails/", "internal/alerts/");

	static String ASUP_LOG_ROOT_DIRECTORY ;
	static String SUB_LOG_ROOT_DIRECTORY ;
	static String SUB_LOG_UNZIP_DIRECTORY ;
	
	static{
		try {
			ASUP_LOG_ROOT_DIRECTORY = PropertiesLoaderUtils.loadAllProperties("logParser.properties").getProperty("LOG.ASUP.ROOT.DIRECTORY");
			SUB_LOG_ROOT_DIRECTORY = PropertiesLoaderUtils.loadAllProperties("logParser.properties").getProperty("LOG.SUB.ROOT.DIRECTORY");
			SUB_LOG_UNZIP_DIRECTORY =PropertiesLoaderUtils.loadAllProperties("logParser.properties").getProperty("LOG.SUB.UNZIP.DIRECTORY");
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			log.error("Can't find the configuration files {}","logParser.properties");
		}
	}
	

	public static Map<File, LOG_FILE_TYPE> getAsupLogFileMap(LogInfo logInfo) {
		
		Map<File, LOG_FILE_TYPE>  logFileMap = new LinkedHashMap<File, LOG_FILE_TYPE>();
		
		File asupFile = getAsupFile(logInfo.getFile_handler());
		
		if(asupFile!=null){
			logFileMap.put(asupFile, LOG_FILE_TYPE.ASUP);
		}
		
		return logFileMap;
	}


	private static File getAsupFile(String file_handler) {
		File asup = null;
		for (String subDir : asupSubDirectory) {
			File dir = new File(ASUP_LOG_ROOT_DIRECTORY, subDir);
			asup = new File(dir, file_handler + GZ);
			log.debug("asup file location : {}",asup);
			if (asup.exists()) {
				break;
			} else {
				asup = null;
			}
		}
		return asup;
	}

	public static Map<File, LOG_FILE_TYPE> getSubLogFileMap(LogInfo subInfo, List<LogInfo> subContentLogInfos) {
		Map<File, LOG_FILE_TYPE>  logFileMap = new LinkedHashMap<File, LOG_FILE_TYPE>();
		
		String subFileLocation = subInfo.getFile_handler();
		
		log.debug("SUB file location : {}",subFileLocation);
		//unzip sub bundle
		try {
			unpackSub(subFileLocation, SUB_LOG_UNZIP_DIRECTORY);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return logFileMap;
		}
		//get subContentLogInfo
		
		String orignSubLocation=subFileLocation.replaceAll(SUBFILE_SUFFIX, "");
		
		for (LogInfo subContentLogInfo : subContentLogInfos) {
			
			logFileMap.put(new File(subContentLogInfo.getFile_handler().replace(orignSubLocation, SUB_LOG_UNZIP_DIRECTORY)), subContentLogInfo.getType());
			log.debug("unzip subcontent file location :{}",subContentLogInfo.getFile_handler().replace(orignSubLocation, SUB_LOG_UNZIP_DIRECTORY));
		}
		
		return logFileMap;
	}
	
	private static void unpackSub(String subFile, String destinatedPath) throws Exception {
		
		if(true){return ;}

		try {
			Runtime rt = Runtime.getRuntime();

			Process p1 = rt.exec(String.format("tar zxvf %s -C %s",
					subFile, destinatedPath));

			log.info("unpacking SUB : {} ",subFile);
			if (p1.waitFor() != 0) {
				log.error("{} invalid compressed data",subFile);
				throw new Exception();
			}
		} catch (Exception e) {
			log.info("Exception while unpacking SUB : {} ",subFile);
			log.error(e.getMessage(),e);
			throw new Exception("decompression error" ,e);
		}
	}

} 
