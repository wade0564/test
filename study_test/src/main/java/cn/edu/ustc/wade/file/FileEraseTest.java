package cn.edu.ustc.wade.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/** 
 * @author wade 
 * @version Nov 19, 2014 2:42:21 PM 
 */
public class FileEraseTest {
	
	
	public static void main(String[] args) throws IOException {
		
		
//		RandomAccessFile raf = new RandomAccessFile("C:/Users/zhangw4/Desktop/test.txt", "rw");
		
//		raf.setLength(1);
		
		removeFirstLine("C:/Users/zhangw4/Desktop/test.txt");
		
//		System.out.println(raf.length());
	}
	
	public static void removeFirstLine(String fileName) throws IOException {  
	    RandomAccessFile raf = new RandomAccessFile(fileName, "rw");          
	     //Initial write position                                             
	    long writePosition = raf.getFilePointer();                            
	    String text = raf.readLine();                                                       
	    // Shift the next lines upwards.                                      
	    long readPosition = raf.getFilePointer();                             

	    byte[] buff = new byte[1024];                                         
	    int n;                                                                
	    while (-1 != (n = raf.read(buff))) {                                  
	        raf.seek(writePosition);                                          
	        raf.write(buff, 0, n);                                            
	        readPosition += n;                                                
	        writePosition += n;                                               
	        raf.seek(readPosition);                                           
	    }                                                                     
	    raf.setLength(writePosition);                                         
	    raf.close();                                                          
	}   
	

}
