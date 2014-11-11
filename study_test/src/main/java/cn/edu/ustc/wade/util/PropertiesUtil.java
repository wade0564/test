package cn.edu.ustc.wade.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {
	private static Logger logger = LoggerFactory
			.getLogger(PropertiesUtil.class);
	private static Properties properties = new Properties();

	private static final String PROPERTIES_FILE_NAME = "rabbitmq.properties";

	static {
		try {
			properties = PropertiesLoaderUtils
					.loadAllProperties(PROPERTIES_FILE_NAME);
		} catch (IOException e) {
			logger.error(" Load All Properties  error");
		}
	}

	public static String getProperty(String name) {
		String result = null;

		result = properties.getProperty(name);

		return result;

	}
}
