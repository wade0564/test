package cn.edu.ustc.wade.date;

import java.util.Date;

import jcifs.dcerpc.msrpc.netdfs;

/** 
 * @author wade 
 * @version Dec 4, 2014 5:15:50 PM 
 */
public class DateCompareTest {
	
	public static void main(String[] args) {
		
		
		Date d = new Date();
		Date d2 = new Date(9999);
		
		System.out.println(d.compareTo(d2));
		
	}

}
