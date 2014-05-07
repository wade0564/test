package com.emc.prometheus.asupqueuing.main;

import java.io.IOException;

import com.emc.prometheus.asupqueuing.business.Consumer;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

public class TestConsumer {
	public static void main(String[] args) throws IOException,
			ShutdownSignalException, ConsumerCancelledException,
			InterruptedException {

		Consumer consumer = new Consumer();
		consumer.getMessageWithoutAck(10);
		
		consumer.close();

	}

}