/**
 * 
 */
package cn.edu.ustc.common.cifs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jiangl
 *
 */
public class ShellExecutor   {
	private static Logger logger = LoggerFactory.getLogger(ShellExecutor.class);
	private static Logger reporter = LoggerFactory.getLogger(ShellExecutor.class);
	private final File JOB_HOME = new File(System.getProperty("JOB_HOME"));

	private String[] commands;

	public String[] getCommands() {
		return commands;
	}

	public void setCommands(String[] commands) {
		this.commands = commands;
	}

	public void execute() throws Exception {
		if (commands == null) {
			logger.info("No shell commands to execute.");
			return;
		}
		if (!JOB_HOME.exists())
			throw new FileNotFoundException("JOB_HOME is not valid. " + JOB_HOME.getAbsolutePath());
		for (String cmd : commands) {
			String[] cmdArray = cmd.split("\\s+");
			ProcessBuilder pb = new ProcessBuilder(cmdArray);
			pb.directory(JOB_HOME);
			pb.redirectErrorStream(true);
			Process p = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = reader.readLine();
			StringBuilder sb = new StringBuilder("Shell: " + cmd);
			while (line != null) {
				sb.append("\n").append(line);
				line = reader.readLine();
			}
			String info = sb.toString();
			logger.info(info);
			String escapedInfo = StringEscapeUtils.escapeHtml(info);
			reporter.info("<pre>" + escapedInfo + "</pre>");

			sb = new StringBuilder();
			BufferedReader errorBr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String errorLine = errorBr.readLine();
			while (errorLine != null) {
				sb.append("\n").append(errorLine);
				errorLine = errorBr.readLine();
			}
			if (sb.length() > 0) {
				String error = sb.toString();
				logger.error(error);
				String escapedError = StringEscapeUtils.escapeHtml(error);
				reporter.error("<pre>" + escapedError + "</pre>");
			}
			p.waitFor();
		}
	}
}
