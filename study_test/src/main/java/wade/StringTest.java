package wade;

public class StringTest {
	
	
	public static void main(String[] args) {
		
//		String s ="dddd\r";
//		
//		String s2 =s.trim();
//		
//		System.out.println(s2);
//		
//		new StringTest().change(s2);
//		
//		System.out.println(s2);
		
		String s1 = "/auto/support/ftpusers/EMC_csftp.tgz/swatibundle.tgz ";
		String s2 = "/auto/support/ftpusers/EMC_csftp.tar.gz/swatibundle.tar.gz ";
		String s = s1.replaceAll("(\\.tgz)|(\\.tar\\.gz)$", "/");
		System.out.println(s);
		s = s2.replaceAll("(\\.tgz)|(\\.tar\\.gz)$", "/");
		System.out.println(s);
	}
	
	
	public void change(String old){
		
		String n ="1";
		
		old=n;
	}
	

}
