package wade.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wade.project.asupqueuing.util.Params;
import ch.qos.logback.core.joran.action.NewRuleAction;

public class FileTest {

	public static void main(String[] args) throws IOException {

		File log = new File(
				"C:/Users/zhangw4/Desktop/tmp/whitcher_Parser.2014-04-19.1.log");

		File log2 = new File(
				"C:/Users/zhangw4/Desktop/tmp/whitcher_Parser.2014-04-19.2.log");
		BufferedReader br = new BufferedReader(new FileReader(log));
		
		String s="";
		
		Pattern p = Pattern.compile(".*(\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}\\.\\d{4}).*");  
		
		Set<String> filenames =new HashSet<String>();

	
		
		while((s=br.readLine())!=null){
			Matcher  matcher = p.matcher(s);
			if(matcher.find()){
				filenames.add(matcher.group(1));
			}
		}
		
		br = new BufferedReader(new FileReader(log2));
		while((s=br.readLine())!=null){
			Matcher  matcher = p.matcher(s);
			if(matcher.find()){
				filenames.add(matcher.group(1));
			}
		}
		
		
		for (String string : filenames) {
			System.out.println(string);
		}

		
		
		// new File("wade11").mkdir();

		// File f =new File("c:/2/1");
		// f.mkdirs();
		// f.delete();

		// String filename = "2014-02-15_06-46-49.22255";
		// String sub_dir = filename.substring(0, filename.indexOf("_"));
		//
		// DateFormat df =new SimpleDateFormat("yyyy-MM-dd");
		// Date parsedDate =null;
		// try {
		// System.out.println(sub_dir);
		// parsedDate=df.parse(sub_dir);
		// System.out.println(parsedDate);
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		//
		//
		// long day = (new
		// Date().getTime()-parsedDate.getTime())/(24*60*60*1000);
		// System.out.println("相差day:"+day);
		// System.out.println("today in month :"+new Date().getMonth());
		// System.out.println("today in day :"+new Date().getDay());
		// System.out.println(parsedDate.getMonth());
		// System.out.println(sub_dir);
		//
		// sub_dir = sub_dir.replace("-", "/");
		// System.out.println(sub_dir);
		//
		// String filePath = "/" + sub_dir + "/" + filename;
		// System.out.println(filePath);

	}

}
