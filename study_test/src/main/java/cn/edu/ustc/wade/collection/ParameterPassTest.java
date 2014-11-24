package cn.edu.ustc.wade.collection;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.sync.ChangeReader.Change;

/**
 * @author wade
 * @version Nov 23, 2014 1:48:55 PM
 */
public class ParameterPassTest {

	public static void main(String[] args) {

		List list = null;

		System.out.println(list);
		change(list);
		
		System.out.println(list);
		

	}

	private static void change(List list) {

		list = new ArrayList<>();
		list.add(1);
	}

}
