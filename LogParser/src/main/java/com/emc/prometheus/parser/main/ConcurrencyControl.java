package com.emc.prometheus.parser.main;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcurrencyControl {
	final static Logger log = LoggerFactory.getLogger(ConcurrencyControl.class);
	
	public static boolean grabLock(String sbase, String file) {
		File base = new File(sbase);
		File f = new File(base, file);
		File fl = new File(base, file+".inuse");
		if(!f.exists()) return false;
		
		log.info("+++ grab lock +++");
		return f.renameTo(fl);
	}
	
	public static boolean releaseLock(String sbase, String file) {
		File base = new File(sbase);
		File f = new File(base, file);
		File fl = new File(base, file+".inuse");
		if(!fl.exists()) return false;

		log.info("--- release lock ---");
		return fl.renameTo(f);
	}
}
