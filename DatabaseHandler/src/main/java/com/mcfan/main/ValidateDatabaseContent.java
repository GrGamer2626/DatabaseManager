package com.mcfan.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mcfan.database.QueryHandler;
import com.mcfan.database.SQLQueries;

public class ValidateDatabaseContent {
	
	private static final List<String> VALIDATE_MYSQL_TYPE = List.of("VARCHAR", "CHAR", "LONGTEXT", "MEDIUMTEXT", "TEXT", "TINYTEXT", "BIGINT", "INTEGER", "MEDIUMINT", "INT", "SMALLINT", "TINYINT", "DOUBLE", "FLOAT", "BIT", "BOOLEAN");
	
	private static final List<String> VALIDATE_SQLITE_TYPE = List.of("TEXT", "BIGINT", "INTEGER", "DOUBLE", "FLOAT", "BIT");

	private static final Set<String> VALIDATE_TABLENAME = new HashSet<>();
	
	private static final Map<String, Map<String,String>> VALIDATE_COLUMN = new HashMap<>();
	

	public static List<String> getValidateType() {
		switch(Main.getDatabaseType()) {
		case "sql":
			return VALIDATE_SQLITE_TYPE;
			
		case "mysql":
			return VALIDATE_MYSQL_TYPE;
		}
		return List.of();
	}

	public static Set<String> getValidateTableName() {
		return VALIDATE_TABLENAME;
	}

	public static Map<String, Map<String, String>> getValidateColumns() {
		return VALIDATE_COLUMN;
	}
	
	public static void updateTableName(SQLQueries sqlQueries) {
		VALIDATE_TABLENAME.clear();
		VALIDATE_TABLENAME.addAll(sqlQueries.getTableNameSet());
	}
	
	public static void updateColumns(SQLQueries sqlQueries) {
		VALIDATE_COLUMN.clear();
		for(String tableName : VALIDATE_TABLENAME) {
			VALIDATE_COLUMN.put(tableName, sqlQueries.getColumnMap(tableName));
		}
	}
	
	public static void updateAll() {
		new QueryHandler() {
			
			@Override
			public void query() {
				updateTableName(getSqlQueries());
				updateColumns(getSqlQueries());
			}
		};
	}

}
