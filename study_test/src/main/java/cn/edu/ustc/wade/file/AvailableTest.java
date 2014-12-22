package cn.edu.ustc.wade.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/** 
 * @author wade 
 * @version Dec 9, 2014 2:18:36 PM 
 */
public class AvailableTest {
	
	
	public static void main(String[] args) throws IOException {
		
		
		GZIPInputStream compressedIs = new GZIPInputStream(new FileInputStream(new File("C:/tmp/asup/internal/2014/10/29/2014-11-10_21-08-20.23893.gz")));
		FileInputStream unCompressedIs = new FileInputStream(new File("C:/tmp/asup/internal/2014/10/29/2014-11-10_21-08-20.23893"));
		
		System.out.println("Compressed : "+ compressedIs.available());

		System.out.println("unCompressed : "+ unCompressedIs.available());
		
		
	}
	
	

}
