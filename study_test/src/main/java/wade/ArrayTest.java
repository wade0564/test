package wade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayTest {
	
	
	public static void main(String[] args) {
		
		
		List l=null;
		
		for (Object object : l) {
			System.out.println(object);
		}
		
		
		String [] arr={"1","11"};
		System.out.println(Arrays.asList(arr).toArray(new String[0])[1]);
		
		List<String> list =new ArrayList<String>();
		list.add("1");
		String[] arr2 =list.toArray(new String[0]);
		
		for (String string : arr2) {
			System.out.println(string);
		}
	}
	

}
