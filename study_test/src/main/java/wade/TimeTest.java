package wade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeTest {
	
	public static void main(String[] args) throws ParseException {
		
		
		int time=1;
		int timeStamp =2;
				
		
		System.out.println(time = time>timeStamp?time:timeStamp);
		
		
		
		SimpleDateFormat sdf =new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
		Date date =sdf.parse("Thu Sep 26 06:00:01 CDT 2013");
		System.out.println(date);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println(sdf.format(date));
		
		
		
	}
	

}
