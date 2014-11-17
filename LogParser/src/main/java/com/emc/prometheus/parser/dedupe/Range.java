package com.emc.prometheus.parser.dedupe;


/**
 * @author wade
 * @version Nov 12, 2014 4:41:03 PM
 */
public class Range {

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

}
