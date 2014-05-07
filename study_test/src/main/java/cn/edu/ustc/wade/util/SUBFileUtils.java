package cn.edu.ustc.wade.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SUBFileUtils {

	public static List<File> getSUBFiles(String url) throws IOException {

		// File archivedFile= copyFile(url);
		//
		// String directory =unzipFile(archivedFile);

		// File tmpFile =new File("src/main/resources/autosupport");
		
		
		File tmpFile = new File("/home/dev/tmp/autosupport");

		ArrayList<File> files = new ArrayList<File>();
		files.add(tmpFile);
		return files;
	}

	private static File copyFile(String url) {

		return null;
	}

	private static String unzipFile(File file) {
		return null;
	}

	private static boolean isSUBFile(File file) {

		return false;
	}

}
