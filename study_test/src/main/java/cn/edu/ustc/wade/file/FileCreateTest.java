package cn.edu.ustc.wade.file;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.io.Files;

/** 
 * @author wade 
 * @version Nov 19, 2014 1:39:33 PM 
 */
public class FileCreateTest {
	
	public static final List<String> asupSubDirectory = Arrays.asList("emails/", "alerts/", "internal/", "internal/", "internal/emails/", "internal/alerts/");

	static String ASUP_LOG_ROOT_DIRECTORY ="c:/tmp/asup" ;
	static String SUB_LOG_ROOT_DIRECTORY ="c:/tmp/sub";
	
public static void main(String[] args) throws IOException {
	
	String [] asupFiles = {"2014/10/29/2014-10-29_00-48-52.23461", "2014/10/29/2014-10-29_00-46-04.21952", "2014/10/29/2014-10-29_00-48-54.23489", "2014/10/29/2014-10-29_00-47-04.22170", "2014/10/29/2014-10-29_00-48-52.23444", "2014/10/29/2014-10-29_00-48-53.23464"};
		
		//asup
		for (int i =0 ;i<asupSubDirectory.size();i++) {
			String subDir = asupSubDirectory.get(i);
			
			File dir = new File(ASUP_LOG_ROOT_DIRECTORY,subDir);
			File asup =  new File(dir, asupFiles[i]);
			
			Files.createParentDirs(asup);
			System.out.println(asup.getAbsolutePath());
			asup.createNewFile();
//			file.mkdirs();
//			Files.createParentDirs(file);
			
		}
	}

}
