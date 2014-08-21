package wade.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.output.FileWriterWithEncoding;

public class File2Test2 {
	public static void main(String[] args) throws Exception {
		File file_in =new File("C:/Users/zhangw4/Desktop/parser/test");
		File file_out =new File("C:/Users/zhangw4/Desktop/parser/test.out");
		
		
		BufferedReader br = new BufferedReader(new FileReader(file_in));
		
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file_out), "UTF8"));
		BufferedWriter bw = new BufferedWriter(new FileWriterWithEncoding(file_out, "UTF-8"));
		
		String s="";
		

		Pattern capture = Pattern
				.compile("([^\\r\\n\\x00]++)[\\r\\n]*");
		

		
		while((s=br.readLine())!=null){
			Matcher m =capture.matcher(s);
			if(m.find()){
				out.write(m.group(1)+"\n");
			}
			
		}
		
		br.close();
		out.close();
		bw.close();
	    
		
	}

}
