import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Flags.Flag;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;

import com.sun.mail.pop3.POP3Folder;

public class EmailRev3 {

	public static void main(String[] args) throws Exception {
		Properties props = System.getProperties();

		String password = "L9Cde!23Szaxc#23ske38nfsdD2";
//		 String username = "svc_dpadctosh";
		String username = "corp/svc_dpadctosh/PDI.SUB.Parser@emc.com";
		// String username = "corp/svc_dpadctosh/PDI.SUB.Parser@emc.com";
		// String username = "corp/svc_dpadctosh/PDI.SUB.Parser@emc.com";
		// String username = "svc_dpadctosh";
		// String username = "corp/svc_dpadctosh";
		String host = "CORPUSMX60C.corp.emc.com";
//		String host = "email.emc.com";

		// String host = "mx26a.corp.emc.com";
		// String host = "email.emc.com";
		String provider = "pop3";
//		 String provider = "imaps";
		Session ss = Session.getInstance(props, null);
		ss.setDebug(true);
		Store store = ss.getStore(provider);
		store.connect(host, username, password);

		
		Calendar calendar = Calendar.getInstance();
		// calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK
		// - (Calendar.DAY_OF_WEEK - 1)) - 1);
		// calendar.add(Calendar.DATE, -5);
		calendar.set(Calendar.YEAR, 2014);
		calendar.set(Calendar.MONTH, 4);
		calendar.set(Calendar.DAY_OF_MONTH, 26);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date mondayDate = calendar.getTime();
		System.out.println(mondayDate);
		SearchTerm comparisonTermGe = new SentDateTerm(ComparisonTerm.GE,
				mondayDate);
		
		
		// store.connect(host, "svc_dpadctosh@emc.com", password);
		POP3Folder inbox = (POP3Folder) store.getFolder("INBOX");
//		Folder inbox =  store.getFolder("INBOX");

		// 打开文件夹，读取信息
		inbox.open(Folder.READ_WRITE);
		SearchTerm	from= new FromStringTerm("Wade.Zhang@emc.com");
		Message[] messages =inbox.search(comparisonTermGe);
		FetchProfile profile = new FetchProfile();
		profile.add(UIDFolder.FetchProfileItem.UID);
		System.out.println("TOTAL EMAIL:" + messages.length);
		
//		Message[] messages = inbox.getMessages();
		inbox.fetch(messages,profile);

		BufferedWriter bw = new BufferedWriter( new FileWriter( new File("c:/tmp/email.txt")));
		for (int i = 0; i < messages.length; i++) {
			
			System.out.println(messages[i].getSubject());
			
//			if(messages[i].isMimeType("text/*")){
//				bw.write((String) messages[i].getContent());
//			}
//			System.out.println(inbox.getUID(messages[i]).equalsIgnoreCase("AAwC1D9zSAgScsowi6/3AOko/ERAolWo"));
		}
		
		inbox.close(true);
		
		bw.close();
		
	}
}