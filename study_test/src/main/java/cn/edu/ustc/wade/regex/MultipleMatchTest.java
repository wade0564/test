package cn.edu.ustc.wade.regex;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class MultipleMatchTest {

	public static void main(String[] args) throws IOException {

		String content = Files.toString(new File("C:/Users/zhangw4/notes/data/output_3.dat"), Charsets.UTF_8);
		
		

//		System.out.println(content.replaceAll("\r\n", "##"));
		Pattern linePattern = Pattern.compile("^(\\d+\\|).*\r\n(?!\\1)\\d+.*",Pattern.MULTILINE);
		
		Matcher lineMatcher = linePattern.matcher(content);
		
		

		while(lineMatcher.find()){
			System.out.println("==========");
			System.out.println(lineMatcher.group());
		}
		

	}
}