package cn.edu.ustc.wade.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/** 
 * @author wade 
 * @version Dec 2, 2014 11:20:34 AM 
 */
public class CreateFileTest {
	
	
	public static void main(String[] args) throws IOException {
		
		List<String> lines = Files.readLines(new File("C:/Users/zhangw4/notes/info/createFiles"), Charsets.UTF_8);
		
		String messages = "==========  MESSAGES  ==========\n GENERATED: 2012-07-20 06:33:42 EDT\n Jul 19 22:10:%s ddr990ts01 sysmon: NOTICE: Cannot retrieve number of snapshots from sml_snapshot_list";
		
		File asup  = new File("C:/logs/autosupport.1");
		File message  = new File("C:/logs/messages.engineering.test");
		
		int i = 10;
		
		for (String line : lines) {
			
			System.out.println(line);
			
			File f = new File(line.trim());
			Files.createParentDirs(f);
			f.createNewFile();
			if(line.contains("autosupport") )
			{
				Files.copy(asup, f);
			}
			
			if(line.contains("messages.engineering") )
			{
				Files.copy(message, f);
			}
			
			
		}
		
		
		
	}
	

}
