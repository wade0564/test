package com.emc.prometheus.parser.pojo;

public enum LOG_FILE_TYPE {

	ASUP, SUB, KERN_INFO, KERN_ERROR, MESSAGES_ENGINEERING, VTL_INFO, BIOS, UNKONWN;

	public static LOG_FILE_TYPE getType(String filehandler) {

		LOG_FILE_TYPE type;

		if (filehandler.contains("autosupport")) {
			type = ASUP;
		} else if (filehandler.contains("kern.info")) {
			type = KERN_INFO;
		} else if (filehandler.contains("vtl.info")) {
			type = VTL_INFO;
		} else if (filehandler.contains("bios.txt")) {
			type = BIOS;
		} else {
			type = UNKONWN;
		}

		return type;

	}

}