package cn.edu.ustc.wade.collection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LinkedHashSetTest {
	
	
	public static void main(String[] args) {
		
		List<String> lst=Arrays.asList("a","b","c","b","a"); 
		for (Iterator iter = lst.iterator(); iter.hasNext();) { 
		String element = (String) iter.next(); 
		System.out.print(element+","); 

		} 
		System.out.println(); 
//		Set set=new LinkedHashSet<String>(); 
		Set set=new HashSet<String>(); 
		set.addAll(lst); 
		
		System.out.println("for each");
		for (Object s: set) {
			System.out.println(s);
		}
		
		System.out.println("---------");
		for (Iterator iter = set.iterator(); iter.hasNext();) { 
		String element = (String) iter.next(); 
		System.out.println(element); 

		}
		
		
	}
	
}
