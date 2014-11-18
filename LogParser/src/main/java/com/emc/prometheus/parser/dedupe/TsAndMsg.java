package com.emc.prometheus.parser.dedupe;



/**
 * @author wade
 * @version Nov 12, 2014 4:13:20 PM
 */
public class TsAndMsg {

	Long ts;

	String msg;

	boolean toBeWritten;
	
	public TsAndMsg(Long ts, String msg, boolean toBeWritten){
		this.ts = ts;
		this.msg = msg;
		this.toBeWritten = toBeWritten;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	

	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	public boolean isToBeWritten() {
		return toBeWritten;
	}

	public boolean toBeWritten() {
		return toBeWritten;
	}

	public void setToBeWritten(boolean toBeWritten) {
		this.toBeWritten = toBeWritten;
	}

}
