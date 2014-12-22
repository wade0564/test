package cn.edu.ustc.wade.string;

import java.util.concurrent.TimeUnit;

/** 
 * @author wade 
 * @version Dec 3, 2014 9:47:42 PM 
 */
public class SplitString {
	
	
	public static void main(String[] args) {
		
		
		String []  times = {"2053,167 ", " 805,630 ", " 412,847 ", " 471,613 ", "  44,060 ", "  48,897 ", "  42,538 "};
		
		Integer total =0;
		
		for (String time : times) {
			
			Integer t = Integer.valueOf(time.trim().replace(",", ""));
			
			total+=t;
			
			System.out.println(TimeUnit.MILLISECONDS.toSeconds(t)+"s ,"+TimeUnit.MILLISECONDS.toMinutes(t)+"min");
			
		}
		
		System.out.println(TimeUnit.MILLISECONDS.toMinutes(total));
		
		
	}
	

}
