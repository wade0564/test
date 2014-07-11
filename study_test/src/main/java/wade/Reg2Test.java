package wade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg2Test {
	
	  public static void main(String[] args) {
		  
		  
		  String[] test ={"autosupport", "autosupport.", "autosupport.1", "autosupport.10", "autosupport.100", "autosupport.gz", "autosupport.1.gz", "autosupport.1.debuig", "autosupport.1.d"};
	  
		  
		  String s="04/28 21:28:44.200 (tid 0x2aaaab4e9410): /../vpart:/vol1/col1/cp1/cset: Container 476856073 with type CONT_DATA mapped to block 60758611 on disk.\n 04/28 21:28:44.199 (tid 0x2aba56f1ac00): gcpy_do: gcpy_job_ctx(locality_repair) 0 - checksum 17595344072372922541, 25 cont, 6878946 bytes, 817 fp, 0 skipped_cont, overall (checksum 17595344072372922541, 25 cont, 6878946 bytes, 817 fp, 0 skipped_cont)\n 04/28 21:28:44.200 (tid 0x2aba56f1ac00): gcpy_do: gcpy_job_ctx(locality_repair) 1 - checksum 15818210760575946655, 25 cont, 5728041 bytes, 671 fp, 0 skipped_cont, overall (checksum 3434745499657447218, 50 cont, 12606987 bytes, 1488 fp, 0 skipped_cont)";
		  
		  Pattern capture = Pattern
					.compile("([^\\r\\n]++)[\\r\\n]++");
		  
		  Matcher m =capture.matcher(s);
		  
		  while(m.find()){
			  System.out.println(m.group(1));
		  }
		  
//		  Pattern pattern =Pattern.compile("^autosupport(|\\.\\d+)(|\\.gz)$");
//		  
//		  for (String string : test) {
//			  
//			  Matcher matcher =pattern.matcher(string);
//			  
//			  if(matcher.find()){
//				  System.out.println(string +":" + matcher.find());
//			  }else {
//				  System.err.println(string +":" + matcher.find());
//			}
//			
//		}
//		  
	  
	  }

}
