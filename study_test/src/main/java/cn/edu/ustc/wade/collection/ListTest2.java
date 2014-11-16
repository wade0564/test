package cn.edu.ustc.wade.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


class T{
	
	Integer count;

	public T(String countStr) {
		this.count = Integer.valueOf(countStr);
	}
	
	

}

public class ListTest2 {
	

	
	static Comparator<T> comparator  = new Comparator<T>() {

		public int compare(T o1, T o2) {
			return o1.count.compareTo(o2.count);
		}
		
	};

	public static void main(String[] args) {

		T t1 =new T("1");
		T t3 =new T("31");
		T t2 =new T("22");
		T t4 =new T("13");
		
		
		List<T> list1 = new ArrayList<T>();
		list1.add(t3);
		list1.add(t1);
		List<T> list2 = new ArrayList<T>();
		list2.add(t2);
		list2.add(t4);
		
		List<T> l = new ArrayList<T>();
		
		l.addAll(list1);
		l.addAll(list2);
		
		System.out.println("--- list");
		for (T t : list1) {
			System.out.println(t.count);
		}
		
		Collections.sort(l, comparator);
		
		System.out.println("--- list");
		for (T t : l) {
			System.out.println(t.count);
		}
		System.out.println("--- list1");
		for (T t : list1) {
			System.out.println(t.count);
		}
		System.out.println("--- list2");
		for (T t : list2) {
			System.out.println(t.count);
		}
	}

}
