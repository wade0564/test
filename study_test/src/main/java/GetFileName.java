import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class GetFileName {
	

	static Set<String> fileStrings = new HashSet<String>();
	
	String customerPath ="/asuprdr10/asupmails/asup_archive/DD/emails/";
	String internalPath ="/asuprdr10/asupmails/asup_archive/DD/internal/emails/";
	
	
	public static void  initFileStrings() throws IOException {
		File noneSnAsups =new File("noneSnAsups");
		
		BufferedReader br = new BufferedReader( new FileReader(noneSnAsups));
		
		String s="";
		
		while((s=br.readLine())!=null){
			fileStrings.add(s.trim());
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		File file = new File("c:/tmp/asup/");
		File [] files =file.listFiles();
		initFileStrings();
		for (String filename : fileStrings) {
			System.out.println(filename);
		}
//		System.out.println(fileStrings.size());
//		System.out.println(files.length);
//		for (File file2 : files) {
//			if(!fileStrings.contains(file2.getName()))
//				System.out.println(file2.getName());
//		}
		
	}

	private  void getInernalAsupFile(File file) throws IOException {
		
	}

	private  void getCustomerAsupFile(File file) throws IOException {


	}

}
