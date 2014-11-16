package cn.edu.ustc.wade.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SftpHandlerTest {

	public static void main(String[] args) throws Exception {
		String SFTPHOST = "10.32.105.205";
		int SFTPPORT = 22;
		String SFTPUSER = "hermes";
		String SFTPPASS = "abc123";
		String SFTPWORKINGDIR = "DD/";

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		JSch jsch = new JSch();
		session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
		session.setPassword(SFTPPASS);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		channel = session.openChannel("sftp");
		channel.connect();
		channelSftp = (ChannelSftp) channel;
		Vector dirs = channelSftp.ls("DD/");
		
		for (Object object : dirs) {

			System.out.println(object.toString());
		}
		channelSftp.cd(SFTPWORKINGDIR);
		File f = new File("c:/tmp/init.pp");
		
		channelSftp.put(new FileInputStream(f), f.getName());
		
		channelSftp.disconnect();
		session.disconnect();
	}
}
