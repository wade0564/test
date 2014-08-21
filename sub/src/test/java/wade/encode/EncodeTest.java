package wade.encode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

import org.aspectj.weaver.NewConstructorTypeMunger;

public class EncodeTest {

	public static void main(String[] args) throws IOException, ParseException {

		
		InputStream is = EncodeTest.class.getResourceAsStream("/gbk.file");
		InputStream is2 = EncodeTest.class.getResourceAsStream("/gbk.file");
		InputStream is3 = EncodeTest.class.getResourceAsStream("/gbk.file");

		
		
		int b ;
		
		while((b=is.read())!=-1){
			System.out.print(b);
		}
		
		System.out.println();
		
		InputStreamReader isr = new InputStreamReader(is2);
		
		char[] cbuf = new char[1];
		int offset= isr.read(cbuf);
		String s = new String(cbuf);
		
		byte [] bytes =s.getBytes("utf-8");
		System.out.println(new String(bytes,"gbk"));
		
		is.close();

	}

}
