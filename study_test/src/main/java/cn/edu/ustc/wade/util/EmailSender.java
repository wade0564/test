package cn.edu.ustc.wade.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//利用POP3来读取邮件
//主要用来检测消息Message的基本信息，如发送者，收信者，时间
public class EmailSender {
	public static void main(String[] args) {
		final String username = "wade0564@gmail.com";
		final String password = "zhang667";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "emcmail.lss.emc.com");
		props.put("mail.smtp.port", "25");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		 session.setDebug(true);
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("Wade.Zhang@emc.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("Wade.Zhang@emc.com"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
					+ "\n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
