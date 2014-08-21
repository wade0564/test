package com.emc.prometheus.asupqueuing.main;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.emc.prometheus.asupqueuing.business.Producer;
import com.emc.prometheus.asupqueuing.pojo.SubMessage;
import com.emc.prometheus.asupqueuing.util.Params;
import com.google.gson.Gson;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

public class TestProducer {
	public static void main(String[] args) throws IOException,
			ShutdownSignalException, ConsumerCancelledException,
			InterruptedException {
		System.out.println(Params.RABBITMQ_HOSTNAME.getValue());

		Producer p = new Producer();

		SubMessage msg = new SubMessage();
		msg.setDate(new Date());
		msg.setLocation("/auto/support/uploads/map3/ddr/bundle/upload.1-support-bundle.tar.gz");
		msg.setMD5("44ace6487b6eb15aae5b0e535e5b13f0");
		msg.setSize(532015485);
		for (int i = 0; i < 10; i++){
			msg.setMD5(i+"4ace6487b6eb15aae5b0e535e5b13f0");
			p.publish(msg);
		}

		p.close();
		// Consumer c=new Consumer();
		// for(String s:c.getMessageWithoutAck(10)){
		// System.out.println(s);
		// }

		// c.ackMessage();
		// c.close();

		// GPDao dao=GPDao.getInstance();
		// try {
		// ResultSet
		// rs=dao.executeQuery("select * from businessrules.bug_signature");
		// rs.next();
		// System.out.println(rs.getString(1));
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
