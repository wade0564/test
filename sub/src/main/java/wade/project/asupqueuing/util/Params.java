package wade.project.asupqueuing.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum Params {

	RABBITMQ_HOSTNAME("rabbitmq_hostname"),
	RABBITMQ_QUEUENAME("rabbitmq_queuename"),
	RABBITMQ_USERNAME("rabbitmq_username"),
	RABBITMQ_PASSWORD("rabbitmq_password"),
	RABBITMQ_CONSUME_TIMEOUT("rabbitmq_consume_timeout"),
	CONSUMER_FETCH_MINUTES("consumer_fetch_minutes"),
	CONUSMER_FETCH_AMOUNT("conusmer_fetch_amount"),
	GPDB_SERVER("gpdb_server"),
	GPDB_PORT("gpdb_port"),
	GPDB_DATABASE("gpdb_database"),
	GPDB_USERNAME("gpdb_username"),
	GPDB_PASSWORD("gpdb_password");

	public String param;
	private final static String CONFIG_PATH = "/asupqueuing.properties";
//	private final static String CONFIG_PATH = "/rabbitmqTest.properties";
	private static Properties props;
	static {
		try {
			props = new Properties();
			InputStream in = Params.class.getResourceAsStream(CONFIG_PATH);
			props.load(in);
			in.close();
		} catch (IOException e) {
		}
	}

	private Params(String param) {
		this.param = param;
	}

	public String getValue() {
		return props.getProperty(param);
	}
	
}
