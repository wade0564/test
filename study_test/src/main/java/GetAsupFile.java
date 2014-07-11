import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class GetAsupFile {
	
	static String output_internal ="/home/dev/tmp/output_internal.bak";
	static String output_customer ="/home/dev/tmp/output_customer.bak";
	BufferedWriter bw_internal;
	BufferedWriter bw_customer;

	public GetAsupFile() throws Exception {
		bw_internal = new BufferedWriter(new FileWriter(output_internal));
		bw_customer = new BufferedWriter(new FileWriter(output_customer));
	}
	
	
	public static void main(String[] args) throws Exception {
		
		String customerPath ="/asuprdr10/asupmails/asup_archive/DD/emails/2014/05";
		String internalPath ="/asuprdr10/asupmails/asup_archive/DD/internal/emails/2014/05";
		
		File customefile =new File(customerPath);
		
		File internalfile =new File(internalPath);
		
		GetAsupFile getter  =new GetAsupFile();
		getter.getCustomerAsupFile(customefile);
		getter.getInernalAsupFile(internalfile);
		getter.bw_customer.close();
		getter.bw_internal.close();
		
	}

	private  void getInernalAsupFile(File file) throws IOException {
		
		if(file.isFile()){
			bw_internal.write(file.getName()+"\n");
		}else {
			File[] childFiles=file.listFiles();
			for (File child : childFiles) {
				getInernalAsupFile(child);
			}
		}
		
	}

	private  void getCustomerAsupFile(File file) throws IOException {

		if (file.isFile()) {
			bw_customer.write(file.getName()+"\n");
		} else {
			File[] childFiles = file.listFiles();
			for (File child : childFiles) {
				getCustomerAsupFile(child);
			}
		}

	}

}
