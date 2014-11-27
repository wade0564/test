package com.emc.prometheus.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.google.common.io.Files;

/** 
 * @author wade 
 * @version Nov 25, 2014 1:59:54 PM 
 */
public class CleanJob {
	
	private String inMemoryDBdir = "";
	private String storageDir = "";
	
	
	
	@Test
	public void cleanAll() throws IOException{
		cleanInMemoryDB();
		deleteStorageFile();
	}
	

	@Before
	public void init() throws IOException {
		inMemoryDBdir = PropertiesLoaderUtils.loadAllProperties("logParser.properties").getProperty("LOG.PARSER.IN_MEMORY_DB.EVN_PATH");
		storageDir = PropertiesLoaderUtils.loadAllProperties("logParser.properties").getProperty("LOGFILE.STORAGE.ROOT");
		
	}
	
	@Test
	public void  cleanInMemoryDB() throws IOException{
		Files.deleteDirectoryContents(new File(inMemoryDBdir));
	}
	
	@Test
	public void deleteStorageFile() throws IOException {
		File storage = new File(storageDir);

//		java.nio.file.Files.deleteIfExists(Paths.get(storageDir));
		File[] listFiles = storage.listFiles();
		for (File child : listFiles) {
		FileUtils.forceDelete(child);
//			Files.deleteRecursively(child);
	}
	}
}
