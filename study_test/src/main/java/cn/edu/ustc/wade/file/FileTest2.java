package cn.edu.ustc.wade.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTest2 {

	public static void main(String[] args) throws IOException, ParseException {

		File log = new File(
				"C:/tmp/sub/upload.2-support-bundle/README");

		BufferedReader br = new BufferedReader(new FileReader(log));
		
		String s="";
		
		Pattern p = Pattern.compile("^VERSION=\\s*+(?:Data\\s++Domain[^\\d]++)?((\\d++)\\.(\\d++)\\.(\\d++)\\.(\\d++)-(\\d++))$");  
		
		Set<String> filenames =new HashSet<String>();
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");

	
		
		while((s=br.readLine())!=null){
			Matcher  matcher = p.matcher(s);
			if(matcher.find()){
//			Date d =simpleDateFormat.parse(matcher.group(1));
//			System.out.println(d);
				for(int i=0;i<matcher.groupCount();i++){
					System.out.println(i+":"+matcher.group(i));
					
				}
			}
		}
		

		
		

	}

}
