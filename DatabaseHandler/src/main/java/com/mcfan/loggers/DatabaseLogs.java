package com.mcfan.loggers;

import com.mcfan.database.Database;
import com.mcfan.database.DatabaseConnection;
import com.mcfan.messages.Builder;



public class DatabaseLogs extends LoggerMessage {
	
	private static Database db = DatabaseConnection.getDatabase();
	private static String PREFIX = db == null ? "[Database]" : db.getPrefix();
	
	//////////////////////// Connecting to database //////////////////////////
	
	public static void driverNotFoundMsg() {
		Builder builder = new Builder("{dbPrefix} JDBC driver not found!")
				.replace("{dbPrefix}", PREFIX);
		
		error(builder.toString());
	}
	
	public static void connectionCloseMsg() {
		Builder builder = new Builder("&#56FF55{dbPrefix} Connection closed successfully.")
				.replace("{dbPrefix}", PREFIX);
		
		info(builder.toString());
	}
	
	public static void noOpenConnectionMsg() {
		Builder builder = new Builder("{dbPrefix} There was no connection open.")
				.replace("{dbPrefix}", PREFIX);
		
		warning(builder.toString());
	}
	
	public static void incorrectDatabaseTypeMsg() {
		Builder builder = new Builder("{dbPrefix} Incorrect database type was entered in the config.yml file!")
				.replace("{dbPrefix}", PREFIX);
		
		warning(builder.toString());
	}
	
	public static void incorrectMySQLConnectionDataMsg() {
		warning("[MySQL] Incorrect MySQL connection data were entered in the config.yml file or another database connection problem!");
	}
	
	public static void establishingConnectionMsg(){
		info("Establishing database connection...");
	}
	
	public static void connectionEstablishedMsg() {
		Builder builder = new Builder("&#56FF55{dbPrefix} Connection established!")
				.replace("{dbPrefix}", PREFIX);
		
		info(builder.toString());
	}
	
	
	//////////////////////// Creating table //////////////////////////
	
	public static void tableCreating(String tableName) {
		Builder builder = new Builder("{dbPrefix} Creating table {tableName}....")
				.replace("{dbPrefix}", PREFIX)
				.replace("{tableName}", tableName);
		
		info(builder.toString());
	}
	
	public static void tableCreatedSuccessfully(String tableName) {
		Builder builder = new Builder("&#56FF55{dbPrefix} Table {tableName} created successfully!")
				.replace("{dbPrefix}", PREFIX)
				.replace("{tableName}", tableName);
		
		info(builder.toString());
	}
	
	
	//////////////////////// Adding column to table //////////////////////////
	
	public static void addingColumnToTable(String tableName, String column, String dataType) {
		Builder builder = new Builder("&#56FF55{dbPrefix} Column {column} {dataType} was added to table {tableName}")
				.replace("{dbPrefix}", PREFIX)
				.replace("{tableName}", tableName)
				.replace("{column}", column)
				.replace("{dataType}", dataType);
	
		info(builder.toString());
	}
	
	//////////////////////// Removing table //////////////////////////
	
	public static void tableRemoving(String tableName) {
		Builder builder = new Builder("{dbPrefix} Removing table {tableName}....")
				.replace("{dbPrefix}", PREFIX)
				.replace("{tableName}", tableName);
		
		info(builder.toString());
	}
	
	public static void tableRemovedSuccessfully(String tableName) {
		Builder builder = new Builder("&#56FF55{dbPrefix} Table {tableName} removed successfully!")
				.replace("{dbPrefix}", PREFIX)
				.replace("{tableName}", tableName);
		
		info(builder.toString());
	}
	
	public static void setPrefix(String PREFIX) {
		DatabaseLogs.PREFIX = PREFIX;
	}
	
	public static void setDatabase(Database db) {
		DatabaseLogs.db = db;
	}
}
