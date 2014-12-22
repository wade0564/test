package cn.edu.ustc.wade.string;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;
import com.google.common.primitives.Chars;

/** 
 * @author wade 
 * @version Dec 9, 2014 5:59:19 PM 
 */
public class StringCharBytesTest2 {
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		
		
		char c = '汉';
		
		char a ='a';
		
		String s = "h汉";
		
//		System.out.println("Chinese :"+Chars.toByteArray(c).length);
//		System.out.println("a:"+Chars.toByteArray(c).length);
//		
//		
//		File f = new File("C:/tmp/asup/internal/2014/10/29/2014-11-10_21-08-20.23893");
//		
//		System.out.println(f.length());
//		
//		String s = IOUtils.toString(new FileInputStream(f));
		
		byte[] bytes = s.getBytes(Charsets.UTF_8);
		
		final byte[] utf32Bytes = s.getBytes("UTF-16");
		System.out.println(utf32Bytes.length);
		
		System.out.println(bytes.length);
		
	}
	

}
