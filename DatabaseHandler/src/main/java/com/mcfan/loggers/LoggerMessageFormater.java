package com.mcfan.loggers;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mcfan.messages.Builder;
import com.mcfan.messages.MessageFormatter;



public class LoggerMessageFormater extends Formatter {
	
	//Pattern to find RGB code

	private final static Pattern formatingTextPattern = Pattern.compile("\\&([r]){1}");
	
	private final static String RESET = 		 "\u001B[0m";  //Work
	private final static String RESET_WARNING = "\u001B[0m\u001B[38;2;249;241;165m";  //Work
	private final static String RESET_ERROR = 	 "\u001B[0m\u001B[38;2;231;72;86m";  //Work
	
	@Override
	public String format(LogRecord record) {
		String msg = record.getMessage();
		Level level = record.getLevel();
		
		Builder input = new Builder("[");
		if(level == Level.INFO) {
			input.append("&#514AFFINFO&r] ");
			
		}else {
			input.append(level.getName()).append("] ");
		}
		
		return handleResetColor(level, input.append(msg).append("&r\n").toString());
	}
	
	private String handleResetColor(Level level, String input) {
		Matcher matcher = formatingTextPattern.matcher(input);
		Builder output = new Builder(input);

		String format;
		
		while(matcher.find()) {
			format = matcher.group();
			if(format.substring(1).equals("r")) {
				if(level == Level.SEVERE) {
					output.replace(format, RESET_ERROR);

				}else if(level == Level.WARNING) {
					output.replace(format, RESET_WARNING);
					
				}else {
					output.replace(format, RESET);
				}
			}
		}
		return MessageFormatter.textFormater(output.toString());
	}
	
}
