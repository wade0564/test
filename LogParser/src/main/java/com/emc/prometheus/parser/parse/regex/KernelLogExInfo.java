package com.emc.prometheus.parser.parse.regex;



import java.util.regex.Pattern;


public class KernelLogExInfo {
	
	public static final Pattern KernelErrorstart = Pattern.compile("====++\\s++KERNEL ERRORS\\s++====++");
	
	public static final Pattern KernelErrorend = Pattern.compile("====+\\s");
	
	//public static final Pattern KernelErrorend = Pattern.compile("====++\\s++DIAGNOSTICS LOG\\s++====++");
	
	public final static Pattern Generated_key = Pattern.compile("^GENERATED: \\d{4}-\\d{2}-\\d{2}+\\s+\\d{2}:\\d{2}:\\d{2}");
	public final static Pattern Generated = Pattern.compile("^GENERATED: (\\d{4}-\\d{2}-\\d{2}+\\s+\\d{2}:\\d{2}:\\d{2})");

	public final static Pattern anylog_key = Pattern.compile("^\\w{3}\\s+\\d+\\s+\\d{2}:\\d{2}:\\d{2}");
	
	
	public final static Pattern anylog_data = Pattern.compile("^([^=].*+)");
	
	public static final Pattern KernelInfostart = Pattern.compile("====++\\s++KERN.INFO\\s++====++");
	
	public static final Pattern KernelInfoend = Pattern.compile("====+\\s");
}
