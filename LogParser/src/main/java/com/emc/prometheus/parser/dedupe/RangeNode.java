package com.emc.prometheus.parser.dedupe;


/**
 * @author wade
 * @version Nov 12, 2014 4:13:20 PM
 */
public class RangeNode {

	Long ts;

	String hash;

	Integer index;
	
	public RangeNode(Long ts, String hash, Integer index){
		this.ts = ts;
		this.hash = hash;
		this.index = index;
	}

	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

}
