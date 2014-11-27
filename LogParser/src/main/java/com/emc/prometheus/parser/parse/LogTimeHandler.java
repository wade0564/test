package com.emc.prometheus.parser.parse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LOG_TYPE;

public class LogTimeHandler {
	private String genDateFormat;
	private String infoDateFormat;
	private String infoDateRegex;
	private String infoTsRegex;
	
	final static Logger log = LoggerFactory.getLogger(LogTimeHandler.class);

	private static SimpleDateFormat DATE_FOMRAT_ISO = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public LogTimeHandler() {
		this("yyyy-MM-dd HH:mm:ss", "yyyy MMM dd HH:mm:ss",
				"^\\w{3}\\s+\\d+\\s+\\d{2}:\\d{2}:\\d{2}",
				"(?<=\\(E\\d\\)\\[)\\s*\\d+");
	}

	public LogTimeHandler(String genDateFormat, String infoDateFormat,
			String infoDateRegex, String infoTsRegex) {
		this.genDateFormat = genDateFormat;
		this.infoDateFormat = infoDateFormat;
		this.infoDateRegex = infoDateRegex;
		this.infoTsRegex = infoTsRegex;
	}

	public static LogTimeHandler getTimeHandlerInstance(LOG_TYPE type) {

		if (type != LOG_TYPE.VTL_INFO) {
			return new LogTimeHandler();
		} else {
			return new LogTimeHandler(
					"yyyy-MM-dd HH:mm:ss",
					"yyyy MM/dd HH:mm:ss.SSS",
					"^\\s*(\\d{2}/\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3})",
					"(?<=\\(E\\d\\)\\[)\\s*\\d+");
		}

	}

	@SuppressWarnings("deprecation")
	public long[] genDateHandler(String genDate, long fetchTimestamp,
			List<String> logs) throws Exception {
		try {
			// the date presents an upper bound of log time
			Date lastDate;
	
			// prepare the result array
			long[] resultList = new long[logs.size()];
	
			// use geninfo.epoch if no "GENERATED:" field in log.
			if (genDate == null) {
				lastDate = new Date(fetchTimestamp + 3600000*24);
			} else {
				lastDate = this.parseDateByString(genDate, this.genDateFormat);
			}
	
			// fix time if it's in future
	
			if (lastDate.getTime() > new Date().getTime()) {
				lastDate = new Date();
			}
	
	
			// handle log one by one from latest to oldest
			for (int i = logs.size() - 1; i >= 0; i--) {
				String l = logs.get(i);
				String dateInfoStr = this.getDateInfo(l);
				long tsInfoNum = this.getTsInfo(l);
				Date infoDate = this.parseDateByString( getUTCYear(lastDate).toString() + " " + dateInfoStr, this.infoDateFormat);
				if (infoDate.after(lastDate) && infoDate.getTime()-lastDate.getTime()>24*60*60*1000 ) {// 1 day fault tolerance
					infoDate = this.parseDateByString( (getUTCYear(lastDate)-1) + " " + dateInfoStr, this.infoDateFormat);
				}
				Date tsDate = infoDate; 
				if (dateInfoStr != null && tsInfoNum != 0L) {
					tsDate = new Date(tsInfoNum);
				}
				lastDate = this.selectDateBetween(infoDate, tsDate);
				resultList[i] = lastDate.getTime();
			}
			/*
			 * Print Log with Timestamp for (int i = 0; i< logs.size(); ++i) {
			 * DATE_FOMRAT_ISO.setTimeZone(TimeZone.getTimeZone("UTC"));
			 * System.out.println( DATE_FOMRAT_ISO.format(resultList[i]) + " : " +
			 * logs.get(i)); }
			 */
			return resultList;
		}
		catch (Exception e) {
			// TODO LOGGER
			log.error( "Error for Asup Id: {}", e.getMessage());
			//e.printStackTrace();
			throw e;
		}
	}

	// String parsed to Date object with param format
	private Date parseDateByString(String dateStr, String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			//e.printStackTrace();
			log.error("Error for Asup Id: {}", e.getMessage());
			throw new Exception("Exception in parse date : " + dateStr);
		}
		return date;
	}

	// get string from list item
	private String getItemFromString(String str, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return m.group(0);
		} else {
			return null;
		}
	}

	// get date info from list item
	private String getDateInfo(String str) {
		if (str == null) {
			return null;
		}
		String item = this.getItemFromString(str, this.infoDateRegex);
		if (item == null) {
			return null;
		}
		return item.trim();
	}

	// get ts info from list item
	 private long getTsInfo(String str) {
         if (str == null) {
                return 0L;
         }
         String item=this.getItemFromString(str, this.infoTsRegex);
         if(item==null || item.length()>10){
                return 0L;
         }
         return Long.valueOf(item.trim()) * 1000; 
  }


	// select date between string date and ts date
	private Date selectDateBetween(Date infoDate, Date tsDate) {
		if ((tsDate.getYear() + 1900) <= 2005) {
			return infoDate;
		}

		final long TS_2_DAYS = 2 * 24 * 3600 * 1000;

		Date tmpDate = new Date(infoDate.getTime());
		while (tsDate.getTime() < tmpDate.getTime() + TS_2_DAYS) {
			tmpDate.setYear(tmpDate.getYear() - 1);
			if (Math.abs(tmpDate.getTime() - tsDate.getTime()) < TS_2_DAYS) {
				// adjust year
				infoDate.setYear(tmpDate.getYear());
				break;
			}
		}

		return infoDate;
	}
	
	@SuppressWarnings("deprecation")
	private Integer getUTCYear(Date date) {
		if (date == null) {
			throw new RuntimeException("null parameter");
		}
		
		return 1900 + new Date(date.getTime() + date.getTimezoneOffset() * 60* 1000).getYear();
	}
}
