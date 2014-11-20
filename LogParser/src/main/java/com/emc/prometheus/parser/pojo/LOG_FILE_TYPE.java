package com.emc.prometheus.parser.pojo;

import java.util.Arrays;
import java.util.List;

public enum LOG_FILE_TYPE {

	ASUP("autosupport"), SUB("tar.gz||tgz"), KERN_INFO("kern.info"), KERN_ERROR("vtl.info"), MESSAGES_ENGINEERING("messages.engineering"), VTL_INFO("vtl.info"), BIOS("bios.txt"), UNKONWN(".*");


	private String fileNamePattern;
	
	private LOG_FILE_TYPE(String fileNamePattern){
		this.fileNamePattern=fileNamePattern;
	}
	
	public static List<LOG_FILE_TYPE> getFileTypeInSub(){
		return Arrays.asList(ASUP,KERN_INFO,KERN_ERROR,MESSAGES_ENGINEERING,VTL_INFO,BIOS);
	}
	
	private String getFilenamePattern(){
		return fileNamePattern;
	}
	
	public static LOG_FILE_TYPE getType(String filehandler) {

		List<LOG_FILE_TYPE> fileTypeInSub = getFileTypeInSub();
		
		for (LOG_FILE_TYPE logFileType : fileTypeInSub) {
			if(filehandler.contains(logFileType.getFilenamePattern())){
				return logFileType;
			}
		}
		return UNKONWN;
	}

}