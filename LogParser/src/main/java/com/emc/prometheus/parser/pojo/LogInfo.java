package com.emc.prometheus.parser.pojo;

import com.emc.prometheus.parser.parse.ParsedLogs;

public class LogInfo {

	Long asupId;

	String sn;

	Long epoch;

	String chassis_sn;

	LOG_FILE_TYPE type;

	String file_handler;

	// store the parsed log entries
	ParsedLogs parsedLogs;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public ParsedLogs getParsedLogs() {
		return parsedLogs;
	}

	public void setParsedLogs(ParsedLogs parsedLogs) {
		this.parsedLogs = parsedLogs;
	}

	public Long getAsupId() {
		return asupId;
	}

	public void setAsupId(Long asupId) {
		this.asupId = asupId;
	}

	public LOG_FILE_TYPE getType() {
		return type;
	}

	public void setType(LOG_FILE_TYPE type) {
		this.type = type;
	}

	public String getFile_handler() {
		return file_handler;
	}

	public void setFile_handler(String file_handler) {
		this.file_handler = file_handler;
	}

	public Long getEpoch() {
		return epoch;
	}

	public void setEpoch(Long epoch) {
		this.epoch = epoch;
	}

	public String getChassis_sn() {
		return chassis_sn;
	}

	public void setChassis_sn(String chassis_sn) {
		this.chassis_sn = chassis_sn;
	}

	@Override
	public String toString() {
		return "LogInfo [asupId=" + asupId + ", sn=" + sn + ", epoch=" + epoch
				+ ", chassis_sn=" + chassis_sn + ", type=" + type
				+ ", file_handler=" + file_handler + ", parsedLogs="
				+ parsedLogs + "]";
	}

}
