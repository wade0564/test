import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;

import com.sun.mail.pop3.POP3Folder;

public class EmailRev3Wade {

	public static void main(String[] args) throws Exception {
		Properties props = System.getProperties();
		String password ="Emc201412";

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		String username = "zhangw4";
		String provider = "pop3s";
		String host = "email.emc.com";
		// String provider = "imaps";
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
		Message[] messages =inbox.search(from);
		FetchProfile profile = new FetchProfile();
		profile.add(UIDFolder.FetchProfileItem.UID);
		System.out.println("TOTAL EMAIL:" + messages.length);
		
//		Message[] messages = inbox.getMessages();
		inbox.fetch(messages,profile);

		BufferedWriter bw = new BufferedWriter( new FileWriter( new File("c:/tmp/email.txt")));
		for (int i = 0; i < messages.length; i++) {
			
			System.out.println(messages[i].getSubject());
			
			messages[i].setFlag(Flags.Flag.DELETED, true);
//			if(messages[i].isMimeType("text/*")){
//				bw.write((String) messages[i].getContent());
//			}
//			System.out.println(inbox.getUID(messages[i]).equalsIgnoreCase("AAwC1D9zSAgScsowi6/3AOko/ERAolWo"));
		}
		
		inbox.close(true);
		
		bw.close();
		
	}
}