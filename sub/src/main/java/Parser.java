import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	public static void main(String[] args) throws Exception {
		long t1 = System.currentTimeMillis();
		File file = new File("/data1/primary/staging/external_file_test/ddfs");
//		File file = new File("C:/temp_external/ddfs.info.6");
		LinkedList<String> lineList = new LinkedList<String>();

		FileInputStream fis = new FileInputStream(file);
		FileChannel fChannel = fis.getChannel();
		byte[] barray = new byte[(int) file.length()];
		ByteBuffer bb = ByteBuffer.wrap(barray);
		fChannel.read(bb);
		String s = new String(barray);
		fis.close();
		fChannel.close();

		String regex = "ddr_stated: Heartbeat timeout; restarting.";
		Pattern p = Pattern.compile(".*\\|.*\\|.*\\|(.*)\n");
		Matcher m = null;
		m = p.matcher(s);
		int i=0;
		while(m.find()){
			System.out.println(i++);
		}
		
		long t2 = System.currentTimeMillis();
		System.out.println("externalFilePrescan : " + (t2 - t1) + "ms");
	}

}
