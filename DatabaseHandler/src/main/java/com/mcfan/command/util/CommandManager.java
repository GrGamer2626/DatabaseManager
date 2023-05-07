package com.mcfan.command.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.mcfan.main.ConsoleSender;
import com.mcfan.messages.Builder;


public class CommandManager {
	
	private static final Map<String, Command> COMMAND_MAP = new HashMap<>();
	
	
	public static void handleCommand(ConsoleSender sender, String[] args) {
		String mainArg = args[0].toLowerCase();
		if(!COMMAND_MAP.keySet().contains(mainArg)) {
			sender.sendMessageNl("&#C12228Incorrect command!");
			sender.sendMessageNl(allCommandsUsage());
			return;
		}
		String[] commandArgs = Arrays.stream(args)
				.skip(1)
				.toArray(String[]::new);
		
		Command cmd = COMMAND_MAP.get(mainArg);
		boolean isDoneCorrectly = cmd.getExecutor().onCommand(sender, cmd.getCorrectUsage(), commandArgs);
		
		if(!isDoneCorrectly) {
			Command help = COMMAND_MAP.get("help");
			help.getExecutor().onCommand(sender, help.getCorrectUsage(), new String[0]);
		}
	}
	
	
	
	
	
	public static String allCommandsUsage() {
		Builder msg = new Builder("======= Command List and Description =======\n");
		for(Command cmd: COMMAND_MAP.values()) {
			msg.append(cmd.getCorrectUsage())
			.append("&r - ")
			.append(cmd.getDescription()).append("\n\n&r");
		}
		return msg.toString();
	}
	
	public static Map<String, Command> getCommandMap() {
		return COMMAND_MAP;
	}
	
	public static Command registerCommand(String name, CommandExecutor executor) {
		return new Command(name, executor);
	}
	
	

}
