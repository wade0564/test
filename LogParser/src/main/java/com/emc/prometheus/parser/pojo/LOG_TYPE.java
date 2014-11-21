package com.emc.prometheus.parser.pojo;

public enum LOG_TYPE{
	 KERN_INFO, KERN_ERROR, MESSAGES,BIOS,VTL_INFO;

	public String getValue() {
		return this.toString().toLowerCase().replace("_", ".");
	}

}
