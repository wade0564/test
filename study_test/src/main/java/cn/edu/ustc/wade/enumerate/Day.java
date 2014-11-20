package cn.edu.ustc.wade.enumerate;

import com.mongodb.Mongo;

/** 
 * @author wade 
 * @version Nov 19, 2014 4:46:07 PM 
 */
public enum Day {
	
	Mon("monday"),Tue("Tuesday");
	
	String day;
	
	private Day(String day) {
		
		this.day = day;
	
	}
	
	public String getValue(){
		return this.day;
	};

}
