package com.emc.prometheus.parser.parse.regex;

import java.util.regex.Pattern;

public class RegExMajorSections {
	public static final Pattern General_Info = Pattern.compile("====++\\s++GENERAL\\s++INFO\\s++====++");
	public static final Pattern Server_Usage = Pattern.compile("====++\\s++SERVER\\s++USAGE\\s++====++");
	public static final Pattern General_Status = Pattern.compile("====++\\s++GENERAL\\s++STATUS\\s++====++");
	public static final Pattern Software_Configuration = Pattern.compile("====++\\s++SOFTWARE\\s++CONFIGURATION\\s++====++");
	public static final Pattern Hardware_Configuration = Pattern.compile("====++\\s++HARDWARE\\s++CONFIGURATION\\s++====++");
	public static final Pattern Detailed_Statistics = Pattern.compile("====++\\s++DETAILED\\s++STATISTICS\\s++====++");
	public static final Pattern Registry = Pattern.compile("====++\\s++REGISTRY\\s++====++");
	public static final Pattern Detailed_Filesystem = Pattern.compile("====++\\s++DETAILED\\s++FILESYSTEM\\s++LAYER\\s++====++");
	public static final Pattern Detailed_Storage = Pattern.compile("====++\\s++DETAILED\\s++STORAGE\\s++LAYER\\s++====++");
	public static final Pattern DDBoost = Pattern.compile("==++\\s*+(?:DDBO)?OST INFORMATION\\s*+==++");
	public static final Pattern System_Show_Performance = Pattern.compile("==++\\s*+SYSTEM SHOW PERFORMANCE\\s*+==++");
	public static final Pattern Detailed_Version = Pattern.compile("==++\\s*+DETAILED-VERSION\\s*+==++");
	public static final Pattern Archiver_Information = Pattern.compile("==++\\s*+ARCHIVER INFORMATION\\s*+==++");
}