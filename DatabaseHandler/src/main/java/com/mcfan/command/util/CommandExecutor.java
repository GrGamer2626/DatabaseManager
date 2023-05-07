package com.mcfan.command.util;

import com.mcfan.main.ConsoleSender;
import com.mcfan.main.ValidateDatabaseContent;

public interface CommandExecutor {
	
	
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args);
	
	
	
	public default String getValidTableName(String tableName) {
		String validColName = ValidateDatabaseContent.getValidateTableName().stream()
				.filter(name-> name.equalsIgnoreCase(tableName))
				.findFirst()
				.orElse(null);
		
		return validColName;
	}
	
	public default String getValidColumnName(String tableName, String columnName) {
		String validColName = ValidateDatabaseContent.getValidateColumns().get(tableName).keySet().stream()
				.filter(name-> name.equalsIgnoreCase(columnName))
				.findFirst()
				.orElse(null);
		
		return validColName;
	}
	public default String getValidColumnDataType(String tableName, String columnName) {
		return ValidateDatabaseContent.getValidateColumns().get(tableName).get(getValidColumnName(tableName, columnName));
	}
	
	
	public default void tableDoNotExist(ConsoleSender sender, String tableName) {
		sender.sendMessageNl(new StringBuilder("&#FF0000")
				.append(tableName)
				.append(" table does not exist!")
				.toString());
	}
	
}
