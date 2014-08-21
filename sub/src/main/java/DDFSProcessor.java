

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DDFSProcessor {
	
	private static final int BUFFER_SIZE = 32768;
//	private String parsedFileLoc =PropertiesUtil.getProperty("BackEnd.OutfileLoc");
	private String srcLoc ="/data1/primary/staging/external_file_test";
	private String destFile ="/data1/primary/staging/external_file_test/ddfs";
	
	
	

	public String execute() throws Exception {

		
		List<File> files = new ArrayList<File>();
		
		files.add(new File(srcLoc,"DDFS_INFO_1403145711838"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145738665"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145738731"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145738832"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145738974"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145750475"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145823330"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145823698"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145824366"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145825532"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145825776"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145905483"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145911093"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145911895"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145912015"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145912154"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145949859"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145980782"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145983641"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145984333"));
		files.add(new File(srcLoc,"DDFS_INFO_1403145984525"));		
		
		writeStoredFile(files);
		return "ok";
	}


	private void writeStoredFile(List<File> files) throws IOException {

		char[] buffer = new char[BUFFER_SIZE];
		File destf =new File(destFile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(destf), BUFFER_SIZE);
		
		for (File file : files) {
			BufferedReader br = new BufferedReader(new FileReader(file),BUFFER_SIZE);
			int n=-1;
			while((n=br.read(buffer))!=-1){
				bw.write(buffer, 0, n);
			}
			br.close();
		}
		bw.close();
	}
	
	public static void main(String[] args) throws Exception {
		
		DDFSProcessor processor = new DDFSProcessor();
		System.out.println( processor.execute());
		
		
		
	}
	

}
