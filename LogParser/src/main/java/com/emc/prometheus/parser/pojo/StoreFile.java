package com.emc.prometheus.parser.pojo;

import java.io.File;
import java.io.Serializable;

/** 
 * @author wade 
 * @version Nov 18, 2014 10:31:40 AM 
 */
public class StoreFile  implements Serializable{
	
	private static final long serialVersionUID = 8886060538044581896L;
	
	String location;
	
	int symptomDataCount; // asupid amount symptomDataCount
	
	long lastPos;//last offset position
	
	long currentPos;
	
	public StoreFile(String location) {
		super();
		this.location = location;
	}
	
	public StoreFile(String location, int symptomDataCount, long lastPos, long currentPos) {
		super();
		this.location = location;
		this.symptomDataCount = symptomDataCount;
		this.lastPos = lastPos;
		this.currentPos = currentPos;
	}

	public File getStoreFile() {
		return new File(location);
	}

	public int getSymptomDataCount() {
		return symptomDataCount;
	}

	public void setSymptomDataCount(int symptomDataCount) {
		this.symptomDataCount = symptomDataCount;
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
		return "StoreFile [storeFile=" + location + ", content=" + symptomDataCount
				+ ", lastPos=" + lastPos + ", currentPos=" + currentPos + "]";
	}

}
