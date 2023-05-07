package com.mcfan.command;

import java.util.Arrays;

import com.mcfan.command.util.CommandExecutor;
import com.mcfan.database.QueryHandler;
import com.mcfan.main.ConsoleSender;
import com.mcfan.main.ValidateDatabaseContent;
import com.mcfan.messages.Builder;

public class TableAddColumn implements CommandExecutor {

	@Override
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args) {
		if(args.length == 3) {
			String tableName = getValidTableName(args[0]);
			if(tableName == null) {
				tableDoNotExist(sender, tableName);
				return false;
			}
			String dataType = args[2].toUpperCase();
			if(!ValidateDatabaseContent.getValidateType().contains(dataType)) {
				sender.sendMessageNl(new Builder(dataType).append(" is not valide data typ.").toString());
				return false;
			}
			
			new QueryHandler() {
				
				@Override
				public void query() {
					getSqlQueries().addColumnToTable(tableName, args[1], dataType);
					ValidateDatabaseContent.updateColumns(getSqlQueries());
				}
			};
			return true;
		
		}else if(args.length >= 4) {
			String tableName = getValidTableName(args[0]);
			if(tableName == null) {
				tableDoNotExist(sender, tableName);
				return false;
			}
			String dataType = args[2].toUpperCase();
			if(!ValidateDatabaseContent.getValidateType().contains(dataType)) {
				sender.sendMessageNl(new Builder(dataType).append(" is not valide data typ.").toString());
				return false;
			}
			StringBuilder extraProperties = new StringBuilder();
			Arrays.asList(args).stream()
								.skip(3)
								.forEach(arg->extraProperties.append(arg).append(" "));
			extraProperties.setLength(extraProperties.length() - 1);
			
			new QueryHandler() {
				
				@Override
				public void query() {
					getSqlQueries().addColumnToTable(tableName, args[1], dataType, extraProperties.toString());
					ValidateDatabaseContent.updateColumns(getSqlQueries());
				}
			};
			return true;
		}
		
		sender.sendMessageNl("Invalid number of command arguments");
		return false;
	}
}
