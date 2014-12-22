package cn.edu.ustc.wade.string;

/**
 * @author wade
 * @version Nov 27, 2014 4:49:36 PM
 */
public class SubStringTest {

	public static void main(String[] args) {

		String preFileLocation = "/data1/primary/staging/logParser/logParser_storage/bios/90830676/";
		
		System.out.println(preFileLocation.lastIndexOf("\\"));
		
//		preFileLocation.lastIndexOf("\\")
		String substring = preFileLocation.substring(0, preFileLocation.lastIndexOf('\\'));
		
		System.out.println(substring);
		
	}

}
