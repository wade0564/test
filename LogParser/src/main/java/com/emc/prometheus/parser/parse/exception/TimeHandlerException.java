package com.emc.prometheus.parser.parse.exception;

import com.emc.prometheus.parser.pojo.LogInfo;

/**
 * @author wade
 * @version Nov 24, 2014 3:42:30 PM
 */
public class TimeHandlerException extends Exception {

	private static final long serialVersionUID = -6425673814966356181L;
	
	private LogInfo logInfo;

	public TimeHandlerException(String message, Exception e,LogInfo logInfo) {
		super(message, e);
		this.logInfo = logInfo;
	}

	public TimeHandlerException(String message) {
		super(message);
	}
	
	public LogInfo getLogInfo(){
		return logInfo;
	}
	
}
