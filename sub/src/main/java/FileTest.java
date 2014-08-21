import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FileTest {
	
	public static void main(String[] args) throws IOException {
		
		File f  =new File("C:\\Users\\zhangw4\\Desktop\\SUB\\ddfs.info.2");
		
//		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		
//		bw.write("1\n");
		
//		bw.close();
		
		BufferedReader br = new BufferedReader( new FileReader(f));
		
		
		char [] buffer = new char[1024*1024];

		int n=-1;
		while ((n=br.read(buffer))!=-1) {
			
			System.out.println(new String(buffer));
			
		}
		
		
		
		
	}

}
