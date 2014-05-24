
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.NotTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;

import com.sun.mail.pop3.POP3Folder;

public class EmailRev2 {

	public static void main(String[] args) throws Exception {
		Properties props = System.getProperties();

		String password = "L9Cde!23Szaxc#23ske38nfsdD2";
		String username = "corp/svc_dpadctosh/PDI.SUB.Parser@emc.com";
		// String username = "corp/svc_dpadctosh";
//		String host = "CORPUSMX60C.corp.emc.com";
		String host = "mx26a.corp.emc.com";
		// String host = "email.emc.com";
		String provider = "pop3";
//		 String provider = "imap";
		Session ss = Session.getInstance(props, null);
		 ss.setDebug(true);
		Store store = ss.getStore(provider);
		// store.connect(host, username, password);
		store.connect(host, username, 
				password);
		// store.connect(host, "svc_dpadctosh@emc.com", password);
//		POP3Folder inbox = (POP3Folder) store.getFolder("INBOX");
		 Folder inbox =   store.getFolder("INBOX");

		// 打开文件夹，读取信息
		inbox.open(Folder.READ_WRITE);

		FetchProfile profile = new FetchProfile();
		profile.add(UIDFolder.FetchProfileItem.UID);
		System.out.println("TOTAL EMAIL:" + inbox.getMessageCount());
		long start = System.currentTimeMillis();
		Message[] messages = inbox.getMessages();
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000 + "s");

		start = System.currentTimeMillis();
//		for (int i = 0; i < messages.length; i++) {
//			System.out.println(inbox.getUID(messages[i]));
//		}
		end = System.currentTimeMillis();
		System.out.println((end - start) / 1000 + "s");
		Calendar calendar = Calendar.getInstance();
		// calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK
		// - (Calendar.DAY_OF_WEEK - 1)) - 1);
		// calendar.add(Calendar.DATE, -5);
		calendar.set(Calendar.YEAR, 2014);
		calendar.set(Calendar.MONTH, 3);
		calendar.set(Calendar.DAY_OF_MONTH, 23);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date mondayDate = calendar.getTime();
		System.out.println(mondayDate);
		SearchTerm comparisonTermGe = new SentDateTerm(ComparisonTerm.GE,
				mondayDate);
		SearchTerm from = new FromStringTerm("Sara.Zhu@emc.com");
		start = System.currentTimeMillis();
		// Message[] messages = inbox.search(comparisonTermGe);
		end = System.currentTimeMillis();
		System.out.println((end - start) / 1000);

		System.out.println("messages :" + messages.length);
		System.out.println(messages[0].getSentDate());
		System.err.println(messages[0].getReceivedDate());
	}

	private static void printAllProperties() {
		Properties properties = System.getProperties();
		Enumeration<Object> keys = properties.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			String value = System.getProperty(key);
			System.out.println(String.format("%s: %s", key, value));
		}
	}
}