
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.pop3.POP3Folder;

public class EmailRev2 {
	
	private final static Logger  log = LoggerFactory.getLogger(EmailRev2.class);

	public static void main(String[] args) throws Exception {
		Properties props = System.getProperties();

		String password = "L9Cde!23Szaxc#23ske38nfsdD2";
		String NT="svc_dpadctosh";
		String [] uernames ={"corp/svc_dpadctosh/PDI.SUB.Parser@emc.com","corp/svc_dpadctosh","svc_dpadctosh/PDI.SUB.Parser@emc.com","PDI.SUB.Parser@emc.com","svc_dpadctosh"};
		String [] providers ={"pop3","pop3s","imap","imaps"};
		
//		String host = "mx26a.corp.emc.com";
//		String host = "email.emc.com";
		String host = "MX05A.corp.emc.com";
		Session ss = Session.getInstance(props, null);
		 ss.setDebug(true);
		 Store store=null;
		for(int j=0;j<providers.length;j++){
				for(int i=0;i<uernames.length;i++){
				store = ss.getStore(providers[j]);
				try {
					store.connect(host, uernames[i], password);
					log.info("successful :"+"\n"+host+"\n"+ uernames[i]+"\n"+ password);
				} catch (Exception e) {
					log.error(e.getMessage(),e);
					log.error("\n"+host+"\n"+ uernames[i]+"\n" +password+"\n"+providers[j]);
					Thread.sleep(1000);
				}
			}
		}
		
		// store.connect(host, "svc_dpadctosh@emc.com", password);
		POP3Folder inbox = (POP3Folder) store.getFolder("INBOX");
		// 打开文件夹，读取信息
		inbox.open(Folder.READ_WRITE);

		FetchProfile profile = new FetchProfile();
		profile.add(UIDFolder.FetchProfileItem.UID);
		System.out.println("TOTAL EMAIL:" + inbox.getMessageCount());
		long start = System.currentTimeMillis();
		Message[] messages = inbox.getMessages();
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000 + "s");

	}
}