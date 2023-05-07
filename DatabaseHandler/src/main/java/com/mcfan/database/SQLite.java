package com.mcfan.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mcfan.loggers.DatabaseLogs;
import com.mcfan.loggers.FilesLogs;

public final class SQLite extends Database {
	
	private static String prefix = "[SQL]";
	private Connection con;
	private String pathToDatabase;
	

	
	@Override
	public boolean connect() {
		File database = new File(pathToDatabase);
		
		if(!database.getParentFile().exists()) {
			FilesLogs.directoryGeneratingMsg(pathToDatabase);
			try {
				database.getParentFile().mkdir();
				FilesLogs.directoryGeneratedSuccessfullyMsg(pathToDatabase);
				
			}catch(Exception e) {
				FilesLogs.directoryGeneratingFailedMsg();
				e.printStackTrace();
				return false;
			}
		}
		DatabaseLogs.establishingConnectionMsg();
		try {
			Class.forName("org.sqlite.JDBC");
			
			StringBuilder sb = new StringBuilder("jdbc:sqlite:");
			sb.append(database);
			
			con = DriverManager.getConnection(sb.toString());
			DatabaseLogs.connectionEstablishedMsg();
			return true; 
			
		}catch(ClassNotFoundException e) {
			DatabaseLogs.driverNotFoundMsg();
			return false; 
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void setPathToDatabase(String pathToDatabase) {
		this.pathToDatabase = pathToDatabase;
	}
	
	@Override
	public Connection getConnection() {
		return con;
	}

	@Override
	public String getPrefix() {
		return prefix;
	}
}
