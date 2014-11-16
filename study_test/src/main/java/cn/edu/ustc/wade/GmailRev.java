package cn.edu.ustc.wade;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
//利用POP3来读取邮件
//主要用来检测消息Message的基本信息，如发送者，收信者，时间

public class GmailRev {
	public static void main(String[] args) {
		Properties props = System.getProperties();
		String host = "outlook.corp.emc.com";
		String username = "zhangw4";
		String password = "Emc201312";
		String provider = "pop3";
		try {
			Session ss = Session.getDefaultInstance(props, null);
			Store store = ss.getStore(provider);
			store.connect(host, username, password);
			Folder inbox = store.getFolder("INBOX");
			if (inbox == null) {
				System.out.println("NO INBOX");
				System.exit(1);
			}
			
			// 打开文件夹，读取信息
			inbox.open(Folder.HOLDS_MESSAGES);
			System.out.println("TOTAL EMAIL:" + inbox.getMessageCount());
			System.out.println("NEW MESSAGE:"+inbox.getNewMessageCount());
			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			Message[] messages = inbox.search(unseenFlagTerm);
			
			// 获取邮件服务器中的邮件
//			 Message[] messages = inbox.getMessages();
			
			for (int i=messages.length-1; i>0; i--) {
				System.out.println("------------Message--" + (i + 1)
						+ "------------");
				System.out.println(messages[i].getFlags());
				messages[i].getContent();
				messages[i].setFlag(Flags.Flag.SEEN, true);
				// 解析地址为字符串
//				String from = InternetAddress.toString(messages[i].getFrom());
//				if (from != null) {
//					System.out.println("From:" + from);
//				}
//
//				String replyTo = InternetAddress.toString(messages[i]
//						.getReplyTo());
//				if (replyTo != null) {
//					System.out.println("Reply To" + replyTo);
//				}
//				String to = InternetAddress.toString(messages[i]
//						.getRecipients(Message.RecipientType.TO));
//				if (to != null) {
//					System.out.println("To:" + to);
//				}
//				String subject = messages[i].getSubject();
//				if (subject != null)
//					System.out.println("Subject:" + subject);
//				SimpleDateFormat sdf = new SimpleDateFormat(
//						"yyyy-MM-dd HH:mm:ss");
//				Date sent = messages[i].getSentDate();
//				if (sent != null)
//					System.out.println("Sent Date:" + sdf.format(sent));
//				Date ress = messages[i].getReceivedDate();
//				if (ress != null)
//					System.out.println("Receive Date:" + sdf.format(ress));
			}
			inbox.close(false);
			store.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}