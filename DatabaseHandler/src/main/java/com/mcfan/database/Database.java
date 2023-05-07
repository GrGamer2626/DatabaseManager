package com.mcfan.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.mcfan.loggers.DatabaseLogs;



public abstract class Database {
	
	public abstract boolean connect();
	public abstract Connection getConnection();
	public abstract String getPrefix();
	
	public void disconnect() {
		try{
			if(!getConnection().isClosed()) {
				getConnection().close();
				DatabaseLogs.connectionCloseMsg();
				
			}else {
				DatabaseLogs.noOpenConnectionMsg();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isClosed() {
		try {
			return getConnection().isClosed();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
