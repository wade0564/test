/**
 * 
 */
package cn.edu.ustc.common.cifs;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author wade
 *
 */
public class SSHExecutor {
	private static Logger logger = LoggerFactory.getLogger(SSHExecutor.class);
	//	private static Logger reporter = LoggerFactory
	//	        .getLogger(JobLogger.JOB_REPORT_NAME);
	//	private final File JOB_HOME = new File(System.getProperty("JOB_HOME"));
	private final int DEFAULT_TIMEOUT = 3600000;

	private String ip;
	private Integer port;
	private String username;
	private String password;
	private int timeout = DEFAULT_TIMEOUT;
	private Boolean isOutput = false;
	protected String[] commands;
	protected String outputResult;
	protected Path redirectPath;

	public String[] getCommands() {
		return commands;
	}

	public void setCommands(String[] commands) {
		this.commands = commands;
	}

	public void execute() throws Exception {
		String commandlines = StringUtils.join(commands, "; \n");
		logger.info(String.format("Execute shell command on %s:\n%s", ip, commandlines));

		JSch jsch = new JSch();
		Session session = null;
		Channel channel = null;
		try {
			session = jsch.getSession(username, ip, port);
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
	
	public boolean isAvailable() throws JSchException{
		JSch jsch = new JSch();
		Session session = null;
		boolean isAvailable = false;
		try {
			session = jsch.getSession(username, ip, port);
			session.setPassword(password);
			session.setTimeout(timeout);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			isAvailable = true;
		}finally{
			if (session != null)
			 session.disconnect();
		}
		
		return isAvailable;
	}

	protected Channel process(Session session) throws Exception {
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setInputStream(null);
		channel.setCommand(StringUtils.join(commands, "; \n"));
		InputStream in = channel.getInputStream();
		channel.connect();
		
		if(isOutput){
			outputResult = IOUtils.toString(in);
		}else{
			Files.copy(in, redirectPath, StandardCopyOption.REPLACE_EXISTING);
		}
		
		return channel;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
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

	public Path getRedirectPath() {
		return redirectPath;
	}

	public void setRedirectPath(String redirectPath) {
		this.redirectPath = Paths.get(redirectPath);
	}

	public void setRedirectPath(Path redirectPath) {
		this.redirectPath = redirectPath;
	}

	public String getOutputResult() {
		return outputResult;
	}

	public Boolean getIsOutput() {
		return isOutput;
	}

	public void setIsOutput(Boolean isOutput) {
		this.isOutput = isOutput;
	}
	
	

}
