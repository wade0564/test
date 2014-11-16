package cn.edu.ustc.wade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTest2 {

	public String getDate(String dateStr, Integer interval) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		Date date;

		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			// handle this exception

			return null;
		}

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, interval);

		Date newDate = c.getTime();

		return df.format(newDate);
	}

	public static void main(String args[]) throws ParseException {
		DateTest2 t = new DateTest2();

		System.out.println(t.getDate("2014-03-03", 10));
		System.out.println(t.getDate("2014-03-03", -10));
	}



}
