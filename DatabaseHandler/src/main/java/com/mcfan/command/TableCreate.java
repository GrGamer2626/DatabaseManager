package com.mcfan.command;

import java.util.Arrays;
import java.util.List;

import com.mcfan.command.util.CommandExecutor;
import com.mcfan.database.QueryHandler;
import com.mcfan.main.ConsoleSender;
import com.mcfan.main.ValidateDatabaseContent;

public class TableCreate implements CommandExecutor {
	

	@Override
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args) {
		int length = args.length;
		if(length > 3) {
			String tableName = getValidTableName(args[0]);
			if(tableName != null) {
				sender.sendMessageNl("&#FF0000Such table exist");
				return false;
			}
			List<String> argsList = Arrays.asList(args).subList(1, length);
			
			StringBuilder builder = new StringBuilder();
			
			int counter = 1;
			String currentArg;
			for(int i = 0; i < argsList.size(); i++) {
				currentArg = argsList.get(i);
				if(counter != 2) {
					builder.append(currentArg).append(" ");
					
				}else {
					String dataType = currentArg.toUpperCase();
					if(!ValidateDatabaseContent.getValidateType().contains(dataType.endsWith(",") ? dataType.substring(0, dataType.length()-1) : dataType)) {
						sender.sendMessageNl("&#0094FF"+currentArg+"&#FF0000 is not valide data typ.");
						return false;
					}
					builder.append(dataType).append(" ");
				}
				if(currentArg.endsWith(",")) {
					if(counter == 1) {
						sender.sendMessageNl("&#FF0000Column Name can't contain comma!");
						return false;
					}
					if(i == (argsList.size() - 1)) {
						if(counter < 2) {
							sender.sendMessageNl("&#FF0000Missing data type.");
							return false;
						}
						builder.setLength(builder.length()-2);
					}
					counter = 0;
				}
				counter++;
			}
			new QueryHandler() {
				
				@Override
				public void query() {
					getSqlQueries().createTable(tableName, builder.toString());
					ValidateDatabaseContent.updateTableName(getSqlQueries());
					ValidateDatabaseContent.updateColumns(getSqlQueries());
				}
			};
			return true;
		}
		sender.sendMessageNl("&#FF0000Invalid number of command arguments!");
		return false;
	}
	
	
}
