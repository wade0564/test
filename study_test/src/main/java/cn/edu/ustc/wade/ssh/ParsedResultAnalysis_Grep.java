package cn.edu.ustc.wade.ssh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import cn.edu.ustc.common.cifs.SSHExecutor;

import com.google.common.io.Files;

/** 
 * @author wade 
 * @version Dec 4, 2014 2:21:47 PM 
 */
public class ParsedResultAnalysis_Grep {
	
	static SimpleDateFormat sdf =new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
	static SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static List<String> asupids = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		
		Runtime runtime = Runtime.getRuntime();
		
		
		String [] cmds ={
//						 "grep -P '^(\\d+\\|).*\\n(?!\\1)\\d+.*' /data2/dev/LogParser/storage/kern.info/80537105/80537105-80585272",
//						 "grep -P '^(\\d+\\|).*\\n(?!\\1)\\d+.*' /data2/dev/LogParser/storage/kern.info/80537105/80633435-80678627",
						 "grep -P '^(\\d+\\|).*\\n(?!\\1)\\d+.*' /data2/dev/debug/storage/messages/23317670/*"
						 };
		Process process = runtime.exec(cmds);

		
		InputStream is = process.getInputStream();
		
		OutputStream os = new FileOutputStream(new File("/data2/dev/debug/tmp/messages_adjacent"));
		
		byte[] buffer = new byte[1024*1024];
		
		int n =-1;
		while((n=is.read(buffer))!=-1){
			os.write(buffer, 0, n);
		}
		is.close();
		os.close();
	}
	
}
