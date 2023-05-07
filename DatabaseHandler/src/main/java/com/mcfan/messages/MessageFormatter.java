package com.mcfan.messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageFormatter {
	
	//Pattern to find RGB code
	private final static Pattern colorPattern = Pattern.compile("\\&#([a-fA-F0-9]{6})");
	private final static Pattern formatingTextPattern = Pattern.compile("\\&([k-or]){1}");
	
	private final static String MAGIC = 		 "\u001B[5m"; //Don't work in console
	private final static String BOLD = 			 "\u001B[1m";
	private final static String STRIKETHROUGH =  "\u001B[9m";
	private final static String UNDERLINE = 	 "\u001B[4m";
	private final static String ITALIC = 		 "\u001B[3m";
	private final static String RESET = 		 "\u001B[0m";
	
	public static String textFormater(String input) {
		Matcher matcher = colorPattern.matcher(input);
		Builder output = new Builder(input);
		
		while(matcher.find()) {
			output.replace(matcher.group(), convertHexToAnsi(matcher.group()));
		}
		
		String format;
		matcher = formatingTextPattern.matcher(output.getStringBuilder());
		while(matcher.find()) {
			format = matcher.group();
			
			switch (format.substring(1)) {
			case "k":
				output.replace(format, MAGIC);
				continue;

			case "l":
				output.replace(format, BOLD);
				continue;
				
			case "m":
				output.replace(format, STRIKETHROUGH);
				continue;
				
			case "n":
				output.replace(format, UNDERLINE);
				continue;
				
			case "o":
				output.replace(format, ITALIC);
				continue;
				
			case "r":
				output.replace(format, RESET);
				continue;
			}
		}
		return output.append(RESET).toString();
	}
	
	private static String convertHexToAnsi(String hexCode) {
		String hex = new Builder(hexCode).replace("&", "").replace("#", "").toString();
		
		int r = toDecimal(hex.substring(0,2));
		int g = toDecimal(hex.substring(2,4));
		int b = toDecimal(hex.substring(4,6));
		
		return convertRgbToAnsi(r, g, b);
	}
	
	private static int toDecimal(String hexCode) {
		return Integer.parseInt(hexCode, 16);
	}
	
	private static String convertRgbToAnsi(int r, int g, int b) {
		Builder colorCode = new Builder("\u001B[38;2;{red};{green};{blue}m")
				.replace("{red}", r)
				.replace("{green}", g)
				.replace("{blue}", b);
		
		return colorCode.toString();
	}
}
