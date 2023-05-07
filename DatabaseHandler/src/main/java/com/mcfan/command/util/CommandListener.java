package com.mcfan.command.util;

import java.util.Scanner;

import com.mcfan.main.ConsoleSender;

public class CommandListener {
	
	private String input;
	private ConsoleSender sender;
	private boolean keepListening;
	
	
	public CommandListener(ConsoleSender sender) {
		this.sender = sender;
		keepListening = true;
	}

	public void initiateCommandListener(Scanner scanner) {
		sender.sendMessageNl(CommandManager.allCommandsUsage());
		while(keepListening) {
			input = scanner.nextLine();
			if(input.isEmpty()) {
				continue;
			}
			String[] args = input.trim().replaceAll("\\s+", " ").split(" ");
			CommandManager.handleCommand(sender, args);
		}
	}

	public void setKeepListening(boolean keepListening) {
		this.keepListening = keepListening;
	}
}
