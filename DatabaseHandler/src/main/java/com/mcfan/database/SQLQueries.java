package com.mcfan.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mcfan.loggers.DatabaseLogs;
import com.mcfan.messages.Builder;


public class SQLQueries {
	
	private Connection con;
	private String dbType;
	
	public SQLQueries(Connection con, String dbType) {
		this.con = con;
		this.dbType = dbType;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setConnection(Connection con) {
		this.con = con;
	}

	public boolean containTable(String tableName) {
		if(dbType.equalsIgnoreCase("sql")) {
			try {
				ResultSet rs = con.getMetaData().getTables(null, null, tableName, null);
				return rs.next();
				
			}catch (SQLException e) {
				return false;
			}
		}else if(dbType.equalsIgnoreCase("mysql")) {
			Builder query = new Builder("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '{databaseName}' AND table_name = '{tableName}'")
					.replace("{databaseName}",((MySQL) DatabaseConnection.getDatabase()).getDatabaseName())
					.replace("{tableName}", tableName);
			try {
				PreparedStatement ps = con.prepareStatement(query.toString());
				
				ResultSet rs = ps.executeQuery();
				rs.next();
				return (rs.getInt(1) > 0);
				
			}catch (SQLException e) {
				return false;
			}
		}
		return false;
	}
	
	public void createTable(String tableName, String records) {
		if(!containTable(tableName)) {
			DatabaseLogs.tableCreating(tableName);
			Builder query = new Builder("CREATE TABLE IF NOT EXISTS {tableName} ({records})")
					.replace("{tableName}", tableName)
					.replace("{records}", records);
			try {
				PreparedStatement ps = con.prepareStatement(query.toString());
				ps.execute();
				ps.close();
				DatabaseLogs.tableCreatedSuccessfully(tableName);
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void removeTable(String tableName) {
		if(containTable(tableName)) {
			DatabaseLogs.tableRemoving(tableName);
			Builder query = new Builder("DROP TABLE IF EXISTS {tableName}")
					.replace("{tableName}", tableName);
			try {
				PreparedStatement ps = con.prepareStatement(query.toString());
				ps.execute();
				ps.close();
				DatabaseLogs.tableRemovedSuccessfully(tableName);
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void addColumnToTable(String tableName, String column, String dataType, String extraParametr) {
		Builder query = new Builder("ALTER TABLE {tableName} ADD {handleDBType} {column} {dataType} {extraParametr}")
				.replace("{tableName}", tableName)
				.replace("{column}", column)
				.replace("{dataType}", dataType)
				.replace("{extraParametr}", extraParametr);
		
		if(dbType.equalsIgnoreCase("sql")) {
			query.replace("{handleDBType}", "COLUMN");
			
		}else if(dbType.equalsIgnoreCase("mysql")) {
			query.replace("{handleDBType}", "IF NOT EXISTS");
		}
		
		try {
			PreparedStatement ps = con.prepareStatement(query.toString());
			
			ps.execute();
			ps.close();
			DatabaseLogs.addingColumnToTable(tableName, column, dataType);
			
		}catch (SQLException e) {
			if(!e.getMessage().contains("duplicate column name:")) {
				e.printStackTrace();
			}
		}
	}
	
	public void addColumnToTable(String tableName, String column, String dataType) {
		Builder query = new Builder("ALTER TABLE {tableName} ADD {handleDBType} {column} {dataType}")
				.replace("{tableName}", tableName)
				.replace("{column}", column)
				.replace("{dataType}", dataType);
		
		if(dbType.equalsIgnoreCase("sql")) {
			query.replace("{handleDBType}", "COLUMN");
			
		}else if(dbType.equalsIgnoreCase("mysql")) {
			query.replace("{handleDBType}", "IF NOT EXISTS");
		}
		try {
			PreparedStatement ps = con.prepareStatement(query.toString());
			
			ps.execute();
			ps.close();
			DatabaseLogs.addingColumnToTable(tableName, column, dataType);
			
		}catch (SQLException e) {
			if(!e.getMessage().contains("duplicate column name:")) {
				e.printStackTrace();
			}
		}
	}

	public Set<String> getTableNameSet() {
		Set<String> tableNames = new HashSet<>();
		try {
			String[] types = {"TABLE"};
			ResultSet rs = con.getMetaData().getTables(null, null, "%", types);
			while(rs.next()) {
				tableNames.add(rs.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tableNames;
	}
	
	public Map<String, String> getColumnMap(String tableName) {
		Map<String,String> columns = new HashMap<>();
		try {
			ResultSet rs = con.getMetaData().getColumns(null, null, tableName, null);
			
			while(rs.next()) {
				String columnType = rs.getString("TYPE_NAME");
				if("CHAR".equalsIgnoreCase(columnType)) {
					Builder charType = new Builder(columnType).toUpperCase()
							.append(" ")
							.append(rs.getString("COLUMN_SIZE"));
					
					columns.put(rs.getString("COLUMN_NAME"), charType.toString());
					continue;
				}
				columns.put(rs.getString("COLUMN_NAME"), rs.getString("TYPE_NAME").toUpperCase());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return columns;
	}

	public void insertToTable(String tableName, String columns, String values) {
		Builder query = new Builder("INSERT INTO {tableName}({columns}) VALUES ({values})")
				.replace("{tableName}", tableName)
				.replace("{columns}", columns)
				.replace("{values}", values);
		
		try {
			PreparedStatement ps = con.prepareStatement(query.toString());
			
			ps.executeUpdate();
		
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeRecord(String tableName, String where) {
		Builder query = new Builder("DELETE FROM {tableName} WHERE ({where})")
				.replace("{tableName}", tableName)
				.replace("{where}", where);
		try {
			PreparedStatement ps = con.prepareStatement(query.toString());
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<List<String>> getAllTable(String tableName) {
		Builder query = new Builder("SELECT * FROM {tableName}")
				.replace("{tableName}", tableName);
		
		try { 
			PreparedStatement ps = con.prepareStatement(query.toString());
			return getAllRow(ps.executeQuery());
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	
	public List<List<String>> getAllTable(String tableName, String where) {
		Builder query = new Builder("SELECT * FROM {tableName} WHERE ({where})")
				.replace("{tableName}", tableName)
				.replace("{where}", where);
		
		try { 
			PreparedStatement ps = con.prepareStatement(query.toString());
			return getAllRow(ps.executeQuery());
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	
	public List<List<String>> getRecord(String tableName, String column) {
		try {
			Builder query = new Builder("SELECT * FROM {tableName}")
					.replace("{tableName}", tableName);
			
			PreparedStatement ps = con.prepareStatement(query.toString());
			return getRow(ps.executeQuery(), tableName, column);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	
	public List<List<String>> getRecord(String tableName, String column, String where) {
		try { 
			Builder query = new Builder("SELECT * FROM {tableName} WHERE ({where})")
					.replace("{tableName}", tableName)
					.replace("{where}", where);
			
			PreparedStatement ps = con.prepareStatement(query.toString());
			return getRow(ps.executeQuery(), tableName, column);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	
	public void setRecord(String tableName, String column, Object value) {
		Builder query = new Builder("UPDATE {tableName} SET {column}={value}")
				.replace("{tableName}", tableName)
				.replace("{column}", column)
				.replace("{value}", String.valueOf(value));
		try {
			PreparedStatement ps = con.prepareStatement(query.toString());
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public void setRecord(String tableName, String column, Object value, String where) {
		Builder query = new Builder("UPDATE {tableName} SET {column}={value} WHERE ({where})")
				.replace("{tableName}", tableName)
				.replace("{column}", column)
				.replace("{value}", String.valueOf(value))
				.replace("{where}", where);
		try {
			PreparedStatement ps = con.prepareStatement(query.toString());
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public void addValeue(String tableName, String column, String value) {
		Builder query = new Builder("UPDATE {tableName} SET {column}={column}+{value}")
				.replace("{tableName}", tableName)
				.replace("{column}", column)
				.replace("{value}", String.valueOf(value));
		try {
			PreparedStatement ps = con.prepareStatement(query.toString());
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public void addValeue(String tableName, String column, String value, String where) {
		Builder query = new Builder("UPDATE {tableName} SET {column}={column}+{value} WHERE ({where})")
				.replace("{tableName}", tableName)
				.replace("{column}", column)
				.replace("{value}", String.valueOf(value))
				.replace("{where}", where);
		try {
			PreparedStatement ps = con.prepareStatement(query.toString());
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public void subtractValeue(String tableName, String column, String value) {
		Builder query = new Builder("UPDATE {tableName} SET {column}={column}-{value}")
				.replace("{tableName}", tableName)
				.replace("{column}", column)
				.replace("{value}", String.valueOf(value));
		try {
			PreparedStatement ps = con.prepareStatement(query.toString());
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public void subtractValeue(String tableName, String column, String value, String where) {
		Builder query = new Builder("UPDATE {tableName} SET {column}={column}-{value} WHERE ({where})")
				.replace("{tableName}", tableName)
				.replace("{column}", column)
				.replace("{value}", String.valueOf(value))
				.replace("{where}", where);
		try {
			PreparedStatement ps = con.prepareStatement(query.toString());
			ps.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private List<String> getAllLabeles(ResultSetMetaData metaData) throws SQLException  {
		List<String> labels = new ArrayList<>();
		int columnCount = metaData.getColumnCount();
		for(int i = 1; i <= columnCount; i++) {
			labels.add(metaData.getColumnLabel(i));
		}
		return labels;
	}

	private List<String> getLabeles(String idLabel, String column) throws SQLException {
		List<String> labels = new ArrayList<>();
		if(idLabel != null) {
			labels.add(idLabel);
		}
		labels.add(column);
		
		return labels;
	}
	

	private String getPrimaryColumnLabele(String tableName) throws SQLException {
		DatabaseMetaData dmd = con.getMetaData();
		ResultSet rs = dmd.getPrimaryKeys(null, null, tableName);
		
		String idLabel = null;
		if(rs.next()) {
			idLabel = rs.getString("COLUMN_NAME");
		}
		
		return idLabel;
	}
	

	private List<List<String>> getAllRow(ResultSet rs) throws SQLException {
		List<List<String>> rows = new ArrayList<>();
		
		ResultSetMetaData metaData = rs.getMetaData();
		rows.add(getAllLabeles(metaData));
		
		int columnCount = metaData.getColumnCount();
		while(rs.next()) {
			List<String> row = new ArrayList<>();
			for(int i = 1; i <= columnCount; i++) {
				Object value = rs.getObject(i);
				row.add(value == null ? "Empty" : String.valueOf(value));
			}
			rows.add(row);
		}
		return rows;
	}
	

	private List<List<String>> getRow(ResultSet rs, String tableName, String column) throws SQLException {
		List<List<String>> rows = new ArrayList<>();
		
		String idLabel = getPrimaryColumnLabele(tableName);
		rows.add(getLabeles(idLabel, column));
		
		while(rs.next()) {
			List<String> row = new ArrayList<>();
			if(idLabel != null) {
				row.add(String.valueOf(rs.getObject(idLabel)));
			}
			Object value = rs.getObject(column);
			row.add((value == null) ? "Empty" : String.valueOf(value));
			rows.add(row);
		}
		return rows;
	}
	
}
