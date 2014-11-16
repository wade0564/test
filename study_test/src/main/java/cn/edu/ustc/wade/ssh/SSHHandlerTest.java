package cn.edu.ustc.wade.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHHandlerTest {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private final Integer DEFAULT_TIMEOUT = 2000;
	
	private String ip;
	private Integer port;
	private String username;
	private String password;
	private String command;
	private Integer timeout;
	boolean output;	
	private boolean booleanResult;
	private String textResult;

	
	public void init(String ip, Integer port, String usrName, String pswd, String cmd, Integer timeout) {
		this.ip = ip;
		this.port = port;
		this.username = usrName;
		this.password = pswd;
		this.command = cmd;
		this.timeout= timeout;
		this.booleanResult = false;
		this.textResult = "";		
	}
		
	public boolean isOK() {
		return booleanResult;
	}
	
	public String getResult() {
		return textResult;
	}
	
	public void execute() {
		logger.info(String.format("Execute shell command on %s:%s", ip, command));
		
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
              
            channel = session.openChannel("exec");  
            channel.setInputStream(null);
            ((ChannelExec)channel).setCommand(command);  
            InputStream in = channel.getInputStream();  
            channel.connect();  
            
            while (output) {
            	while(in.available()>0) { 
            		int i=in.read();
                    if(i>=0)
                    	textResult += ((char)i); 
                    else
                    	break;
            	}
            	
            	if (channel.isClosed() || channel.getExitStatus() == 0) {
            		break;
            	}
            	
                try{
                	Thread.sleep(100);
                }catch(Exception e){
                	
                }
            }
            
           	booleanResult = true;
           	
        } catch (JSchException e) {  
            logger.error(String.format("Exception (JSchException) on executing shell command on node %s: %s", ip, command));
            logger.error(e.getMessage(), e);
            booleanResult = false;
            textResult = e.getMessage();
    		
        } catch (IOException e) {  
            logger.error(String.format("Exception (IOException) on executing shell command on node %s: %s", ip, command));
            logger.error(e.getMessage(), e);
            booleanResult = false;
            textResult = e.getMessage();
            
        } finally {
        	if (channel != null)
        		channel.disconnect();
        	if (session != null) 
        		session.disconnect();
        }
    	
	}
}
