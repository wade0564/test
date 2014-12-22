import java.io.BufferedReader;
import java.io.InputStreamReader;



public class UnPacking {
	
	
	public static void main(String[] args) throws Exception {
		
		unpackSub("/auto/support/uploads/Richard.Pate@pseg.com/ddr/bundle/upload.5-bundle-2014-07-22.tar.gz", "/data2/dev/LogParser/unzip_sub_temp");
//		unpackSub("/auto/cores/c2116735/atlodd1_10.3.8.48_support-bundle.tar.gz", "/data2/dev/LogParser/unzip_sub_temp");
		
	}
	
	
		 static void unpackSub(String subFile, String destinatedPath) throws Exception {
			
			 BufferedReader reader =null;
			try {
				Runtime rt = Runtime.getRuntime();
				Process p = rt.exec(String.format("tar zxvf %s -C %s",subFile, destinatedPath));
				System.out.println(String.format("tar zxvf %s -C %s",subFile, destinatedPath));
				System.out.println("unpacking SUB : "+subFile);
//				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//				String line = reader.readLine();
//				StringBuilder sb = new StringBuilder("Shell: ");
//				while (line != null) {
//					sb.append("\n").append(line);
//					line = reader.readLine();
//				}
//				String info = sb.toString();
//				System.out.println(info);
				
				if (p.waitFor() != 0) {
					System.out.println(subFile+"invalid compressed data");
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("Exception while unpacking SUB : "+subFile);
				e.printStackTrace();
			}finally{
//				reader.close();
			}
		}
}
