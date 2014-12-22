package cn.edu.ustc.wade.ssh;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.io.Files;

/** 
 * @author wade 
 * @version Dec 4, 2014 2:21:47 PM 
 */
public class ParsedResultAnalysis4 {
	
	static SimpleDateFormat sdf =new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
	static SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static List<Integer> asupids = new ArrayList<>();
	static int i =0 ;
	
	public static void main(String[] args) throws Exception {
		
		
		List<Log> logs = extract(Files.toString(new File("C:/Users/zhangw4/notes/data/output_2.dat"), Charsets.UTF_8));
//		List<Log> logs = extract(Files.toString(new File("C:/Users/zhangw4/notes/data/output_3.dat"), Charsets.UTF_8));
		
		
		Collections.sort(logs,new Comparator<Log>() {
			public int compare(Log o1, Log o2) {
				if( o1.ts.compareTo(o2.ts)==0){
					return o1.msg.compareTo(o2.msg);
				}else {
					return o1.ts.compareTo(o2.ts); 
				}
			}
		});
		
		String flag =null;
		i=0;
		
		List<String> writtenStrings = new ArrayList<>();
		for (; i < logs.size(); i++) {
			
			String msg ="";
			//forward
			Log log = logs.get(i);
//			if(i<logs.size()-2){
//				if(logs.get(i+1).ts.compareTo(logs.get(i+2).ts)==0){
//					msg = log.msg;
//				}
//			}
			if(i!=logs.size()-1){
				if(log.ts.compareTo(logs.get(i+1).ts)==0){
					msg = log.msg;
				}
			}
			//backward
			if(i>0){
				if(log.ts.compareTo(logs.get(i-1).ts)==0){
					msg = log.msg;
				}
			}
			//backward
//			if(i>1){
//				if(logs.get(i-2).ts.compareTo(logs.get(i-1).ts)==0){
//					msg = log.msg;
//				}
//			}
			
			if(asupids.get(i).equals(log.asupId)){
				flag="√";
			}else {
				flag ="×";
			}
			String display=	String.format("%s|%s|%s|%s|%s", flag,asupids.get(i),log.asupId,displayFormat.format(log.ts),msg);
			writtenStrings.add(display);
//			System.out.println(display);
			
		}
		
		FileUtils.writeLines(new File("C:/Users/zhangw4/notes/data/output_analysis_2.dat"), writtenStrings);
		
		getSplitAsupid();
		
	}
	
	
	private static void getSplitAsupid() {
		Multiset<Integer> asupMultiset = HashMultiset.create();
		asupMultiset.addAll(asupids);

		for (Integer asupid : asupMultiset) {
			if(asupMultiset.count(asupid)>2){
				System.out.println(asupid+"|"+asupMultiset.count(asupid));
			}
		}
		
	}


	private static List<Log> extract(String s) throws ParseException {
		
		Pattern linePattern = Pattern.compile("^(\\d+\\|).*\n(?!\\1)\\d+.*",Pattern.MULTILINE);
		
		Matcher lineMatcher = linePattern.matcher(s);
		
		List<Log> logs = new ArrayList<>();
		while(lineMatcher.find()){
			String matchedLine = lineMatcher.group();
			Pattern p  = Pattern.compile("^(\\d+)\\|([^\\|]*)\\|(.*)",Pattern.MULTILINE);
			Matcher m = p.matcher(matchedLine);
			
			while (m.find()) {
				i++;
				Log log = new Log();
				log.asupId = Integer.valueOf( m.group(1));
				log.ts = sdf.parse(m.group(2));
				log.msg = m.group(3);
				logs.add(log);
				asupids.add(Integer.valueOf( m.group(1)));
			}
		}
		
		System.out.println("========Total : "+i);
		System.out.println("========Total List : "+logs.size());
		
		i=0;
		return logs;
	}

}

class Log {
	int asupId;
	Date ts;
	String msg;
}
