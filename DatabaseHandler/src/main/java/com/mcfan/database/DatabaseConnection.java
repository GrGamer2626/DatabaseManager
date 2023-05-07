package com.mcfan.database;

import java.util.Scanner;

import com.mcfan.loggers.DatabaseLogs;
import com.mcfan.main.ConsoleSender;
import com.mcfan.main.ValidateDatabaseContent;



public class DatabaseConnection {
	
	private static Database db;
	private String dbType;
	private ConsoleSender sender;
	
	public DatabaseConnection(ConsoleSender sender, String dbType) {
		this.dbType = dbType;
		this.sender = sender;
	}

	public void connectToDatabase(Scanner scanner) {
		switch(dbType.toLowerCase()) {
		case "sql":
			db = connectToSQLite(scanner);
			break;
			
		case "mysql":
			db = connectToMySQL(scanner);
			break;
		
		default:
			DatabaseLogs.incorrectDatabaseTypeMsg();
			break;
		}
		ValidateDatabaseContent.updateAll();
	}
	
	private Database connectToSQLite(Scanner scanner) {
		SQLite db = new SQLite();
		
		sender.sendMessageNl("Please provide the exact path to your SQLite database along with the filename and extension \n&#59B503&lExample:&r some/example/path/toDatabase/database.db \nIf provided path or file does not exist, it will be created.");
		String path = scanner.nextLine();
		db.setPathToDatabase(path);
		
		boolean isConnected = db.connect();
		sender.sendMessageNl("");
		while(!isConnected) {
			sender.sendMessageNl("&#FF0000Something went wrong!");
			sender.sendMessageNl("Please provide the exact path to your SQLite database along with the filename and extension \n&#59B503&lExample:&r some/example/path/toDatabase/database.db");
			path = scanner.nextLine();
			db.setPathToDatabase(path);
			
			isConnected = db.connect();
			sender.sendMessageNl("");
		}
		return db;
	}
	
	private Database connectToMySQL(Scanner scanner) {
		MySQL db = new MySQL();
		
		setMySQlFields(db, scanner);
		boolean isConnected = db.connect();
	
		while(!isConnected) {
			sender.sendMessageNl("Some of provided datas are incorrect!");
			setMySQlFields(db, scanner);
			
			isConnected = db.connect();
		}
		return db;
	}

	
	private void setMySQlFields(MySQL db, Scanner scanner) {
		sender.sendMessageNl("Please provide data reqiuer to connetct to MySql database");
		sender.sendMessageNl("If you want to keep the value from the previous provide just click enter button without entering any text");
		sender.sendMessageNl("");
		sender.sendMessageNl("========== Providing MySQl connection data ==========");
		sender.sendMessage("&l&#FFB82BServer IP: ");
		db.setServerIP(scanner.nextLine());
		
		boolean isPort = false;
		while(!isPort) {
			try {
				handlePort(db, scanner);
				isPort = true;
				
			}catch(NumberFormatException  e) {
				sender.sendMessage("Provided value is not port number! Enter correct port value!");
				isPort = false;
			}
		}
		
		sender.sendMessage("&l&#FFB82BDatabase name: ");
		db.setDatabaseName(scanner.nextLine());
		
		sender.sendMessage("&l&#FFB82BUser name/login: ");
		db.setLogin(scanner.nextLine());
		
		sender.sendMessage("&l&#FFB82BPassword: ");
		db.setPassword(scanner.nextLine());
		sender.sendMessageNl("");
	}

	private void handlePort(MySQL db, Scanner scanner) throws NumberFormatException {
		sender.sendMessage("&l&#FFB82BPort: ");
		String portText = scanner.nextLine();
		if(!portText.equals("")) {
			int port = Integer.parseInt(portText);
			db.setPort(port);
		}
	}
	
	public static Database getDatabase() {
		return db;
	}
	
	public static void setDatabase(Database db) {
		DatabaseConnection.db = db;
	}

	public String getDatabaseType() {
		return dbType;
	}
	
	public void setDatabaseType(String dbType) {
		this.dbType = dbType;
	}
}
