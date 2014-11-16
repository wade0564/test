package cn.edu.ustc.wade;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LineMathCount {
	
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner scanner =new Scanner( new File("C:/tmp/sub/upload.1-support-bundle/ddr/var/log/debug/vtl.info"));
		scanner.useDelimiter("\n");
		
		System.out.println("01/01 18:47:26.768 (tid 0x2aaac7631e80): VTL read ahead stats for ollie_nwdd22a_05/DA0729L3:".matches("(^\\s*(\\d{2}/\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3})\\s+\\(tid.*)"));
		
		int line=0;
		while (scanner.hasNext()) {
			String string = (String) scanner.next();
//			System.out.println(string);
			if(string.matches("(^\\s*(\\d{2}/\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3})\\s+\\(tid.*)")){
				line++;
			}
			
		}
		
		System.out.println(line);
		
	}
	
	
	

}
