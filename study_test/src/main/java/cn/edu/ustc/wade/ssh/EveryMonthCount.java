package cn.edu.ustc.wade.ssh;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/** 
 * @author wade 
 * @version Dec 5, 2014 11:22:05 PM 
 */
public class EveryMonthCount {
	
	
	public static void main(String[] args) throws IOException {
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("2013|Jan",0);
		map.put("2013|Feb",0);
		map.put("2013|Mar",0);
		map.put("2013|Apr",0);
		map.put("2013|May",0);
		map.put("2013|June",0);
		map.put("2013|July",0);
		map.put("2013|Aug",0);
		map.put("2013|Sep",0);
		map.put("2013|Oct",0);
		map.put("2013|Nov",0);
		map.put("2013|Dec",0);
		map.put("2014|Jan",0);
		map.put("2014|Feb",0);
		map.put("2014|Mar",0);
		map.put("2014|Apr",0);
		map.put("2014|May",0);
		map.put("2014|June",0);
		map.put("2014|July",0);
		map.put("2014|Aug",0);
		map.put("2014|Sep",0);
		map.put("2014|Oct",0);
		map.put("2014|Nov",0);
		map.put("2014|Dec",0);
		
		File file = new File("C:/Users/zhangw4/notes/data/output_2.dat");
		
		List<String> lines = Files.readLines(file, Charsets.UTF_8);
		
		for (String line : lines) {
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				if(line.contains(entry.getKey())){
					map.put(entry.getKey(), entry.getValue()+1);
				}
			}
		}
		

		int total=0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey()+":"+entry.getValue());
			total+= entry.getValue();
		}
		
		
		if(total==lines.size()){
			System.out.println("ok");
		}
	}

}
