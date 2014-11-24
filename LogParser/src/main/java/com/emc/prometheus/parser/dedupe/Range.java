package com.emc.prometheus.parser.dedupe;

import java.io.Serializable;


/**
 * @author wade
 * @version Nov 12, 2014 4:41:03 PM
 */
public class Range implements Serializable {

	private static final long serialVersionUID = 7491964789055213027L;

	RangeNode start;

	RangeNode end;

	public Range(RangeNode start, RangeNode end){
		this.start = start;
		this.end = end;
	}
	
	public RangeNode getStart() {
		return start;
	}

	public void setStart(RangeNode start) {
		this.start = start;
	}

	public RangeNode getEnd() {
		return end;
	}

	public void setEnd(RangeNode end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "Range [start=" + start + ", end=" + end + "]";
	}
	
	

}
