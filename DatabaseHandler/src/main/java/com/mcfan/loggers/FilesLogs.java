package com.mcfan.loggers;

import com.mcfan.messages.Builder;

public class FilesLogs extends LoggerMessage {
	
	
	public static void directoryGeneratingMsg(String fileName) {
		Builder builder = new Builder("Generating new directory for {fileName}....")
				.replace("{fileName}", fileName);
		
		info(builder.toString());
	}
	
	public static void directoryGeneratedSuccessfullyMsg(String fileName) {
		Builder builder = new Builder("&#56FF55Directory for {fileName} generated Successfully!")
				.replace("{fileName}", fileName);
		
		info(builder.toString());
	}
	
	public static void directoryGeneratingFailedMsg() {
		error("Failed to generate directory!");
	}
	
	////////////////////////// Generating file /////////////////////////////////
	
	public static void fileGeneratingMsg(String fileName) {
		Builder builder = new Builder("Generating new {fileName} .....")
				.replace("{fileName}", fileName);
		
		info(builder.toString());
	}
	
	public static void fileGeneratedSuccessfullyMsg(String fileName) {
		Builder builder = new Builder("&#56FF55File {fileName} generated successfully!")
				.replace("{fileName}", fileName);;
		
		info(builder.toString());
	}
	
	public static void fileGeneratingFailedMsg(String fileName) {
		Builder builder = new Builder("Failed to generate {fileName}!")
				.replace("{fileName}", fileName);
		
		error(builder.toString());
	}
}
