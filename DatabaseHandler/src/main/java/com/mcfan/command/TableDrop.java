package com.mcfan.command;

import com.mcfan.command.util.CommandExecutor;
import com.mcfan.database.QueryHandler;
import com.mcfan.main.ConsoleSender;

public class TableDrop implements CommandExecutor {

	@Override
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args) {
		if(args.length == 1) {
			String tableName = getValidTableName(args[0]);
			if(tableName == null) {
				tableDoNotExist(sender, tableName);
				return false;
			}
			new QueryHandler() {
				
				@Override
				public void query() {
					getSqlQueries().removeTable(tableName);
				}
			};
			return true;
		}
		sender.sendMessageNl("&#FF0000Invalid number of command arguments!");
		return false;
	}

}
