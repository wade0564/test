package cn.edu.ustc.wade.ssh;


public class SSHTest {
	
	
	
	public static void main(String[] args) {
		
		SSHHandlerTest ssh = new SSHHandlerTest();
		
//		ssh.init("10.62.96.95", 22, "sysadmin", "abc123", "autosupport show report", 2000);
		ssh.init("10.62.96.95", 22, "sysadmin", "abc123", "autosupport show report", 2000);
//		ssh.init("10.62.96.95", 22, "root", "abc123", "hostname", 2000);
		ssh.output=true;
		ssh.execute();
		System.out.println("Result:\n"+ssh.getResult());
		
	}
	
	
	
	
	
}
