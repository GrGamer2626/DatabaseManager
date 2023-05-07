package com.mcfan.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.mcfan.command.util.CommandExecutor;
import com.mcfan.database.QueryHandler;
import com.mcfan.main.ConsoleSender;
import com.mcfan.main.ValidateDatabaseContent;
import com.mcfan.messages.Builder;

public class RecordGet implements CommandExecutor {

	
	@Override
	public boolean onCommand(ConsoleSender sender, String correctUsage, String[] args) {
		int length = args.length;
		if(length == 2) {
			String tableName = getValidTableName(args[0]);
			if(tableName == null) {
				tableDoNotExist(sender, args[0]);
				return false;
			}
			boolean isDoneProperly = handleColumnName(sender, tableName, args[1], null);
			return isDoneProperly;
			
		}else if (length >= 3) {
			String tableName = getValidTableName(args[0]);
			if(tableName == null) {
				tableDoNotExist(sender, args[0]);
				return false;
			}
			
			StringBuilder whereStatment = new StringBuilder();
			Arrays.asList(args).stream()
								.skip(2)
								.forEach(arg->whereStatment.append(arg).append(" "));
			whereStatment.setLength(whereStatment.length() - 1);
			
			boolean isDoneProperly = handleColumnName(sender, tableName, args[1], whereStatment.toString());
			return isDoneProperly;
		}
		return false;
	}
	
	private boolean handleColumnName(ConsoleSender sender, String tableName, String columnName, String where) {
		String validColumnName = getValidColumnName(tableName, columnName);
		
		List<List<String>> list = new ArrayList<>();
		if(columnName.equalsIgnoreCase("*")) {
			new QueryHandler() {
				
				@Override
				public void query() {
					List<List<String>> rows = (where == null) ? getSqlQueries().getAllTable(tableName) : getSqlQueries().getAllTable(tableName, where);					
					rows.stream().forEachOrdered(row-> list.add(row));
				}
			};
			displayResoult(sender, list);
			return true;
			
		}else if(validColumnName != null)  {
			if(!ValidateDatabaseContent.getValidateType().contains(getValidColumnDataType(tableName, validColumnName))) {
				sender.sendMessageNl("&#FF0000Sorry, We don't support this data type yet.");
				return false;
			}
			new QueryHandler() {
				
				@Override
				public void query() {
					List<List<String>> rows = (where == null) ? getSqlQueries().getRecord(tableName, columnName) : getSqlQueries().getRecord(tableName, columnName, where);
					rows.stream().forEachOrdered(row-> list.add(row));
				}
			};
			displayResoult(sender, list);
			return true;
		}
		sender.sendMessageNl(new StringBuilder("&#FF0000Column with name ").append(columnName).append(" don't exist").toString());	
		return false;
	}
	
	private void displayResoult(ConsoleSender sender, List<List<String>> list) {
		int[] maxColumnLength = new int[list.get(0).size()];
		list.forEach(row->{
			IntStream.range(0, row.size()).forEach(i->{
				int recordLength = row.get(i).length();
				if(recordLength> maxColumnLength[i]) {
					maxColumnLength[i] = recordLength;
				}
			});
		});
		
		Builder builder = new Builder();
		list.forEach(row -> {
			if(list.indexOf(row) == 0) {
    			builder.append("&#FFAE00&l");
    		}
			row.forEach(record -> {
				int columnIndex = row.indexOf(record);
				int recordLength = record.length();
				
				
				int separatorsAmount = (maxColumnLength[columnIndex] - recordLength) / 2;
				
				IntStream.rangeClosed(0, separatorsAmount).forEach(i -> builder.append(" "));
				
				builder.append(record);
				
				IntStream.rangeClosed(0, separatorsAmount).forEach(i -> builder.append(" "));
				
				builder.append("\t");
			});
			sender.sendMessageNl(builder.toString());
			builder.clear();
		});
	}
}
