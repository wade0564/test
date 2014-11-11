package wade.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest {
	
	  public static void main(String[] args) {
//		  String str3 = "aabbcccaaaaaeeeaaaaaaaaagggga";  
//		  Pattern p3 = Pattern.compile("a+.*");  
//		  Matcher m3 = p3.matcher(str3);  
//		  boolean bo4 = m3.matches();  
//		  System.out.println(bo4);
		  
		  
//		  String md5 ="a6bdf045124d6199bfeeaba0a2c182df  /auto/support/uploads/morked/ddr/bundle/upload.26-support-bundle.tar.gz";
//		  
//		  Pattern p = Pattern.compile("^(\\w+).*");
//		  
//		  Matcher matcher = p.matcher(md5);
//		  
//		  if(matcher.find()){
//			  System.out.println(matcher.group(1));
//		  }
		  
		  
		  Pattern p = Pattern.compile("Serial number:\\s*(.*)");
		  Matcher matcher = p.matcher("Serial number: AUDVZMAKURAFHU");
		  String localSn ="";
		  if(matcher.find()){
			  localSn=  matcher.group(1);
		  }
		  System.out.println(localSn);
		  
//		  String location ="/auto/support/uploads/jmmoyer@geisinger.edu/ddr/bundle/upload.4-bundle-2014-04-04.tar.gz";
//		  Pattern p =Pattern.compile("([^/]*$)");
//		  Matcher m =p.matcher(location);
//			
//			String filename ="";
//			if(m.find()){
//				filename=m.group(1);
//				System.out.println(filename);
//			}
//			
		  
//		  System.out.println("ab++c".matches("^[A-Za-z0-9]++$"));
		  
//		  Pattern p = Pattern.compile("([^\\r\\n]++)[\\r\\n]++");  
		  
		  
		     //create a Matcher and use the Matcher.group(int) method  
//		     String candidateString = "My name is Bond. James Bond.";  
//		     //create a helpful index for the sake of output  
//		     Matcher matcher = p.matcher("");  
//		     System.out.println(matcher.find());
//		     //Find group number 0 of the first find  
//		      matcher.find();  
//		      String group_0 = matcher.group(0);  
//		      String group_1 = matcher.group(1);  
//		      System.out.println("Group 0 " + group_0);  
//		      System.out.println("Group 1 " + group_1);  
//		      System.out.println(candidateString);  
//		  
//		     //Find group number 1 of the second find  
//		      matcher.find();  
//		      group_0 = matcher.group(0);  
//		      group_1 = matcher.group(1);  
//		      System.out.println("Group 0 " + group_0);  
//		      System.out.println("Group 1 " + group_1);  
//		      System.out.println(candidateString); 
	}

}
