package cn.edu.ustc.wade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayTest {
	
	
	public static void main(String[] args) {
		
		
		String [] arr={"1","11"};
		System.out.println(Arrays.asList(arr).toArray(new String[0])[1]);
		
		List<String> list1= Arrays.asList(arr);
		List<String> list =new ArrayList<String>();
		list.add("1");
		String[] arr2 =list.toArray(new String[0]);
		
		for (String string : list1) {
			System.out.println(string);
		}
	}
	

}
