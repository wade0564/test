
import java.io.File;
import javax.mail.Flags;
import javax.mail.Store;
import javax.mail.Folder;
import java.util.Iterator;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.URLName;
import java.io.IOException;
import javax.mail.UIDFolder;
import javax.mail.FetchProfile;
import com.sun.mail.pop3.POP3Folder;
import javax.mail.MessagingException;

public class Run
{
   public static final void main(String[] parameters)
   {
      try
      {
         efficient(parameters);
      }
      catch(MessagingException x)
      {
         System.out.println(x.getMessage());
      }
      catch(IOException x)
      {
         System.out.println(x.getMessage());
      }
   }

   public static final void efficient(String[] parameters)
      throws MessagingException, IOException
   {
      if(parameters.length < 3)
      {
         System.err.println("Parameters: host user password");
         return;
      }
      File file = new File("uids.txt");
      UIDStore uids = new UIDStore();
      uids.load(file);
      URLName url = new URLName("pop3",
                                parameters[0],
                                110,
                                "",
                                parameters[1],
                                parameters[2]);
      Session session = Session.getDefaultInstance(System.getProperties(),null);
      Store store = session.getStore(url);
      store.connect();
      POP3Folder inbox = (POP3Folder)store.getFolder("INBOX");
      inbox.open(Folder.READ_WRITE);
      FetchProfile profile = new FetchProfile();
      profile.add(UIDFolder.FetchProfileItem.UID);
      Message[] messages = inbox.getMessages();
      inbox.fetch(messages,profile);
      for(int i = 0;i < messages.length;i++)
      {
         String uid = inbox.getUID(messages[i]);
         if(uids.isNew(uid))
         {
            System.out.print(i);
            System.out.print(". ");
            System.out.println(inbox.getMessage(i + 1).getSubject());
         }
      }
      System.out.println("Done.");
      uids.store(file);
   }
}