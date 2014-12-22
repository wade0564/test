package cn.edu.ustc.wade.collection.map;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class MapSortDemo {

	public static void main(String[] args) {

		Map<Date, String> map = new TreeMap<Date, String>();

		map.put(new Date(3), "kfc");
		map.put(new Date(2), "wnba");
		map.put(new Date(5), "nba");
		map.put(new Date(4), "cba");

//		Map<Date, String> resultMap = sortMapByKey(map);	//按Key进行排序

		for (Map.Entry<Date, String> entry : map.entrySet()) {
			System.out.println(entry.getKey().getTime() + " " + entry.getValue());
		}
	}
	
	/**
	 * 使用 Map按key进行排序
	 * @param map
	 * @return
	 */
	public static Map<Date, String> sortMapByKey(Map<Date, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}

		Map<Date, String> sortMap = new TreeMap<Date, String>();

		sortMap.putAll(map);

		return sortMap;
	}
}