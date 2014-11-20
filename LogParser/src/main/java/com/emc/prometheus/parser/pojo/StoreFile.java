package com.emc.prometheus.parser.pojo;

import java.io.File;

/** 
 * @author wade 
 * @version Nov 18, 2014 10:31:40 AM 
 */
public class StoreFile {
	
	File storeFile;
	
	Integer content;
	
	Long lastPos;//last line position
	
	Long currentPos;
	
	public StoreFile(File storeFile) {
		super();
		this.storeFile = storeFile;
	}
	
	public StoreFile(File storeFile, Integer content, Long lastPos, Long currentPos) {
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

	public Integer getContent() {
		return content;
	}

	public void setContent(Integer content) {
		this.content = content;
	}

	public Long getLastPos() {
		return lastPos;
	}

	public void setLastPos(Long lastPos) {
		this.lastPos = lastPos;
	}

	public Long getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(Long currentPos) {
		this.currentPos = currentPos;
	}
	
	
	

}
