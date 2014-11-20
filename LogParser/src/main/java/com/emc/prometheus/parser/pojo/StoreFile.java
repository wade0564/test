package com.emc.prometheus.parser.pojo;

import java.io.File;

/** 
 * @author wade 
 * @version Nov 18, 2014 10:31:40 AM 
 */
public class StoreFile {
	
	File storeFile;
	
	int content;
	
	long lastPos;//last line position
	
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
		return storeFile;
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

}
