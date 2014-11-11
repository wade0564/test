package wade.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class SSHTest2 {
	
	
	
	public static void main(String[] args) {
		
		String SFTPHOST = "10.62.96.97";
		int SFTPPORT = 22;
		String SFTPUSER = "sysadmin";
		String SFTPPASS = "abc123";
//		String SFTPPASS = "abc123";
		
		Session session = null;
		Channel channel =null;
		
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
		} catch (JSchException e) {
			System.out.println("Unavailable");
		}
		session.setPassword(SFTPPASS);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		try {
			session.connect();
			channel =  session.openChannel("exec");
			channel.connect();
			System.out.println("========");
		} catch (JSchException e) {
			e.printStackTrace();
			System.out.println("Incorrect password");
		}finally{
			channel.disconnect();
			session.disconnect();
		}
	}
	
	
	
	
	
}
