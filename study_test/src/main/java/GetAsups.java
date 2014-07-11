import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class GetAsups {
	
	
	Set<String> fileStrings = new HashSet<String>();
	
	String customerPath ="/asuprdr10/asupmails/asup_archive/DD/emails/";
	String internalPath ="/asuprdr10/asupmails/asup_archive/DD/internal/emails/";
	
	
	public void  initFileStrings() throws IOException {
		File noneSnAsups =new File("noneSnAsups");
		
		BufferedReader br = new BufferedReader( new FileReader(noneSnAsups));
		
		String s="";
		
		while((s=br.readLine())!=null){
			fileStrings.add(s);
		}
		
	}
	
	
	public void  findAsups() throws FileNotFoundException{
		
		
		for (String filename : fileStrings) {
			
			String date_folder = filename.substring(0, filename.indexOf("_"));
			date_folder = date_folder.replace("-", "/");
			
			String asupFilePath =internalPath+date_folder+"/"+filename;
			
			File asupFile= new File(asupFilePath);
			
			System.out.println(asupFilePath);
			if(asupFile.exists()){
				System.out.println(asupFile.getName()+":"+copy(asupFilePath));
			}
			
			//in case of wrong file source of asup
			if(!asupFile.exists()){
				
				asupFilePath =customerPath+date_folder+"/"+filename;
				asupFile= new File(asupFilePath);
				System.out.println(asupFile.getName()+":"+copy(asupFilePath));
			}
			
		}
		
	}
	
	
	private boolean copy(String absolutePath ) throws FileNotFoundException {

		try {
			Runtime rt = Runtime.getRuntime();

			System.out.println(String.format("cp %s /home/dev/tmp/asup/",absolutePath));
			Process p1 = rt.exec(String.format("cp %s /home/dev/tmp/asup/",absolutePath));
			
			
			if (p1.waitFor() != 0) {
				return false;
			}

		} catch (Exception e) {
			System.out.println("Exception while unpacking Sub tar.gz files");
			throw new FileNotFoundException("File not found");
		}

		return true;
	}
	
	

	public static void main(String[] args) throws IOException {
		
		GetAsups getter =new GetAsups();
		
		getter.initFileStrings();
		
		getter.findAsups();
		
		
	}
	
	
	

}
