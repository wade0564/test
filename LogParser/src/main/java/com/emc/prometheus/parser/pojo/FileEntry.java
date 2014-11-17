package com.emc.prometheus.parser.pojo;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;

/** 
 * @author wade 
 * @version Nov 13, 2014 4:03:52 PM 
 */
public class FileEntry  extends SimpleEntry<File, LOG_FILE_TYPE>{
	
	public FileEntry(Entry<? extends File, ? extends LOG_FILE_TYPE> entry) {
		super(entry);
		// TODO Auto-generated constructor stub
	}
	File file ;
	LOG_FILE_TYPE logFileType;


}
