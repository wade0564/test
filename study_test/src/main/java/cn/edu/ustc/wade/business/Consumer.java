package cn.edu.ustc.wade.business;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.ustc.wade.pojo.SubMessage;
import cn.edu.ustc.wade.util.Params;
import cn.edu.ustc.wade.util.PropertiesUtil;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Consumer {
	private static Logger logger = LoggerFactory.getLogger(Consumer.class);
	private Channel channel;
	private Connection connection;
	private Map<Long, String> deliveryMap = new HashMap<Long, String>();
	private MessageProcessor msgProcessor;
	private boolean isFirst = true;
	private QueueingConsumer consumer;

	public Consumer() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(PropertiesUtil.getProperty("rabbitmq_hostname"));
		 factory.setUsername(PropertiesUtil.getProperty("rabbitmq_username"));
		 factory.setPassword(PropertiesUtil.getProperty("rabbitmq_password"));
		 factory.setVirtualHost(PropertiesUtil.getProperty("rabbitmq_vhost"));
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getMessageWithoutAck(int maxCount)
			throws ShutdownSignalException, ConsumerCancelledException,
			IOException, InterruptedException {
		return this.getMessage(true, maxCount);
	}

	private int getMessage(boolean autoAck, int maxCount) throws IOException,
			ShutdownSignalException, ConsumerCancelledException,
			InterruptedException {
		channel.queueDeclare(Params.RABBITMQ_QUEUENAME.getValue(), true, false,
				false, null);

		consumer = new QueueingConsumer(channel);

		channel.basicConsume(Params.RABBITMQ_QUEUENAME.getValue(), autoAck,
				consumer);

		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		
//		SubMessage msg = new Gson().fromJson(new String(delivery.getBody()), SubMessage.class); 
		System.out.println(new String (delivery.getBody()));
//		System.out.println(new String(delivery.getBody()));
		
		return 1;
	}


	public void close() {
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
