package cn.edu.ustc.wade.file;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestFilenamePath {
	

	public static void checkOtherExistence(File srcFile) {

		DateFormat df = null;
		Date parsedDate = null;
		DateFormat folderDateFormat =new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar =Calendar.getInstance();
		try {
			df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			parsedDate = df.parse(srcFile.getName().split("\\.")[0]);
			calendar.setTime(parsedDate);
			int minute =calendar.get(calendar.MINUTE);
			
			String todayFolder =folderDateFormat.format(parsedDate);
			
			if(minute<30){
				calendar.add(Calendar.DATE, -1);
			}else{
				calendar.add(Calendar.DATE, +1);
			}
			
				String	otherFolder = folderDateFormat.format(calendar.getTime()); 
				String otherFolderPath =srcFile.getName().replace(todayFolder, otherFolder);
				System.out.println(otherFolderPath);
		} catch (ParseException e1) {
		}
//		long day = (new Date().getTime() - parsedDate.getTime())
//				/ (24 * 60 * 60 * 1000);
//		if (day > mercyTime) {
//			asupQueueObj.setStatus("FILE_NOT_FOUND");
//		}

	}
	
	
	public static void main(String[] args) {
		
		File file =new File("2014-04-10_12-43-16.28151");
		
		checkOtherExistence(file);
	}

}
