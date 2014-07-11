import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.swing.text.AbstractDocument.BranchElement;

import ch.qos.logback.classic.pattern.FileOfCallerConverter;


public class GetSn {
	
	
	
	Set<String> fileStrings = new HashSet<String>();
	
	File dir ;
	BufferedWriter bw;
	
	
	public void  initFileStrings() throws IOException {
		File noneSnAsups =new File("noneSnAsups");
		bw = new BufferedWriter(new FileWriter("c:/tmp/sn.dat"));
		dir= new File("C:\\tmp\\asup");
		BufferedReader br = new BufferedReader( new FileReader(noneSnAsups));
		
		String s="";
		
		while((s=br.readLine())!=null){
			fileStrings.add(s);
		}
		
	}
	
	
	public void getSn() throws IOException{
		
	for (String  filename : fileStrings) {
		BufferedReader br = new BufferedReader(new InputStreamReader(new  GZIPInputStream(new FileInputStream(new File(dir, filename)))));
		String s="";
		while ((s=br.readLine())!=null) {
			
			if(s.startsWith("X-autosupport-systemid")){
				bw.write(s+"#"+filename+"\n");
				br.close();
				break;
			}
			
			
		}
		
	}
		bw.close();
		
	}
	
	
	
	public static void main(String[] args) throws IOException {
		GetSn sn =new GetSn();
		sn.initFileStrings();
		sn.getSn();
	}
	

}
