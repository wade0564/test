package com.emc.prometheus.parser.parse.regex;

import java.util.regex.Pattern;

public class BiosParserRegExInfo {

	public final static String BiosStart_Reg= "BIOS\\s*LOG";
	public final static Pattern BiosStart = Pattern.compile(BiosStart_Reg);
	
	public final static String BiosEnd_Reg= "(==========\\s*PCICE\\s+LOG\\s*==========)|(REGCHK)";
	public final static Pattern BiosEnd = Pattern.compile(BiosEnd_Reg);
	
	public final static String Data_Info_Reg = "(^\\s*\\w+\\s+\\|\\s+(\\d{2}/\\d{2}/\\d{4}\\s*\\|\\s*\\d{2}\\:\\d{2}\\:\\d{2}).*)";
	public final static Pattern Data_Info_Pattern = Pattern.compile(Data_Info_Reg);
	
	
	public final static String File_write_Reg = "(\\s*+(\\S++\\s++\\S++\\s++\\d++\\s++\\d++:\\d++:\\d++\\s++\\S++\\s++\\d++).*bios\\.txt$)";
	public final static Pattern File_Write_Pattern = Pattern.compile(File_write_Reg);
	
	
}
