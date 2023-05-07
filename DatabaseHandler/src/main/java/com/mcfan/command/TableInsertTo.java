package com.mcfan.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mcfan.command.util.CommandExecutor;
import com.mcfan.database.QueryHandler;
import com.mcfan.main.ConsoleSender;
import com.mcfan.messages.Builder;

public class TableInsertTo implements CommandExecutor {

	@Override
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args) {
		int length = args.length;
		if(length < 3) {
			sender.sendMessageNl("&#FF0000Invalid number of command arguments!");
			return false;
		}
		String tableName = getValidTableName(args[0]);
		if(tableName == null) {
			tableDoNotExist(sender, args[0]);
			return false;
		}
		
		
		Builder builder = new Builder();
		boolean buildSting = false;
		
		List<String> argsList = new ArrayList<>();
		for(String arg : Arrays.asList(args).subList(1, length)) {
			if(arg.startsWith("'")) {
				buildSting = true;
				builder.append(arg).append(" ");
			
			}else if(arg.endsWith("'")) {
				buildSting = false;
				builder.append(arg);
				
				argsList.add(builder.toString());
				builder.clear();
				
			}else if(buildSting) {
				builder.append(arg).append(" ");
				
			}else {
				argsList.add(arg);
			}
		}
		
		StringBuilder columnsBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();
		
		for(String arg : argsList) {
			int index = argsList.indexOf(arg);
			if(index % 2 == 0) {
				String columnName = getValidColumnName(tableName, arg);
				if(columnName == null) {
					sender.sendMessageNl("&#FF0000Invalid column name!");
					return false;
				}
				columnsBuilder.append(columnName).append(", ");
				
			}else {
				String columnDataType = getValidColumnDataType(tableName, argsList.get(index - 1));
				if(!handleColumnDataType(sender, columnDataType, arg)) {
					return false;
				}
				valueBuilder.append(arg).append(", ");
			}
		}
		columnsBuilder.setLength(columnsBuilder.length()-2);
		valueBuilder.setLength(valueBuilder.length()-2);
		
		new QueryHandler() {
			
			@Override
			public void query() {
				getSqlQueries().insertToTable(tableName, columnsBuilder.toString(), valueBuilder.toString());
				
			}
		};
		return true;
	}
	
	private boolean handleColumnDataType(ConsoleSender sender, String columnType, String arg) {
		String[] charType = columnType.split(" ");
		if(charType.length == 2) {
			int maxColumnLength = Integer.parseInt(charType[1]) ;
			if(arg.length() <= maxColumnLength+2) {
				return true;
			}
			sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is too long. The maximum number of characters in this char column is ").append(maxColumnLength).toString());
			return false;
		}
		switch(columnType) {
		case "VARCHAR":
			return true;
			
		case "TINYTEXT":
			if(arg.length()<=257) {
				return true;
				
			}else {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is too long. The maximum number of characters is 255.").toString());
				return false;
			}
		case "TEXT":
			if(arg.length()<=65_537) {
				return true;
				
			}else {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is too long. The maximum number of characters is 65,535.").toString());
				return false;
			}
		case "MEDIUMTEXT":
			if(arg.length()<=16_777_217) {
				return true;
				
			}else {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is too long. The maximum number of characters is 16,777,215.").toString());
				return false;
			}
		case "LONGTEXT":
			if(((long) arg.length())<=4_294_967_297L) {
				return true;
				
			}else {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is too long. The maximum number of characters is 4,294,967,295.").toString());
				return false;
			}
		case "BIT": case "BOOLEAN":
			try {
				Boolean.parseBoolean(arg);
				return true;
				
			}catch(IllegalArgumentException e) {
				sender.sendMessageNl(new StringBuilder("&#FF0000").append(arg).append(" is not boolean data type").toString());
				return false;
			}
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
			sender.sendMessageNl(new StringBuilder("&#FF0000We don't support ").append(columnType).append(" data type yet.").toString());
			return false;
		}
	}
}
