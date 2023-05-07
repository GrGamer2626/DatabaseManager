package com.mcfan.command;

import java.util.Map;

import com.mcfan.command.util.CommandExecutor;
import com.mcfan.main.ConsoleSender;
import com.mcfan.main.ValidateDatabaseContent;

public class ListColumns implements CommandExecutor {

	@Override
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args) {
		if(args.length == 1) {
			ValidateDatabaseContent.updateAll();
			String tableName = getValidTableName(args[0]);
			if(tableName == null) {
				tableDoNotExist(sender, tableName);
				return false;
			}
			Map<String, String> columns = ValidateDatabaseContent.getValidateColumns().get(tableName);
			sender.sendMessageNl("ColumnName\t\tDataType");
			columns.entrySet().stream()
			.forEachOrdered(entry -> 
			{
				sender.sendMessage(entry.getKey());
				sender.sendMessage("\t\t");
				sender.sendMessageNl(entry.getValue());
			});
			return true;
		}
		return false;
	}
	
	

}
