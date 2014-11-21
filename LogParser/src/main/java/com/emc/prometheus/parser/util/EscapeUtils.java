package com.emc.prometheus.parser.util;

import java.util.List;

public class EscapeUtils {
	
	public static final String PIPE ="|";
	public static final String PIPE_ESCAPE ="::PIPE::";
	public static final String ENTER ="\n";
	public static final String ENTER_ESCAPE ="::LF::";
	
	public static void transferPipe(List<String> strings ){
		
		for (String string : strings) {
			string.replace(PIPE, PIPE_ESCAPE);
		}
		
	}

}
