package wade;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

public class FileScannerTest {
	public static void main(String[] args) throws Exception {
		File file =new File("C:\\Users\\zhangw4\\Desktop\\SUB\\messages.engineering.7.gz");
		
		GZIPInputStream gzipIs= new GZIPInputStream(new FileInputStream(file));
		java.util.Scanner s = new java.util.Scanner(new FileInputStream(file)).useDelimiter("\\GG");
	    String t= s.hasNext() ? s.next() : "";
	    System.out.println( t);
	    
		
	}

}
