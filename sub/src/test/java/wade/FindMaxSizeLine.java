package wade;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FindMaxSizeLine {
	
	
	
	public static void main(String[] args) throws Exception {
		
//		BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Users\\zhangw4\\notes\\sql\\missing")));
		BufferedReader br = new BufferedReader(new FileReader(new File("c:/tmp/DDFS_INFO_9700625822748631")));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("c:/tmp/DDFS_INFO_output")));
		
		
		
		String line=""; 
		
		int lineNum =1;
		int maxNum=1;
		int maxLength=0;
		String  s ="";
		while((line= br.readLine())!=null){
			if(maxLength<line.length()){
				maxLength=line.length();
				maxNum=lineNum; 
				s=line;
			}
			
			lineNum++;
		}
		
		bw.write(s);
		bw.close();
		System.out.println(maxNum);
		System.out.println(maxLength);
		
	}
	
	

}
