

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.NotTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;


public class EmailRevWade {
	
	public static void main(String[] args) {
		Properties props = System.getProperties();
		String username = "zhangw4";
		String password ="Emc201412";
		String provider = "imaps";
		String host = "outlook.corp.emc.com";
		try {
			Session ss = Session.getInstance(props, null);
//			ss.setDebug(true);
			Store store = ss.getStore(provider);
			store.connect(host, username, password);
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			Calendar calendar = Calendar.getInstance(); 
			calendar.set(Calendar.YEAR, 2014);
			calendar.set(Calendar.MONTH, 3);
			calendar.set(Calendar.DAY_OF_MONTH, 23);
			calendar.set(Calendar.HOUR_OF_DAY,13);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			Date mondayDate = calendar.getTime();
			System.out.println(mondayDate);
			SearchTerm comparisonTermGe = new SentDateTerm(ComparisonTerm.GE, mondayDate); 
			
			
			inbox.search(comparisonTermGe);
			System.out.println("TOTAL EMAIL:" + inbox.getMessageCount());
			System.out.println("NEW MESSAGE:"+inbox.getNewMessageCount());
			
			NotTerm notSeen = new NotTerm(new FlagTerm(new Flags(Flags.Flag.SEEN), true));
			Message[] messages = inbox.search(comparisonTermGe);
			
			System.out.println("messages :"+ messages.length);
			
			System.out.println(messages[0].getReceivedDate());
			
			inbox.close(false);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}