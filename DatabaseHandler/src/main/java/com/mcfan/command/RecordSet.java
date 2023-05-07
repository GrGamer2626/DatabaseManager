package com.mcfan.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mcfan.command.util.CommandExecutor;
import com.mcfan.database.QueryHandler;
import com.mcfan.main.ConsoleSender;
import com.mcfan.messages.Builder;

public class RecordSet implements CommandExecutor {

	//set tableName columnName value where
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
			Builder builder = new Builder();
			boolean buildSting = false;
			
			List<String> argsList = new ArrayList<>();
			for(String arg : Arrays.asList(args).subList(2, length)) {
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
			String value = argsList.get(0);
			
			String dataType = getValidColumnDataType(tableName, columnName);
			if(!handleColumnDataType(sender, dataType, value)) {
				return false;
			}
			
			if(argsList.size() == 1) {
				new QueryHandler() {
					
					@Override
					public void query() {
						getSqlQueries().setRecord(tableName, columnName, value);
					}
				};
				return true;
				
			}else {
				StringBuilder whereStatment = new StringBuilder();
				argsList.stream()
						.skip(1)
						.forEach(arg-> whereStatment.append(arg).append(" "));
				whereStatment.setLength(whereStatment.length() - 1);
				new QueryHandler() {
					
					@Override
					public void query() {
						getSqlQueries().setRecord(tableName, columnName, value, whereStatment.toString());
					}
				};
				return true;
			}
		}
		sender.sendMessageNl("&#FF0000Invalid number of command arguments!");
		return false;
	}
	
	private boolean handleColumnDataType(ConsoleSender sender, String columnType, String arg) {
		String[] charType = columnType.split(" ");
		if(charType.length == 2) {
			int maxColumnLength = Integer.parseInt(charType[1]) ;
			if(arg.length() <= maxColumnLength + 2) {
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
