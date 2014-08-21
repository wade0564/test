package com.emc.prometheus.asupqueuing.util;

import java.util.Properties;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class Email163 {
	
	
	public static void main(String[] args) throws NoSuchProviderException {
		Properties props = new Properties();
		//存储接收邮件服务器使用的协议，这里以POP3为例
		props.setProperty("mail.store.protocol", "pop3");
		//设置接收邮件服务器的地址，这里还是以网易163为例
		props.setProperty("mail.pop3.host", "pop3.163.com");
		//根据属性新建一个邮件会话.
		Session session=Session.getInstance(props);
		//从会话对象中获得POP3协议的Store对象
		Store store = session.getStore("pop3");
		//如果需要查看接收邮件的详细信息，需要设置Debug标志
		session.setDebug(false);
	}
	

}
