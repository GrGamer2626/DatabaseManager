package com.mcfan.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mcfan.loggers.DatabaseLogs;

//import loggers.DatabaseLogs;



public final class MySQL extends Database {
	
	final private  String prefix = "[MySQL]";
	
	private String serverIP = "localhost";
	private int port = 3306;
	private String dbName = "mysql";
	private String login = "login";
	private String password = "password";
	
	private Connection con;
	
	@Override
	public boolean connect() {
		DatabaseLogs.establishingConnectionMsg();
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			StringBuilder sb = new StringBuilder("jdbc:mysql://");
			sb.append(serverIP).append(":").append(port).append("/").append(dbName);
			String url = sb.toString();
			
			con = DriverManager.getConnection(url, login, password);
			
			DatabaseLogs.connectionEstablishedMsg();
			return true;
			
		}catch(ClassNotFoundException e) {
			DatabaseLogs.driverNotFoundMsg();
			return false;
			
			
		}catch(SQLException e) {
			DatabaseLogs.incorrectMySQLConnectionDataMsg();
			return false;
		}
	}
	
	@Override
	public Connection getConnection() {
		return con;
	}

	@Override
	public String getPrefix() {
		return prefix;
	}


	public void setServerIP(String serverIP) {
		if(!serverIP.equals("")) {
			this.serverIP = serverIP;
		}
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getDatabaseName() {
		return dbName;
	}
	
	public void setDatabaseName(String dbName) {
		if(!dbName.equals("")) {
			this.dbName = dbName;
		}
	}
	
	public void setLogin(String login) {
		if(!login.equals("")) {
			this.login = login;
		}
	}
	
	public void setPassword(String password) {
		if(!password.equals("")) {
			this.password = password;
		}
	}
}
