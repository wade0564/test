package cn.edu.ustc.wade.file.path;

import java.nio.file.Path;
import java.nio.file.Paths;

/** 
 * @author wade 
 * @version 2014年12月29日 上午9:31:11 
 */
public class PathTest {
	
	public static void main(String[] args) {
		
		Path path = Paths.get("C:\\Program Files\\Java\\jdk1.8.0_20\\src.zip\\com\\sun\\java\\swing\\plaf\\gtk");    // Microsoft Windows syntax     
//		//Path path = Paths.get("/home/joe/foo");    // Solaris syntax     
		System.out.format("toString: %s%n", path.toString());     
//		System.out.format("getName: %s%n", path.get);     
		System.out.format("getName(0): %s%n", path.getName(0));     
		System.out.format("getNameCount: %d%n", path.getNameCount());     
//		System.out.format("subpath(0,2): %d%n", path.subpath(0,2));     
		System.out.format("subpath(0,2): %s%n", path.subpath(0,2));     
		System.out.format("getParent: %s%n", path.getParent());     
		System.out.format("getRoot: %s%n", path.getRoot());     
//		
//		FileSystem fileSystem = FileSystems.getDefault();
//		System.out.println("directories:"+ fileSystem.getRootDirectories().toString());
//		System.out.println("provider:"+fileSystem.provider().getScheme());
		
		
	}
	
	

}
