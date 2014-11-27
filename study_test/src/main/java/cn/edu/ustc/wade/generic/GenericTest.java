package cn.edu.ustc.wade.generic;
/** 
 * @author wade 
 * @version Nov 21, 2014 11:59:24 PM 
 */
public class GenericTest<E> {
	
	    E createContents(Class<E> clazz) throws InstantiationException, IllegalAccessException
	    {
	        return clazz.newInstance();
	    }
	

}
