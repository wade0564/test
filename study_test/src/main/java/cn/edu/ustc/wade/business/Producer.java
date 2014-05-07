package cn.edu.ustc.wade.business;

import java.io.IOException;

import cn.edu.ustc.wade.pojo.SubMessage;
import cn.edu.ustc.wade.util.Params;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
	private Channel channel;
	private Connection connection;

	private AMQP.BasicProperties props;
	Gson gson ;
	public Producer() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Params.RABBITMQ_HOSTNAME.getValue());
		props = new AMQP.BasicProperties.Builder().contentType(
				"application/json").build();
		connection = factory.newConnection();
		channel = connection.createChannel();
		gson= new Gson();
	}

	public void publish(SubMessage message) throws IOException {
		String queueName = Params.RABBITMQ_QUEUENAME.getValue();
		channel.queueDeclare(queueName, false, false, false, null);
		channel.basicPublish("", queueName, props, gson.toJson(message).getBytes());
		System.out.println(" [x] Sent '" + gson.toJson(message) + "'");
	}

	public void close() throws IOException {
		channel.close();
		connection.close();
	}

}
