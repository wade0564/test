package cn.edu.ustc.wade.file.path;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/** 
 * @author wade 
 * @version 2014年12月29日 上午9:31:11 
 */
public class PathTest01 {
	
	public static void main(String[] args) throws URISyntaxException {
		
		
//		URI uri = new URI("file:///粥五/下载");
		
				
		Path p3 = Paths.get("test");  
		
		System.out.println(p3.toAbsolutePath());
		
}
	
	

}
