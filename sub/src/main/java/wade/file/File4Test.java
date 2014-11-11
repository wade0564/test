package wade.file;

import java.io.File;

public class File4Test {
	public static void main(String[] args) throws Exception {
		
		
		File file = new File("C:/workspace/r_msgq/analytics/prometheus/Hermes-Application/components/master/target/test-classes/mvc-test.xml");
		
		System.out.println(file.toPath());
		System.out.println(file.toURI());
		System.out.println(file.toURI().toURL());
		
		
		
	}

}
