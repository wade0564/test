package cn.edu.ustc.wade.log;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

	public static void main(String[] args) throws ParseException {
		
		Logger log =LoggerFactory.getLogger(LogTest.class);
		
		for (int i =0 ;i<50000;i++) {
			System.out.println(i);
			log.debug(i+"  This is wade first Log");
		}
		
		
//		try {
//			throw new  IOException("wade");
//		} catch (IOException e) {
//
//			log.error("Error:{}",e.getMessage());
//		}
//		
//		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//
//		// 打印logback的内部状态
//
//		StatusPrinter.print(lc);
		
//		System.out.println(System.currentTimeMillis());
//		log.info("merge Time :{}",new Date());
		
		System.out.println(TimeUnit.MINUTES.toMillis(1));
		System.out.println(TimeUnit.DAYS.toHours(1));

	}

}
