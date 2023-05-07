package com.mcfan.command;

import com.mcfan.command.util.CommandExecutor;
import com.mcfan.main.ConsoleSender;
import com.mcfan.main.Main;

public class Exit implements CommandExecutor {

	@Override
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args) {
		
		Main.getCommandListener().setKeepListening(false);
		
		
		return true;
	}

}
