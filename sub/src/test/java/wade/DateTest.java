package wade;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTest {

	public static void main(String[] args) throws ParseException {
		
//		System.out.println(Calendar.getInstance().getTimeInMillis());
		String send_from_date_Str ="2014/4/2";
		
		
		Date send_from_date;
		
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		try {
			send_from_date= df.parse(send_from_date_Str);
			
			System.out.println(send_from_date);
		} catch (ParseException e) {
			throw e;
		}
	}

}
