package wade.string;
public class StringReplaceTest {
	
	
	
	public static void main(String[] args) {
		
		

		
		test2();
		
	}

	private static void test2() {
		String  s="HElLO r";
		System.out.println(s.compareToIgnoreCase("hello R"));
		
	}

	private static void test1() {
		String s ="Data Domain support has received the following DDR upload:\r\n\r\nFrom user: leej68\r\nLocation:\r\n/auto/support/uploads/leej68/ddr/bundle/upload.2-bundle-2014-05-14.tar.g\r\nz\r\nDate: Wednesday, May 14, 2014 5:42:51 PM PDT\r\nMD5: e000856bc360480836d984d3942cffb2\r\nSize: 840,846,348 bytes, 821,139 KBytes";
		
//		String result= s.replaceAll("(\\\\r\\\\n){2,}", "\\\\r\\\\n").replaceAll("(?<=Location:)\\\\r\\\\n", "");
		String result= s.replaceAll("(\r\n){2,}", "\r\n").replaceAll("(?<=Location:)\r\n", "");
		
		System.out.println(result);
	}
	
	
	
	
}
