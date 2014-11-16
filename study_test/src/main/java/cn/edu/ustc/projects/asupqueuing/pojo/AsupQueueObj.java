package cn.edu.ustc.projects.asupqueuing.pojo;

import java.math.BigInteger;
import java.sql.Timestamp;


public class AsupQueueObj {

	private Timestamp timestamp;

	private String filehandle;

	private String parse_status;

	private BigInteger id;
	
	private BigInteger asupid;
	
	private String fileSource;
	
	private String sn;
	
	private String  model;
	
	private String fileType;
	
	private String ddosVer;
	

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getFilehandle() {
		return filehandle;
	}

	public void setFilehandle(String filehandle) {
		this.filehandle = filehandle;
	}

	public String getParse_status() {
		return parse_status;
	}

	public void setParse_status(String parse_status) {
		this.parse_status = parse_status;
	}
	

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getAsupid() {
		return asupid;
	}

	public void setAsupid(BigInteger asupid) {
		this.asupid = asupid;
	}

	public String getFileSource() {
		return fileSource;
	}

	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDdosVer() {
		return ddosVer;
	}

	public void setDdosVer(String ddosVer) {
		this.ddosVer = ddosVer;
	}

	public void setParse_status(ASUPQUEUEOBJ_STATUS status) {
		this.parse_status = status.toString();
	}

	@Override
	public String toString() {
		return "AsupQueueObj [timestamp=" + timestamp + ", filehandle="
				+ filehandle + ", parse_status=" + parse_status + ", id=" + id
				+ ", asupid=" + asupid + ", fileSource=" + fileSource + ", sn="
				+ sn + ", model=" + model + ", fileType=" + fileType
				+ ", ddosVer=" + ddosVer + "]";
	}
	
	

}
