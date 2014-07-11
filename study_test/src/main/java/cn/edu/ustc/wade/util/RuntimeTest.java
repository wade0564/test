package cn.edu.ustc.wade.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class RuntimeTest {
	
	public static void main(String[] args) throws Exception {

				Runtime rt = Runtime.getRuntime();

				Process p1 = rt.exec(String.format("ipconfig"));
				
				BufferedReader br = new BufferedReader(new InputStreamReader(p1.getInputStream())) ;
				

				if (p1.waitFor() != 0) {
				}
				String s="";
				while((s=br.readLine())!=null){
					System.out.println(s);
				}

	}

}
