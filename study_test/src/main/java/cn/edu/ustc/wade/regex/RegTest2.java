package cn.edu.ustc.wade.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest2 {

	private static String loadavg = "load\\s++average:\\s++(-?[\\d\\.]++),\\s++(-?[\\d\\.]++),\\s++(-?[\\d\\.]++)";

	private static String loadavg2 = "load\\s++average:\\s++(-?[\\d\\.]++),\\s++(-?[\\d\\.]++)";

	public static final String uptime_key = "UPTIME=";

	public static final Pattern uptime_1 =

	Pattern.compile("^UPTIME=.*?up\\s*+(-?[\\d\\.]++)\\s*+days?,\\s*+(-?[\\d\\.]++):(-?[\\d\\.]++),\\s++(\\d++)\\s++users?,\\s++"
			+ loadavg);

	public static final Pattern uptime_2 =

	Pattern.compile("^UPTIME=.*?up\\s*+(-?[\\d\\.]++)\\s*+days?,\\s*+(-?[\\d\\.]++)\\s*+mins?,\\s++(\\d++)\\s++users?,\\s++"
			+ loadavg);

	public static final Pattern uptime_3 =

	Pattern.compile("^UPTIME=.*?up\\s*+(-?[\\d\\.]++)\\s*+mins?,\\s++(\\d++)\\s++users?,\\s++"
			+ loadavg);

	public static final Pattern uptime_4 =

	Pattern.compile("^UPTIME=.*?up\\s*+(-?[\\d\\.]++):(-?[\\d\\.]++),\\s++(\\d++)\\s++users?,\\s++"
			+ loadavg);

	public static final Pattern uptime_5 =

	Pattern.compile("^UPTIME=");

	public static final Pattern uptime_6 =

	Pattern.compile("^UPTIME=.*?up\\s*+(-?[\\d\\.]++)\\s*+days?,\\s*+(-?[\\d\\.]++):(-?[\\d\\.]++),\\s++(\\d++)\\s++users?,\\s++"
			+ loadavg2);

	public static final Pattern uptime_7 =

	Pattern.compile("^UPTIME=.*?up\\s*+(-?[\\d\\.]++)\\s*+days?,\\s*+(-?[\\d\\.]++)\\s*+mins?,\\s++(\\d++)\\s++users?,\\s++"
			+ loadavg2);

	public static final Pattern uptime_8 =

	Pattern.compile("^UPTIME=.*?up\\s*+(-?[\\d\\.]++)\\s*+mins?,\\s++(\\d++)\\s++users?,\\s++"
			+ loadavg2);

	public static final Pattern uptime_9 =

	Pattern.compile("^UPTIME=.*?up\\s*+(-?[\\d\\.]++):(-?[\\d\\.]++),\\s++(\\d++)\\s++users?,\\s++"
			+ loadavg2);

	public static String s = "UPTIME= 10:08:10 up 67 days, 17:21,  1 user,  load average: 4.62, 3.88, 3.75";
	public static void main(String[] args) {

		
		System.out.println("====uptime_1");
		match(uptime_1);
		System.out.println("====uptime_2");
		match(uptime_2);
		System.out.println("====uptime_3");
		match(uptime_3);
		System.out.println("====uptime_4");
		match(uptime_4);
		System.out.println("====uptime_5");
		match(uptime_5);
		System.out.println("====uptime_6");
		match(uptime_6);
		System.out.println("====uptime_7");
		match(uptime_7);
		System.out.println("====uptime_8");
		match(uptime_8);
		System.out.println("====uptime_9");
		match(uptime_9);
		

	}

	private static void match(Pattern pattern) {
		
		Matcher m =pattern.matcher(s);
		
		if(m.find()){
			for(int i=1;i<=m.groupCount();i++){
				System.out.println(m.group(i));
			}
		}
		
	}

}
