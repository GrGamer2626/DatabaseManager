package com.mcfan.command;

import java.util.Arrays;

import com.mcfan.command.util.CommandExecutor;
import com.mcfan.database.QueryHandler;
import com.mcfan.main.ConsoleSender;

public class RecordRemove implements CommandExecutor {

	@Override
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args) {
		int length = args.length;
		if(length <= 2) {
			String tableName = getValidTableName(args[0]);
			if(tableName == null) {
				tableDoNotExist(sender, tableName);
				return false;
			}
			StringBuilder whereStatment = new StringBuilder();
			Arrays.asList(args).stream()
					.skip(1)
					.forEach(arg-> whereStatment.append(arg).append(" "));
			whereStatment.setLength(whereStatment.length() - 1);
			
			new QueryHandler() {
				
				@Override
				public void query() {
					getSqlQueries().removeRecord(tableName, whereStatment.toString());
				}
			};
			return true;
		}		
		sender.sendMessageNl("&#FF0000Invalid number of command arguments!");
		return false;
	}
}
