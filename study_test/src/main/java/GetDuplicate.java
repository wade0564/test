import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cn.edu.ustc.wade.dao.BaseDao;


public class GetDuplicate {
	
	static BufferedWriter bw;
	static BufferedReader br;
	public GetDuplicate() throws IOException {
		
		
		
	}
	
	
	public static void main(String[] args) throws Exception {
		Set<String> asups =new HashSet<String>();
		BaseDao dao = new BaseDao();
		ResultSet rs = dao.query("select substring (filename,12) from asup.asup where filename  like '%2014-05%'");
		
		while (rs.next()) {
			asups.add(rs.getString(1));
		}
		
		rs = dao.query("select file_name from asup.asup_queue where file_name  like '%2014-05%'");
		while (rs.next()) {
			asups.add(rs.getString(1));
		}
		System.out.println("Total:"+asups.size());
		writeDuplicate(asups);
		
	}


	private static void writeDuplicate (Set<String> asups)  throws Exception{
		
		bw = new BufferedWriter(new FileWriter(new File("C:/Users/zhangw4/git/test/study_test/duplicate_customer.dat")));
		br =new BufferedReader(new FileReader(new File("C:/Users/zhangw4/git/test/study_test/output_customer.bak")));
		String s="";
		int count=0;
		while((s=br.readLine())!=null){
			s=s.replace(".gz", "");
			if(!asups.contains(s)){
				bw.write(s+"\n");
				count++;
			}
		}
		bw.flush();
		bw.close();
		System.out.println("Duplicate:"+count);
		
	}
	
	
	
	
	

}
