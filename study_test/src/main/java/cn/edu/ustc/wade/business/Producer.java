package cn.edu.ustc.wade.business;

import java.io.IOException;

import cn.edu.ustc.wade.util.PropertiesUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
	private Channel channel;
	private Connection connection;

	public Producer() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(PropertiesUtil.getProperty("rabbitmq_hostname"));
//		System.out.println(PropertiesUtil.getProperty("rabbitmq_hostname"));
		factory.setUsername(PropertiesUtil.getProperty("rabbitmq_username"));
//		System.out.println(PropertiesUtil.getProperty("rabbitmq_username"));
		factory.setPassword(PropertiesUtil.getProperty("rabbitmq_password"));
//		System.out.println(PropertiesUtil.getProperty("rabbitmq_password"));
		factory.setVirtualHost("/hermes");
		connection = factory.newConnection();
		channel = connection.createChannel();
	}

	public void publish(String message) throws IOException {
		String queueName = PropertiesUtil.getProperty("rabbitmq_queuename");
		channel.queueDeclare(queueName, true, false, false, null);
		channel.basicPublish("", queueName, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
	}

	public void close() throws IOException {
		channel.close();
		connection.close();
	}

}
