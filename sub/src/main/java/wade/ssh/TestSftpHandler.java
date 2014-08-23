package wade.ssh;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

public class TestSftpHandler {
	
	public void testSftp() throws JSchException{
		UserInfo userInfo =null;
		SftpHandler jhandler = new SftpHandler("username","hostname",22,userInfo);
		try {
			jhandler.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pwd = null;
		try {
			pwd = jhandler.pwd();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(pwd);
//		jhandler.destory();
	}

}
