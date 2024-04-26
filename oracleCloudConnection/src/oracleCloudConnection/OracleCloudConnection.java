/**
 * 
 */
package oracleCloudConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

public class OracleCloudConnection {
	
	final static String DB_URL="jdbc:oracle:thin:@db202101290850_medium?TNS_ADMIN=D:/cloud_connection/Wallet_DB2021012999220/";
	final static String DB_USER = "user_01";
	final static String DB_PASSWORD = "UserPassword1";
	
	public static void main(String[] args) throws SQLException {
		
		Properties info = new Properties();
		info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
		info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
		info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");
		
		OracleDataSource ods = new OracleDataSource();
		ods.setURL(DB_URL);
		ods.setConnectionProperties(info);
		
		try(OracleConnection connection = (OracleConnection) ods.getConnection()){
			
			displayDatabaseDetails(connection);
			displayEmployees(connection);
			displayManagers(connection);
		
		}
		
	}
	
	public static void displayDatabaseDetails(Connection connection) throws SQLException{
		DatabaseMetaData dbmd = connection.getMetaData();
		System.out.println("Driver Name: " + dbmd.getDriverName());
		System.out.println("Driver Version: " + dbmd.getDriverVersion());
		System.out.println("Default Row Prefetch Value is: " 
		+ ((OracleConnection) connection).getDefaultRowPrefetch());
		System.out.println("Database Username is: " 
		+ ((OracleConnection) connection).getUserName());
		System.out.println();

	}
	
	public static void displayEmployees(Connection connection) throws SQLException{
		try(Statement statement = connection.createStatement()){
			try(ResultSet resultSet = statement.executeQuery("SELECT first_name, last_name FROM employees")){
				System.out.println("First Name" + " " + "Last Name");
				System.out.println("---------------");
				while(resultSet.next())
					System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + " ");
			}
		}
	}
	
	public static void displayManagers(Connection connection) throws SQLException{
		try(Statement statement = connection.createStatement()){
			try(ResultSet resultSet = statement.executeQuery("select e.first_name||' '||e.last_name||' is managed by '||m.first_name||' '||m.last_name FROM employees e JOIN employees m ON (e.manager_id = m.employee_id)")){
				System.out.println("\nManagement Information");
				System.out.println("---------------");
				while(resultSet.next())
					System.out.println(resultSet.getString(1));
			}
		}
	}

}
