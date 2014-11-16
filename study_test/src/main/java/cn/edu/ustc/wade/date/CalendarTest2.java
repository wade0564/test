package cn.edu.ustc.wade.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CalendarTest2 {

	public static void main(String[] args) throws Exception {

//		System.out.println(new Date("Tue Dec 17 09:48:01 EST 2013").getTime());
//
//		System.out.println(new Date(1387234081000L));
//		System.out.println("Tue Dec 17 09:48:01 EST 2013");
//
		
		String dateInfo="01/01 00:17:02";
		
		String lastDate_str="Fri Dec 31 16:17:12 PST 2014";
		String infoDate_str="Wed Dec 31 16:17:02 PST 811";
		
		String s="Fri Dec 13 06:00:01 GMT+8:00 2013";
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
		
		simpleDateFormat.parse(infoDate_str);
		
//		Date lastDate= simpleDateFormat.parse(lastDate_str);
////		Date lastDate= new Date(lastDate_str);
//		Date infoDate = parseDateByString((1900 + lastDate.getYear())
//				+ " " + dateInfo, "yyyy MMM dd HH:mm:ss");
//		
//		System.out.println(lastDate);
//		System.out.println(infoDate);

	}
	
	private static Date parseDateByString(String dateStr, String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			//e.printStackTrace();
			throw new Exception("Exception in parse date : " + dateStr);
		}
		return date;
	}


}
