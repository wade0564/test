package com.emc.prometheus.parser.parse.regex;

import java.util.regex.Pattern;

public class VtlInfoParserRegExInfo {

	public final static String VtlStart_Reg= "==*\\s*VTL\\.INFO\\s*==*";
	public final static Pattern VtlStart = Pattern.compile(VtlStart_Reg);
	
//	public final static String VtlEnd_Reg= "==*\\s*CLIENTS\\.LOG\\s*==*";
	public final static String VtlEnd_Reg= "====";
	public final static Pattern VtlEnd = Pattern.compile(VtlEnd_Reg);
	
	//07/19 05:08:02.768 (tid 1253452128)
	public final static String Msg_Reg = "(^\\s*(\\d{2}/\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3})\\s+\\(tid.*)";
	public final static Pattern Msg_Reg_Pattern = Pattern.compile(Msg_Reg);
	
	public final static String Everything_Reg = "(.*)";
	public final static Pattern Everything_Reg_Pattern = Pattern.compile(Everything_Reg);
	
	
//	public final static String Msg_Reg_Sub = "^(?!\\s*\\d{2}/\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}\\s+\\(tid)(.*)";
//	public final static String Msg_Reg_Sub = "(^(?!\\s*\\d{2}/\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}\\s+\\(tid)(.*))^(?!==*\\s*CLIENTS\\.LOG\\s*==*)(.*)";
//	public final static String Msg_Reg_Sub = "(^(?!\\s*\\d{2}/\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}\\s+\\(tid)(.*))^(?!====*)(.*)";
//	public final static String Msg_Reg_Sub = "(^(?!((\\s*\\d{2}/\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}\\s+\\(tid))|(====)))(.*))";
//	public final static String Msg_Reg_Sub = "(^(?!((\\s*\\d{2}/\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}\\s+\\(tid)|(====)|(GENERATED)|(WWN seed)))(.*))";
	public final static String Msg_Reg_Sub = "(^(?!\\s*\\d{2}/\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}\\s+\\(tid|\\s*====|\\s*GENERATED|\\s*WWN seed|No vtl.info).*)";
//	public final static String Msg_Reg_Sub = "(^(?!((\\s*\\d{2}/\\d{2}\\s+\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}\\s+\\(tid)|(====)))(.*))";
	public final static Pattern Msg_Reg_Sub_Pattern = Pattern.compile(Msg_Reg_Sub);
	
	public final static Pattern Generated_key = Pattern.compile("^GENERATED: \\d{4}-\\d{2}-\\d{2}+\\s+\\d{2}:\\d{2}:\\d{2}");
	public final static Pattern Generated =Pattern.compile("^GENERATED: (\\d{4}-\\d{2}-\\d{2}+\\s+\\d{2}:\\d{2}:\\d{2})");
	
	
}
