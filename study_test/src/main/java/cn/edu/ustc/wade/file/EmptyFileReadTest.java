package cn.edu.ustc.wade.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * @author wade
 * @version Dec 2, 2014 9:14:47 PM
 */
public class EmptyFileReadTest {

	public static void main(String[] args) throws IOException {

		String readFileToString = FileUtils.readFileToString(new File(
				"C:\\Users\\zhangw4\\Desktop\\empty.txt"));

		if (readFileToString == null) {
			System.out.println("null");
		} else {
			System.out.println(readFileToString.length());
		}

	}

}
