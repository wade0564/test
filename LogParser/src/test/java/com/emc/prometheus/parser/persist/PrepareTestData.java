package com.emc.prometheus.parser.persist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.emc.prometheus.parser.dedupe.TsAndMsg;

/** 
 * @author wade 
 * @version Nov 20, 2014 8:55:14 AM 
 */
public class PrepareTestData {
	
	
	
	public static List<TsAndMsg>  prepare() throws ParseException{
		
		List<TsAndMsg> tsAndMsgs = new ArrayList<>();
		
		
		String []  tsStrs= {"2014 Jul 19 22:10:27", "2014 Jul 19 22:10:58", "2014 Jul 19 22:11:58", "2014 Jul 19 22:12:58", "2014 Jul 19 22:13:58", "2014 Jul 19 22:14:58", "2014 Jul 19 22:15:58", "2014 Jul 19 22:16:58", "2014 Jul 19 22:17:58", "2014 Jul 19 22:18:58"};
		String [] msgs= {"Jul 19 22:10:27 sara_test_1", "Jul 19 22:10:58 sara_test_2", "Jul 19 22:11:58 sara_test_3", "Jul 19 22:12:58 sara_test_4", "Jul 19 22:13:58 sara_test_5", "Jul 19 22:14:58 sara_test_6", "Jul 19 22:15:58 sara_test_7", "Jul 19 22:16:58 sara_test_8", "Jul 19 22:17:58 sara_test_9", "Jul 19 22:18:58 sara_test_10"};
		
		SimpleDateFormat sdf =	new SimpleDateFormat("yyyy MMM dd hh:mm:ss");
		
		for (int i = 0; i < msgs.length; i++) {
			
			Long ts = sdf.parse(tsStrs[i]).getTime();
			String msg= msgs[i];
			TsAndMsg tsAndMsg = new TsAndMsg(ts, msg, false);
			tsAndMsgs.add(tsAndMsg);
			
		}
		
		return tsAndMsgs;
	}
	
	
	@Test
	public void test() throws ParseException{
		List<TsAndMsg> prepare = prepare();
		
		for (TsAndMsg tsAndMsg : prepare) {
			System.out.println(tsAndMsg.getTs()+"|"+tsAndMsg.getMsg());
		}
		
	}
	

}
