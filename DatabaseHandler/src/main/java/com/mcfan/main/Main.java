package com.mcfan.main;

import java.util.List;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcfan.command.ListColumns;
import com.mcfan.command.TableCreate;
import com.mcfan.command.TableDrop;
import com.mcfan.command.RecordGet;
import com.mcfan.command.RecordRemove;
import com.mcfan.command.RecordSet;
import com.mcfan.command.RecordSubtract;
import com.mcfan.command.TableAddColumn;
import com.mcfan.command.Exit;
import com.mcfan.command.Help;
import com.mcfan.command.TableInsertTo;
import com.mcfan.command.ListTable;
import com.mcfan.command.RecordAdd;
import com.mcfan.command.util.Command;
import com.mcfan.command.util.CommandListener;
import com.mcfan.database.DatabaseConnection;
import com.mcfan.loggers.LoggerMessageFormater;

import static com.mcfan.command.util.CommandManager.*;


public class Main {
	
	private static final List<String> VALID_DATABASE_TYPE = List.of("sql", "mysql");
	private static Logger logger = Logger.getLogger("");
	private static String databaseType;
	private static CommandListener commandListener;
	
	public static void main(String[] args) {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new LoggerMessageFormater());
		logger.addHandler(handler);
		logger.setLevel(Level.ALL);
		
		registerAllCommands();
		
		ConsoleSender sender = new ConsoleSender();
		try(Scanner scanner = new Scanner(System.in)) {
			databaseType = enterDatabaseTypeMsg(sender, scanner);
			
			while(!VALID_DATABASE_TYPE.contains(databaseType)) {
				sender.sendMessageNl("&#FF0000Incorrect or not supported database type!");
				databaseType = enterDatabaseTypeMsg(sender, scanner);
			}
			
			DatabaseConnection dbConnection = new DatabaseConnection(sender, databaseType);
			dbConnection.connectToDatabase(scanner);
			
			commandListener = new CommandListener(sender);
			commandListener.initiateCommandListener(scanner);
			
			DatabaseConnection.getDatabase().disconnect();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	
	private static void registerAllCommands() {
		Command helpCmd = registerCommand("help", new Help());
		helpCmd.setCorrectUsage("&#0094FFHelp &#59B503[CommandName]");
		helpCmd.setDescription("Display list of commands or description provided command");
		
		Command tableListCmd = registerCommand("tablelist", new ListTable());
		tableListCmd.setCorrectUsage("&#0094FFTableList");
		tableListCmd.setDescription("Display list of tables in database");
		
		Command columndListCmd = registerCommand("columnlist", new ListColumns());
		columndListCmd.setCorrectUsage("&#0094FFColumnList &#FFA700<TableName>");
		columndListCmd.setDescription("Display list of columns in table and their data type");
		
		Command createCmd = registerCommand("createtable", new TableCreate());
		createCmd.setCorrectUsage("&#0094FFCreateTable &#FFA700<TableName> <ColumnName DataType &#59B503[Extra Properties]&#FFA700>");
		createCmd.setDescription("Create new table if don't exist. &l&#FF3D3DColumn declaration must end with a comma!&r\n"
				+ "\t&#59B503&lExample:&r\n\tcreateTable Table_1 column_1 INTEGER PRIMARY KEY AUTO_INCREMENT, column_2 BIT NOT NULL DEFAULT 0, column_3 TEXT");
		
		Command addColumnCmd = registerCommand("addcolumn", new TableAddColumn());
		addColumnCmd.setCorrectUsage("&#0094FFAddColumn &#FFA700<TableName> <ColumnName> <DataType>");
		addColumnCmd.setDescription("Add a new column to table if the table didn't contain it before");
		
		Command removeTableCmd = registerCommand("removetable", new TableDrop());
		removeTableCmd.setCorrectUsage("&#0094FFRemoveTable &#FFA700<TableName>");
		removeTableCmd.setDescription("Removing the table from database");
		
		Command insertCmd = registerCommand("insert", new TableInsertTo());
		insertCmd.setCorrectUsage("&#0094FFInsert &#FFA700<TableName> <ColumnName_1> <Value_1> <ColumnName_2> <Value_2>...");
		insertCmd.setDescription("Insert records to provided table.");
		
		Command getCmd = registerCommand("get", new RecordGet());
		getCmd.setCorrectUsage("&#0094FFGet &#FFA700<TableName> <ColumnName> &#59B503[Logical Condition]");
		getCmd.setDescription("Gets records from provided table from provided column(or all column in table if columnName is *).\n"
				+ "\tUsing logical condition: \n"
				+ "\t - Math logic operators = equals, != not equals >, >=, <, <= &#59B503&lExample:&r Id=3, Age>=18\n"
				+ "\t - AND if both condition have to be true. &#59B503&lExample:&r Age>=18 AND Id<30\n"
				+ "\t - OR if one of the conditions must be true. &#59B503&lExample:&r Age=18 OR Age=21\n"
				+ "\t - NOT to invert logical condition. &#59B503&lExample:&r NOT Name='Hanna', NOT LIKE Name='Hanna'\n"
				+ "\t - LIKE check which TEXT column row contains the specified phrase. Use the % tag to match any text before or after specified phrase\n"
				+ "\t   &#59B503&lExample:&r Name LIKE '%Hanna%'\n"
				+ "\tUse brackets to specify the order in which the conditions are performed.");
		
		Command setCmd = registerCommand("set", new RecordSet());
		setCmd.setCorrectUsage("&#0094FFSet &#FFA700<TableName> <ColumnName> <Value> &#59B503[Logical Condition]");
		setCmd.setDescription("Set the value of specified column in specified table. If you use logical condition only specified column fields will be set, otherwise all column will be set to the provided value");
		
		Command removeCmd = registerCommand("remove", new RecordRemove());
		removeCmd.setCorrectUsage("&#0094FFRemove &#FFA700<TableName> <Logical Condition>");
		removeCmd.setDescription("Remove records from the table. Logical condition is necessary to pervorm this command");
		
		Command addCmd = registerCommand("add", new RecordAdd());
		addCmd.setCorrectUsage("&#0094FFAdd &#FFA700<TableName> <ColumnName> <Value> &#59B503[Logical Condition]");
		addCmd.setDescription("Add provided value to record.");
		
		Command subtractCmd = registerCommand("subtract", new RecordSubtract());
		subtractCmd.setCorrectUsage("&#0094FFSubtract &#FFA700<TableName> <ColumnName> <Value> &#59B503[Logical Condition]");
		subtractCmd.setDescription("Subtract provided value to record.");
		
		Command exitCmd = registerCommand("exit", new Exit());
		exitCmd.setCorrectUsage("&#0094FFExit");
		exitCmd.setDescription("Turn off application.");
	}
	
	private static String enterDatabaseTypeMsg(ConsoleSender sender, Scanner scanner) {
		sender.sendMessageNl("&l&#FFB82BPlease enter what database type you want to manage.&r\n  &l&#0094FFsql&r\t\t - SQLite\n  &l&#0094FFmysql&r\t - MySQL database");
		String databaseType = scanner.nextLine().toLowerCase();
		sender.sendMessageNl("");
		return databaseType;
	}
	
	
	public static Logger getLogger() {
		return logger;
	}
	
	public static String getDatabaseType() {
		return databaseType;
	}
	
	public static CommandListener getCommandListener() {
		return commandListener;
	}
}
