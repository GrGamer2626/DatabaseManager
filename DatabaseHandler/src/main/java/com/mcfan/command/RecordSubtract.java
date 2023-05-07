package com.mcfan.command;

import java.text.NumberFormat;
import java.util.Arrays;

import com.mcfan.command.util.CommandExecutor;
import com.mcfan.database.QueryHandler;
import com.mcfan.main.ConsoleSender;

public class RecordSubtract  implements CommandExecutor {

	@Override
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args) {
		int length = args.length;
		if(length >= 3) {
			String tableName = getValidTableName(args[0]);
			if(tableName == null) {
				tableDoNotExist(sender, tableName);
				return false;
			}
			String columnName = getValidColumnName(tableName, args[1]);
			if(columnName == null) {
				sender.sendMessageNl(new StringBuilder("&#FF0000Column with name ").append(columnName).append(" don't exist").toString());	
				return false;
			}
			String value = args[2];
			
			String dataType = getValidColumnDataType(tableName, columnName);
			if(!handleColumnDataType(sender, dataType, value)) {
				return false;
			}
			
			if(length==3) {
				new QueryHandler() {
					
					@Override
					public void query() {
						getSqlQueries().subtractValeue(tableName, columnName, value);
					}
				};
				return true;
				
			}else {
				StringBuilder whereStatment = new StringBuilder("");
				Arrays.asList(args).subList(2, length).stream()
						.skip(1)
						.forEach(arg-> whereStatment.append(arg).append(" "));
				whereStatment.setLength(whereStatment.length() - 1);
				
				new QueryHandler() {
					
					@Override
					public void query() {
						getSqlQueries().subtractValeue(tableName, columnName, value, whereStatment.toString());
					}
				};
				return true;
			}
		}
		return false;
	}
	
	private boolean handleColumnDataType(ConsoleSender sender, String columnType, String arg) {
		switch(columnType) {
		case "TINYINT":
			try {
				Byte.parseByte(arg);
				return true;
				
			}catch(NumberFormatException e) {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is not byte data type").toString());
				return false;
			}
		case "SMALLINT":
			try {
				Short.parseShort(arg);
				return true;
				
			}catch(NumberFormatException e) {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is not short data type").toString());
				return false;
			}
		case "MEDIUMINT": case "INT": case "INTEGER":
			try {
				Integer.parseInt(arg);
				return true;
				
			}catch(NumberFormatException e) {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is not integer data type").toString());
				return false;
			}
		case "BIGINT":
			try {
				Long.parseLong(arg);
				return true;
				
			}catch(NumberFormatException e) {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is not long data type").toString());
				return false;
			}
		case "DOUBLE":
			try {
				Double.parseDouble(arg);
				return true;
				
			}catch(NumberFormatException e) {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is not double data type").toString());
				return false;
			}
		case "FLOAT":
			try {
				Float.parseFloat(arg);
				return true;
				
			}catch(NumberFormatException e) {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is not float data type").toString());
				return false;
			}
		default:
			try {
				NumberFormat.getInstance().parse(arg);
				sender.sendMessageNl(new StringBuilder("&#FF0000We don't support ").append(columnType).append(" data type yet.").toString());
				
			}catch (Exception e) {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is not number!").toString());
			}
			
			return false;
		}
	}
}
