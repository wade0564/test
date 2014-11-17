package cn.edu.ustc.wade.string;
public class StringReplaceTest {
	
	
	
	public static void main(String[] args) {
		
		

		
		test2();
		
	}

	private static void test2() {
		System.out.println(String.format("hello %s %s", "wade","zhang"));
		
		
		String nullObj =null;
		
		System.out.println(String.format("%s%s", "null",nullObj==null?"":nullObj).length());
		
	}

	
	
	
}
