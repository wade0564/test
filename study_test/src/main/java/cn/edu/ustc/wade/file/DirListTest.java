package cn.edu.ustc.wade.file;

import java.io.File;

/** 
 * @author wade 
 * @version Nov 24, 2014 11:29:26 AM 
 */
public class DirListTest {
	
	
	public static void main(String[] args) {
		
		File storage =  new File("C:/tmp/storage");
		
		String[] list = storage.list();
		
		for (String string : list) {
			
			System.out.println(string);
			
		}
		
		
	}

}
