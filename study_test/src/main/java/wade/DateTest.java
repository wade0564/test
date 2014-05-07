package wade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTest {

	public static void main(String[] args) throws ParseException {

//		System.out.println(new Date("Tue Dec 17 09:48:01 EST 2013").getTime());
//
//		System.out.println(new Date(1387234081000L));
//		System.out.println("Tue Dec 17 09:48:01 EST 2013");
//
		
		System.out.println(new Date(1396423881000L));
	
		System.out.println("System default -"+ null);
		System.out.println(Calendar.getInstance().getTimeInMillis());
		Calendar calendar =Calendar.getInstance();
		calendar.add(calendar.DATE, -1);
		
//		calendar.set(2013, 0, 1);
		System.out.println("Calendar---"+calendar.getTime());
		
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss a z");
		
		Date date = simpleDateFormat.parse("Monday December 16 2013 3:32:32 PM PST");
		System.out.println(date);
//		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println(date.getTime());
//		System.out.println(simpleDateFormat.format(date));
		
		
//		System.out.println(new Date(1378820275000L));
//		System.out.println(new Date(1312827842000L));
//		
//		System.out.println("Mon Sep 10 06:37:55 PDT 2013");

	}

}
