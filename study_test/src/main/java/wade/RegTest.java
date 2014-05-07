package wade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest {
	
	  public static void main(String[] args) {
//		  String str3 = "aabbcccaaaaaeeeaaaaaaaaagggga";  
//		  Pattern p3 = Pattern.compile("a+.*");  
//		  Matcher m3 = p3.matcher(str3);  
//		  boolean bo4 = m3.matches();  
//		  System.out.println(bo4);
		  
		  
		  
//		  System.out.println("ab++c".matches("^[A-Za-z0-9]++$"));
		  
		  Pattern p = Pattern.compile("B(ond)");  
		  
		     //create a Matcher and use the Matcher.group(int) method  
		     String candidateString = "My name is Bond. James Bond.";  
		     //create a helpful index for the sake of output  
		     Matcher matcher = p.matcher(candidateString);  
		     //Find group number 0 of the first find  
		      matcher.find();  
		      String group_0 = matcher.group(0);  
		      String group_1 = matcher.group(1);  
		      System.out.println("Group 0 " + group_0);  
		      System.out.println("Group 1 " + group_1);  
		      System.out.println(candidateString);  
		  
		     //Find group number 1 of the second find  
		      matcher.find();  
		      group_0 = matcher.group(0);  
		      group_1 = matcher.group(1);  
		      System.out.println("Group 0 " + group_0);  
		      System.out.println("Group 1 " + group_1);  
		      System.out.println(candidateString); 
	}

}
