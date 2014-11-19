package com.emc.prometheus.parser.pojo;

import java.io.File;

/** 
 * @author wade 
 * @version Nov 18, 2014 10:31:40 AM 
 */
public class StoreFile {
	
	File storeFile;
	
	Integer content;
	
	Integer pos;//last line position
	
	public StoreFile(File storeFile) {
		super();
		this.storeFile = storeFile;
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

	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}
	
	
	

}
