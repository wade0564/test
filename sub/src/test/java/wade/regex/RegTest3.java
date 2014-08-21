package wade.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest3 {
	
	public static void main(String[] args) {
		

//		
//		Pattern p =Pattern.compile("[^\\d]*(\\d*)(.gz)*$");
//		
//		Matcher m1 =p.matcher("messages.engernig.1");
//		Matcher m2 =p.matcher("messages.engernig.2.gz");
//		
//		if(m1.find()&& m2.find()){
//			String s1=m1.group(1);
//			System.out.println(s1);
//			String s2=m2.group(1);
//			System.out.println(s2);
//	
//		}
//		
		
		String md5Result="7f0d33f518d0740364cb565722a21436  /auto/support/uploads/greshr/ddr/bundle/upload.42-bundle-2014-04-10.tar.gz";
		
		String md5 = "";
		Pattern p2 = Pattern.compile("^\\w+");
		Matcher matcher = p2.matcher(md5Result);
		if (matcher.find()) {
			md5 = matcher.group();
		}
		

	}
	
	
}
