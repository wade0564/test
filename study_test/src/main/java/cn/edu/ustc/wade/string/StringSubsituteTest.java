package cn.edu.ustc.wade.string;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;

import cn.edu.ustc.wade.sys.Sys;

/** 
 * @author wade 
 * @version Dec 17, 2014 10:49:03 AM 
 */
public class StringSubsituteTest {
	
	public static void main(String[] args) {
		String s =  "unzip -o  /root/hermes/hermes-agent-${version}.zip hermes-agent.war -d $TOMCAT_HOME/webapps/";
		Map< String, String> valueMap = new HashMap<String, String>();
		valueMap.put("version", "1.01");
		System.out.println(StrSubstitutor.replace(s, valueMap));

	}
	
	
	
	
	

}
