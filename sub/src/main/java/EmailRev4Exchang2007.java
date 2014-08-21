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

public class EmailRev4Exchang2007 {

	public static void main(String[] args) throws Exception {
		Properties props = System.getProperties();

		String password = "L9Cde!23Szaxc#23ske38nfsdD2";
//		 String username = "svc_dpadctosh";
		String username = "corp/svc_dpadctosh/PDI.SUB.Parser";
		String host = "email.emc.com";
		String provider = "pop3s";
//		 String provider = "imaps";
		Session ss = Session.getInstance(props, null);
		ss.setDebug(true);
		Store store = ss.getStore(provider);
		store.connect(host, username, password);

		
		// store.connect(host, "svc_dpadctosh@emc.com", password);
		POP3Folder inbox = (POP3Folder) store.getFolder("INBOX");
//		Folder inbox =  store.getFolder("INBOX");

		// 打开文件夹，读取信息
		inbox.open(Folder.READ_WRITE);
		SearchTerm	from= new FromStringTerm("Wade.Zhang@emc.com");
		Message[] messages =inbox.getMessages();
		FetchProfile profile = new FetchProfile();
		profile.add(UIDFolder.FetchProfileItem.UID);
		profile.add(FetchProfile.Item.ENVELOPE);
		System.out.println("TOTAL EMAIL:" + messages.length);
		
//		Message[] messages = inbox.getMessages();
		inbox.fetch(messages,profile);

//		BufferedWriter bw = new BufferedWriter( new FileWriter( new File("c:/tmp/email.txt")));
		for (int i = 0; i < messages.length; i++) {
			System.out.println(messages[i].getSentDate());
			
		}
		
		inbox.close(true);
		
//		bw.close();
		
	}
}