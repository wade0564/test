package cn.edu.ustc.wade.generic;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/** 
 * @author wade 
 * @version Nov 21, 2014 11:59:24 PM 
 */
public class GenericTest<E> {
	
	    E createContents(Class<E> clazz) throws InstantiationException, IllegalAccessException
	    {
	        return clazz.newInstance();
	    }
	
		static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
		    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
		        new Comparator<Map.Entry<K,V>>() {
		            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
		                int res = e1.getValue().compareTo(e2.getValue());
		                return res != 0 ? res : 1;
		            }
		        }
		    );
		    sortedEntries.addAll(map.entrySet());
		    return sortedEntries;
		}
}
