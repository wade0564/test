package cn.edu.ustc.wade.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostName {
	
	
	
	public static void main(String[] args) throws UnknownHostException {
		
		
		System.out.println(InetAddress.getLocalHost().getHostName());
		
	}
	
	

}
