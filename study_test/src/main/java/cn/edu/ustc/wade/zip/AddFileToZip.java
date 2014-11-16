package cn.edu.ustc.wade.zip;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFileToZip {

	public static void main(String[] args) throws Throwable {
		Map<String, String> env = new HashMap<>();
		env.put("create", "true");
		
		Path zipPath = Paths.get("C:/workspace/r_msgq/analytics/prometheus/Hermes-Application/components/agent/target/hermes-agent-test.zip");

		try (FileSystem zipfs = FileSystems.newFileSystem(zipPath,null)) {
//			Path externalTxtFile = Paths.get("C:/workspace/r_msgq/analytics/prometheus/Hermes-Application/components/agent/src/main/java/com/emc/prometheus/hermes/agent/FTPExecutor.java");
//			Path pathInZipfile = zipfs.getPath("/demo.xml");
			// copy a file into the zip file
//			Files.copy(externalTxtFile, pathInZipfile);
			
			Files.walkFileTree(zipfs.getPath("/"), new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					
					System.out.println(file.toAbsolutePath());
					return super.visitFile(file, attrs);
				}
				
				
			});
			
			Iterable<Path> rootDirectories = zipfs.getRootDirectories();
			
			Path logback = zipfs.getPath("/conf", "logback.xml");
			
			
			Files.write(zipfs.getPath("/lib", "job.properites"), "host=ddve1\nwade=shuai".getBytes(),StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
			
//			Files.mo
			
//			List<String> readAllLines = Files.readAllLines(logback, Charset.defaultCharset());
//			
//			
//			for (String string : readAllLines) {
//				System.out.println(string);
//			}
			
			
			
		}
	}

}
