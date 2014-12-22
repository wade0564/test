package cn.edu.ustc.wade.regex;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * @author wade 
 * @version Dec 18, 2014 2:46:56 PM 
 */
public class QuickTest {
	
	
	public static void main(String[] args) throws IOException {
		String s ="/auto/cores/c2122936/support-bundle_2014_01_15ddr/var/support/autosupport.8";
		Pattern p = Pattern.compile("ddr/var/support/.+$");
		Matcher matcher = p.matcher(s);
		
		if(matcher.find()){
			System.out.println(matcher.group());
			File f = new File("c:",matcher.group());
			
			System.out.println(f.getAbsolutePath());
			
		}
	}
}
