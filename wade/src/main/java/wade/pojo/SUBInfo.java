package wade.pojo;


import java.math.BigInteger;


public class SUBInfo {
	
	public static final String CUSTOMER ="customer";
	public static final String INTERNAL ="internal";
	private BigInteger subid;
	private SUBStatus status;
	private String source;
	private String username;
	private String location;
	private String sn;
	private String channel;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	
	
	public BigInteger getSubid() {
		return subid;
	}
	public void setSubid(BigInteger subid) {
		this.subid = subid;
	}
	public SUBStatus getStatus() {
		return status;
	}
//	public void setStatus(SUBStatus status) {
//		this.status = status;
//	}
	
	public void setStatus(String statusStr) {
		for (SUBStatus status : SUBStatus.values()) {
			if(statusStr.equals(status.toString())){
				this.status=status;
			}
		}
	}
	@Override
	public String toString() {
		return "SUBInfo [subid=" + subid + ", status=" + status + ", source="
				+ source + ", username=" + username + ", location=" + location
				+ ", sn=" + sn + ", channel=" + channel + "]";
	}
	
	
	

}

