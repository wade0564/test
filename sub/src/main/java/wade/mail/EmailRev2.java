package wade.mail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
//利用POP3来读取邮件
//主要用来检测消息Message的基本信息，如发送者，收信者，时间
import javax.mail.search.FromStringTerm;
import javax.mail.search.NotTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;

public class EmailRev2 {
	
	public static void main(String[] args) {
		Properties props = System.getProperties();
		String host = "outlook.corp.emc.com";
		String username = "zhangw4";
		String password = "Emc201412";
//		String provider = "pop3";
		String provider = "imaps";
		try {
			Session ss = Session.getDefaultInstance(props, null);
			ss.setDebug(true);
			Store store = ss.getStore(provider);
			store.connect(host, username, password);
			Folder inbox = store.getFolder("INBOX");
			if (inbox == null) {
				System.out.println("NO INBOX");
				System.exit(1);
			}
			
			// 打开文件夹，读取信息
			inbox.open(Folder.READ_WRITE);
			
			
			System.out.println("TOTAL EMAIL:" + inbox.getMessageCount());
			System.out.println("NEW MESSAGE:"+inbox.getNewMessageCount());
			Flags seen = new Flags(Flags.Flag.SEEN);
			
			Calendar calendar = Calendar.getInstance(); 
//			calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK - (Calendar.DAY_OF_WEEK - 1)) - 1); 
			calendar.add(Calendar.DATE, -1);
			Date mondayDate = calendar.getTime(); 
			System.out.println(mondayDate);
			SearchTerm comparisonTermGe = new SentDateTerm(ComparisonTerm.GE, mondayDate); 
			SearchTerm from =new FromStringTerm("Sara.Zhu@emc.com");
			NotTerm notSeen = new NotTerm(new FlagTerm(new Flags(Flags.Flag.SEEN), true));
			SearchTerm searchTerm =new AndTerm(from,notSeen);
//			SearchTerm searchTerm ;
			SearchTerm searchTermD =new AndTerm(from,new FlagTerm(new Flags(Flags.Flag.DELETED), true));
			SearchTerm	searchTerm2= new AndTerm(from,comparisonTermGe);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			Message[] messages = inbox.search(searchTerm2);
			
			// 获取邮件服务器中的邮件
//			 Message[] messages = inbox.getMessages();
			System.out.println("messages :"+ messages.length);
			for (int i=0; i<messages.length;i++) {
				System.out.println("------------Message--" + (i + 1)
						+ "------------");
				Message msg = messages[i];
				System.out.println(msg.getFrom()[0]);
				System.out.println(msg.getSentDate());
				msg.isSet(Flag.SEEN);
				msg.setFlag(Flag.SEEN,true);
//				msg.setFlag(Flag.SEEN, false);
//				msg.setFlag(Flag.SEEN, false);
			}
			inbox.close(false);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}