package com.mcfan.command;

import java.util.Set;

import com.mcfan.command.util.CommandExecutor;
import com.mcfan.main.ConsoleSender;
import com.mcfan.main.ValidateDatabaseContent;
import com.mcfan.messages.Builder;

public class ListTable implements CommandExecutor {

	
	@Override
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args) {
		ValidateDatabaseContent.updateAll();
		Set<String> tableNames = ValidateDatabaseContent.getValidateTableName();
		sender.sendMessageNl("====== Table List ======");
		if(tableNames.isEmpty()) {
			sender.sendMessageNl("&#FFC851There are no tables in the database");
		}
		Builder builder = new Builder();
		tableNames.forEach(name-> 
				{
					builder.setBuilder("  &#0094FF");
					sender.sendMessageNl(builder.append(name).toString());
				});
		
		return true;
	}

}
