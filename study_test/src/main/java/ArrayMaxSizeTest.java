import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;


/** 
 * @author wade 
 * @version Dec 9, 2014 4:16:20 PM 
 */
public class ArrayMaxSizeTest {
	
	static Logger log = Logger.getLogger("wade");
	public static void main(String[] args) throws IOException {
		
//		System.out.println(System.getProperty("file.encoding"));
		
		
//		String charsetName = Charset.defaultCharset().name();
		
		String largeLogContent = getLargeLogContent(new File("/data2/dev/LogParser/tmp/support-bundle.tar.gz"));
		
		System.out.println(largeLogContent);
		
//		test00();
		
		
//		test01();
		
		
//		test02(new File("/data2/dev/LogParser/tmp/ddr/var/log/debug/vtl.info.1"));
		
	}
	
	private static void test00() {
		
		byte [] bytes = new byte[1846258179];
		
		System.out.println(bytes.length);
		
	}

	public static void test02(File logFile) throws FileNotFoundException, IOException{

		
		
		StringBuilder strBuilder = new StringBuilder((int)logFile.length());
		//100M buffer
		int len =100 *1024 *1024;
		byte[] buffer = new byte[len];
		int bytesRead = 0;
		
		
		//maybe appear message.engering.gz
		if (logFile.getName().endsWith("gz")) {
			GZIPInputStream gzipIs = null;
			try {
				gzipIs = new GZIPInputStream(new FileInputStream(logFile));
				while ((bytesRead = gzipIs.read(buffer, 0, len)) != -1) {
					strBuilder.append(new String(buffer, 0, bytesRead,"ISO-8859-1"));
				}
			} finally {
				gzipIs.close();
			}
		} else {
			BufferedInputStream bufferedInputStream =  null;
			try{
				bufferedInputStream = new BufferedInputStream(new FileInputStream(logFile));;
				while((bytesRead=bufferedInputStream.read(buffer,0,len))!=-1){
					strBuilder.append(new String (buffer,0,bytesRead,"ISO-8859-1"));
				}
			}finally{
				bufferedInputStream.close();
			}
		}
			
			System.out.println("Writing ...");
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("/data2/dev/LogParser/tmp/vtl.info.1")));
//			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/data2/dev/LogParser/tmp/vtl.info.1")));
//			char[] charArray = new char[strBuilder.length()];
//			strBuilder.getChars(0, strBuilder.length()-1, charArray, 0);
//			System.out.println("The big string len :"+charArray.length);
//			bw.write(charArray);
			String str = strBuilder.toString();
			strBuilder = null;
			byte[] bytes = str.getBytes("ISO-8859-1");
			str = null;
			System.out.println(bytes.length);
			bos.write(bytes);
			bos.close();
//			bw.close();
			System.out.println("Writing over !!!");
	
	}

	private static void test01() throws FileNotFoundException, IOException {
		File file = new File("/data2/dev/LogParser/tmp/ddr/var/log/debug/vtl.info.1");
		
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		
		System.out.println("file length :"+file.length());
		System.out.println("file inputstram available :"+bis.available());
		
		byte []  buffer = new byte[bis.available()];
		
		String str = null;
		System.out.println("Reading ...");
		int readBytes = -1;
		while((readBytes=bis.read(buffer))!=-1){
			str = new String(buffer,0,readBytes);
		}

		bis.close();
		System.out.println("Reading over !!!");
		buffer= null;
		
		System.out.println("Writing ...");
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("/data2/dev/LogParser/tmp/vtl.info.1")));
		
		
		bos.write(str.getBytes());
		bos.close();
		
		System.out.println("Writing over !!!");
	}
	
	
private static String getLargeLogContent(File logFile) throws FileNotFoundException, IOException {
		
		long logFilelen = 0L;
		if(logFile.getName().endsWith("gz")){
			
			Runtime runtime = Runtime.getRuntime();
			Process exec = runtime.exec(String.format("gzip -l %s", logFile.getAbsolutePath()));
			System.out.println("Execute "+String.format("gzip -l %s", logFile.getAbsolutePath()));
			BufferedReader  br= null;
			try {
				br = new BufferedReader(new InputStreamReader(
						exec.getInputStream()));
				String s = null;
				while ((s = br.readLine()) != null) {
					System.out.println(s);
					Pattern p = Pattern.compile("\\s*\\d+\\s*(\\d+).*");
					Matcher matcher = p.matcher(s);
					if (matcher.find()) {
						System.out.println("group :"+ matcher.group(1));
						logFilelen = Long.parseLong(matcher.group(1));
					}
				} 
			}finally{
				br.close();
			}
		}else{
			logFilelen = logFile.length();
		}
		
		if(logFilelen>Integer.MAX_VALUE){
			System.out.println("The log file {} is too large to read !!!"+logFile.getAbsolutePath());
			return "";
		}
		
		System.out.println(logFilelen);
		StringBuilder strBuilder = new StringBuilder((int)logFilelen);
		//100M buffer
		int len =100 *1024 *1024;
		byte[] buffer = new byte[len];
		int bytesRead = 0;
		
//		log.info("The large file {} being read ...",logFile.getAbsolutePath());
		
		//maybe appear message.engering.gz
		if (logFile.getName().endsWith("gz")) {
			GZIPInputStream gzipIs = null;
			try {
				gzipIs = new GZIPInputStream(new FileInputStream(logFile));
				while ((bytesRead = gzipIs.read(buffer, 0, len)) != -1) {
					strBuilder.append(new String(buffer, 0, bytesRead));
				}
			} finally {
				gzipIs.close();
			}
		} else {
			BufferedInputStream bufferedInputStream =  null;
			try{
				bufferedInputStream = new BufferedInputStream(new FileInputStream(logFile));;
				while((bytesRead=bufferedInputStream.read(buffer,0,len))!=-1){
					strBuilder.append(new String (buffer,0,bytesRead));
				}
			}finally{
				bufferedInputStream.close();
			}
		}
			return strBuilder.toString();
	}

}
