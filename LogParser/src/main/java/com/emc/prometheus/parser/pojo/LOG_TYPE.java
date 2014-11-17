package com.emc.prometheus.parser.pojo;

public enum LOG_TYPE {
	 KERN_INFO, KERN_ERROR, MESSAGES;

	public String getValue() {
		return this.toString().toLowerCase().replace("_", ".");
	}

}
