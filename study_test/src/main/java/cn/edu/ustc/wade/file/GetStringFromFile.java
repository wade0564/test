package cn.edu.ustc.wade.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.GZIPInputStream;

/** 
 * @author wade 
 * @version Dec 9, 2014 9:45:23 AM 
 */
public class GetStringFromFile {
	
	
	String fileName;
	
	File file;
	
	public String getSub() throws IOException {

		//maybe appear message.engering.gz
		if (fileName.endsWith("gz")) {
			GZIPInputStream gzipIs = new GZIPInputStream(new FileInputStream(
					file));
			//use \\GG as Delimiter
			java.util.Scanner scanner = new java.util.Scanner(gzipIs)
					.useDelimiter("\\GG");
			String t = scanner.hasNext() ? scanner.next() : "";
			gzipIs.close();
			return t;
		} else {
			FileInputStream fis = new FileInputStream(file);
			FileChannel fChannel = fis.getChannel();
			byte[] barray = new byte[(int) file.length()];
			ByteBuffer bb = ByteBuffer.wrap(barray);
			fChannel.read(bb);
			String s = new String(barray);
			fis.close();
			fChannel.close();
			return s;
		}

	}


}
