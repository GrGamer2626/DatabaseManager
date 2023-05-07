package com.mcfan.database;

import com.mcfan.main.Main;

public abstract class QueryHandler {
	
	private static Database db = DatabaseConnection.getDatabase();
	private SQLQueries sqlQueries = new SQLQueries(db.getConnection(), Main.getDatabaseType());	
	
	
	public QueryHandler() {
		doQueries();
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	private void doQueries() {
	  	if(db.isClosed()) {
			db.connect();
			sqlQueries.setConnection(db.getConnection());
		}
		
		query();
		
		db.disconnect();
	}
	
	public SQLQueries getSqlQueries() {
		return sqlQueries;
	}
	
	public abstract void query();
}