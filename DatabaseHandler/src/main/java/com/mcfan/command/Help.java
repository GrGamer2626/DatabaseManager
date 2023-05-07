package com.mcfan.command;

import java.util.Map;

import com.mcfan.command.util.Command;
import com.mcfan.command.util.CommandExecutor;
import com.mcfan.command.util.CommandManager;
import com.mcfan.main.ConsoleSender;
import com.mcfan.messages.Builder;

public class Help implements CommandExecutor {

	private static Map<String, Command> commandMap = CommandManager.getCommandMap();
	
	@Override
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(commandList());
			return true;
			
		}else if(args.length == 1) {
			String commandName = args[0].toLowerCase();
			if(!commandMap.containsKey(commandName)) {
				sender.sendMessageNl("&#FF0000Incorrect command name");
				return false;
			}
			StringBuilder builder = new StringBuilder("&#0094FF")
					.append(commandName)
					.append("&r - ")
					.append(commandMap.get(commandName).getDescription());
			
			sender.sendMessageNl(builder.toString());
			return true;
		}
		return false;
	}
	
	private static String commandList() {
		Builder msg = new Builder("======= Command List =======\n");
		for(Command cmd : commandMap.values()) {
			msg.append(cmd.getCorrectUsage()).append("\n\n&r");
		}
		return msg.toString();
	}
}
