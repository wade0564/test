package com.emc.prometheus.parser.pojo;

import java.io.File;
import java.io.Serializable;

/** 
 * @author wade 
 * @version Nov 18, 2014 10:31:40 AM 
 */
public class StoreFile  implements Serializable{
	
	private static final long serialVersionUID = 8886060538044581896L;

	File storeFile;
	
	String location ;
	
	int content; // asupid amount symptomDataCount
	
	long lastPos;//last offset position
	
	long currentPos;
	
	public StoreFile(File storeFile) {
		super();
		this.storeFile = storeFile;
	}
	
	public StoreFile(File storeFile, int content, long lastPos, long currentPos) {
		super();
		this.storeFile = storeFile;
		this.content = content;
		this.lastPos = lastPos;
		this.currentPos = currentPos;
	}

	public File getStoreFile() {
		return new File(location);
	}

	public void setStoreFile(File storeFile) {
		this.storeFile = storeFile;
	}

	public int getContent() {
		return content;
	}

	public void setContent(int content) {
		this.content = content;
	}

	public long getLastPos() {
		return lastPos;
	}

	public void setLastPos(long lastPos) {
		this.lastPos = lastPos;
	}

	public long getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(long currentPos) {
		this.currentPos = currentPos;
	}
	

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "StoreFile [storeFile=" + storeFile.getAbsolutePath() + ", content=" + content
				+ ", lastPos=" + lastPos + ", currentPos=" + currentPos + "]";
	}

}
