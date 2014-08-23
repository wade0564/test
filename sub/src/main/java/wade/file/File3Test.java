package wade.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class File3Test {
	public static void main(String[] args) throws Exception {
		File file =new File("C:/tmp/test");
		
		FileInputStream fis = new FileInputStream(file);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String s=null;
//		while((s=br.readLine())!=null){
//			s=br.readLine();
//			System.out.println(s);
			
//		}
		
		char[] buffer =new char[1024];
		
		int n;
		while((n=br.read(buffer))!=-1){
			
			System.out.println(new String(buffer));
			
			
		}
		
		
		
		
			
		
//		byte[] buffer= new byte[4*1024];
//		 fis.read(buffer);
//		fis.close();
//		 System.out.println(new String(buffer));
//		
		
		
	}

}
