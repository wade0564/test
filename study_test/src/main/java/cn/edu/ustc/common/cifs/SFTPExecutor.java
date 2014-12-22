/**
 * 
 */
package cn.edu.ustc.common.cifs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

/**
 * @author jiangl
 *
 */
public class SFTPExecutor  {
	private static final Logger logger = LoggerFactory.getLogger(SFTPExecutor.class);

	private final int DEFAULT_TIMEOUT = 2000;
	private final int DEFAULT_PORT = 22;

	public static enum Command {
		GET, PUT
	}

	private String host;
	private int port = DEFAULT_PORT;
	private String username;
	private String password;
	private int timeout = DEFAULT_TIMEOUT;
	private Command command;
	private String[] sourcePaths;
	private String destinationPath;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public String[] getSourcePaths() {
		return sourcePaths;
	}

	public void setSourcePaths(String[] sourcePaths) {
		this.sourcePaths = sourcePaths;
	}

	public String getDestinationPath() {
		return destinationPath;
	}

	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}

	private Channel process(Session session) throws Exception {
		ChannelSftp channel;
		channel = (ChannelSftp) session.openChannel("sftp");
		channel.connect();
		for (String srcPath : sourcePaths) {
			if (command == Command.PUT) {
				File srcFile = new File(srcPath);
				FileInputStream fis = new FileInputStream(srcFile);
				channel.put(fis, destinationPath + File.separator + srcFile.getName());
				fis.close();
				logger.info("Uploaded [{}] to [{}]", srcFile.getName(), destinationPath);
			} else if (command == Command.GET) {
				String remoteDir = "";
				int p = srcPath.lastIndexOf("/");
				if (p >= 0) {
					remoteDir = srcPath.substring(0, p);
				}
				List<SFTPFile> files = getRemoteFilePaths(channel, srcPath);
				for (SFTPFile f : files) {
					FileOutputStream fos = new FileOutputStream(new File(destinationPath + File.separator + f.name));
					String src = remoteDir + "/" + f.name;
					channel.get(src, fos);
					fos.close();
					logger.info("Downloaded [{}] to [{}]", src, destinationPath);
				}
			} else {
				logger.error("sFTP command {} is not supported.", command);
				break;
			}
		}
		return channel;
	}

	public void execute() throws Exception {
		JSch jsch = new JSch();
		Session session = null;
		Channel channel = null;

		try {
			session = jsch.getSession(username, host, port);
			session.setPassword(password);
			session.setTimeout(timeout);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = process(session);
		} finally {
			if (channel != null)
				channel.disconnect();
			if (session != null)
				session.disconnect();
		}
	}

	private List<SFTPFile> getRemoteFilePaths(ChannelSftp channel, String path) throws Exception {
		List<SFTPFile> files = null;
		List<?> lsEntries = channel.ls(path);
		if (lsEntries != null) {
			files = new ArrayList<SFTPFile>();
			for (int i = 0; i < lsEntries.size(); i++) {
				LsEntry next = (LsEntry) lsEntries.get(i);
				SFTPFile sftpFile = new SFTPFile(next);
				if (sftpFile.isFile()) {
					files.add(sftpFile);
				}
			}
		}
		return files;
	}

	private class SFTPFile {
		String path;
		String name;
		SftpATTRS sftpAttributes;

		public SFTPFile(LsEntry lsEntry) {
			this.sftpAttributes = lsEntry.getAttrs();
			this.path = lsEntry.getLongname();
			this.name = lsEntry.getFilename();
		}

		public boolean isFile() {
			return (!sftpAttributes.isDir() && !sftpAttributes.isLink());
		}
	}

}
