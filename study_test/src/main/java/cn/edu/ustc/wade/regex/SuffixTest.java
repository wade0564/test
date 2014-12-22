package cn.edu.ustc.wade.regex;

import java.math.MathContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * @author wade 
 * @version Dec 3, 2014 9:56:48 AM 
 */
public class SuffixTest {
	
	public static void main(String[] args) {
		
		
		String path  = "c:/tmp/unzip/ddr/var/support/autosupport";
		
		Pattern p = Pattern.compile("[^/]*$");
		
		Matcher matcher = p.matcher(path);
		
		String suffix  = null;
		if(matcher.find()){
			suffix = matcher.group();
			System.out.println(suffix);
		}
		
		String test ="/t/autosupport.1";
		System.out.println(test+":"+test.endsWith(suffix));
		String test1 ="/t/autosupport";
		System.out.println(test1 +":"+ test1.endsWith(suffix));
	}

}
