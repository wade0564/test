package cn.edu.ustc.wade.ssh;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.edu.ustc.common.cifs.SSHExecutor;

import com.google.common.io.Files;

/** 
 * @author wade 
 * @version Dec 4, 2014 2:21:47 PM 
 */
public class ParsedResultAnalysis {
	
	static SimpleDateFormat sdf =new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
	static SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static List<String> asupids = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		
		SSHExecutor executor = new SSHExecutor();
		
		executor.setIp("10.25.192.33");
		executor.setPort(22);
		executor.setUsername("dev");
		executor.setPassword("pass4etl2");
		executor.setIsOutput(true);
		String [] cmds ={
//						 "grep -P '^(\\d+\\|).*\\n(?!\\1)\\d+.*' /data2/dev/LogParser/storage/kern.info/80537105/80537105-80585272",
//						 "grep -P '^(\\d+\\|).*\\n(?!\\1)\\d+.*' /data2/dev/LogParser/storage/kern.info/80537105/80633435-80678627",
						 "grep -P '^(\\d+\\|).*\\n(?!\\1)\\d+.*' /data2/dev/debug/storage/messages/23317670/*"
						 };
		executor.setCommands(cmds);
		executor.execute();
		Files.write(executor.getOutputResult().getBytes(), new File("C:/logs/output.log"));
	}
	
}
