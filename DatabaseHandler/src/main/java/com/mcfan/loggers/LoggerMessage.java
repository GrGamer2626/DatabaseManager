package com.mcfan.loggers;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcfan.main.Main;


public abstract class LoggerMessage {
	
	private static Logger logger = Main.getLogger();
	
	
	protected static void error(String msg) {
		logger.log(Level.SEVERE, msg);
	}
	
	protected static void warning(String msg) {
		logger.log(Level.WARNING, msg);
	}
	
	public static void info(String msg) {
		logger.log(Level.INFO, msg);
	}			
	
	public static void configInfo(String msg) {
		logger.log(Level.CONFIG, msg);
	}
	
	public static void fineInfo(String msg) {
		logger.log(Level.FINE, msg);
	}
}
