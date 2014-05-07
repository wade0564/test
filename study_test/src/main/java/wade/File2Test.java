package wade;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class File2Test {
	public static void main(String[] args) throws Exception {
		File file =new File("C:/tmp/upload.1-support-bundle/ddr/var/log/debug/ddfs.info");
		
		
		FileChannel ch = new FileInputStream(file).getChannel();
		
//		ByteBuffer bb= new ByteBuffer()

//		MappedByteBuffer buffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
//				file.length());
		// CharBuffer cb = Charset.forName("UTF-8").decode(buffer);
//		String s = new String(buffer.array());
//		ch.close();
		
		
		FileChannel fChannel = new FileInputStream(file).getChannel();
	    byte[] barray = new byte[(int) file.length()];
	    ByteBuffer bb = ByteBuffer.wrap(barray);
//	    bb.order(ByteOrder.LITTLE_ENDIAN);
	    fChannel.read(bb);
	    System.out.println( new String (barray));
	    
		
	}

}
