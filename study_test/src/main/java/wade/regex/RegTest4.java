package wade.regex;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest4 {
	
	public static void main(String[] args) throws IOException {
		

		
		String str="atsg240.lss.emc.com                      time=55.25 ms\natsg200                                  time=81.54 ms\n\n\n---- ping statistics ----";
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(str.getBytes())));
		
		String s ="";
		
		Pattern p =Pattern.compile("^(.+)\\stime.+$");
		while((s = br.readLine())!= null){
			Matcher matcher = p.matcher(s);
			if(matcher.find()){
				System.out.println(s );
				String node = matcher.group(1);
				System.out.println(node);
			}
		}
	}
	
	
}
