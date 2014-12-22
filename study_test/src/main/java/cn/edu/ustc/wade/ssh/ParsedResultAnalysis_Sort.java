package cn.edu.ustc.wade.ssh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/** 
 * @author wade 
 * @version Dec 4, 2014 2:21:47 PM 
 */
public class ParsedResultAnalysis_Sort {
	
	static SimpleDateFormat sdf =new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
	static SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static List<Integer> asupids = new ArrayList<>();
	static int i =0 ;
	
	public static void main(String[] args) throws Exception {
		
		
//		List<Line> lines = extract(Files.toString(new File("C:\\Users\\zhangw4\\notes\\data\\23317670-85954548"), Charsets.UTF_8));
		List<Line> lines = extract(Files.toString(new File("/data2/dev/debug/storage/messages/23317670/23317670-85954548"), Charsets.ISO_8859_1));
		
		
		Collections.sort(lines,new Comparator<Line>() {
			public int compare(Line o1, Line o2) {
				if( o1.ts.compareTo(o2.ts)==0){
					return o1.msg.compareTo(o2.msg);
				}else {
					return o1.ts.compareTo(o2.ts); 
				}
			}
		});
		
		
		
		
		BufferedWriter bw = new BufferedWriter(new  FileWriter (new File("/data2/dev/debug/tmp/sort_messages")));
		
		for (Line line : lines) {
//			writtenLines.add(line.msg);
			bw.write(line.msg+"\n");
		}
		
//		FileUtils.writeLines(new File("C:/Users/zhangw4/notes/data/output_2.dat"), writtenLines);
		bw.flush();
		bw.close();
		
	}
	
	


	private static List<Line> extract(String s) throws ParseException {
		
		List<Line> lines = new ArrayList<>();
		
		Pattern p  = Pattern.compile("^(\\d+)\\|([^\\|]*)\\|(.*)",Pattern.MULTILINE);
		Matcher m = p.matcher(s);
		
		while (m.find()) {
			Line line = new Line();
			line.ts = sdf.parse(m.group(2));
			line.msg = m.group();
			lines.add(line);
		}
		
		System.out.println("========Total List : "+lines.size());
		
		return lines;
	}

}

class Line {
	String msg;
	Date ts;
}
