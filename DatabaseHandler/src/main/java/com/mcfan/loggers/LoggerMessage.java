package com.mcfan.loggers;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class LoggerMessage {
	
	private static ConsoleHandler handler = new ConsoleHandler();
	private static Logger logger = Logger.getLogger(LoggerMessage.class.getName());
	
	static {
		logger.setUseParentHandlers(false);

		handler.setFormatter(new LoggerMessageFormater());
		handler.setLevel(Level.ALL);
		
		logger.addHandler(handler);
		logger.setLevel(Level.ALL);
	}
	
	
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
