package cn.edu.ustc.wade.business;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ParamConfig {
	private final static String CONFIG_PATH="rabbitmq.properties";
	private static Properties props;
	
	static{
		try {
			props = new Properties();
			InputStream in=new BufferedInputStream(new FileInputStream(CONFIG_PATH));
			props.load(in);
			in.close();
		} catch (IOException e) {
		}
	}
	
//	public static String getProperty()
	
	public static String getProperty(String key){
		System.out.println(props.getProperty(key));
		return props.getProperty(key);
	}
}
