package cn.edu.ustc.wade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeTest {
	
	public static void main(String[] args) throws ParseException {
		
		
		int time=1;
		int timeStamp =2;
				
		
		Long l= null;
		long l2=1L;
		if(l==l2)
		{
			System.out.println(1);
		}
		
//		System.out.println(time = time>timeStamp?time:timeStamp);
		
		
		
//		SimpleDateFormat sdf =new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
//		Date date =sdf.parse("Thu Sep 26 06:00:01 CDT 2013");
//		System.out.println(date);
		
		
		String s ="Tue May 13 17:09:23 2014";
		SimpleDateFormat sdf= new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
		Date date =sdf.parse(s);
//		sdf.setTimeZone(TimeZone.getTimeZone("CST"));
//		System.out.println(sdf.format(date));
		
		
//		
//		for(int i=0;i<10;i++){
//			
//			System.out.println(System.nanoTime());
//		}
		
		
	}
	

}
