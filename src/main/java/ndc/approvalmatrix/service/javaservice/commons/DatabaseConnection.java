package ndc.approvalmatrix.service.javaservice.commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	public Connection getDatabaseConnection()  {

        String DB_URL = "jdbc:mysql://119.160.107.35:11001/dbxdb";
        Connection connection = null;
		try {
			connection = DriverManager.getConnection(DB_URL, "developer", "dEeVel0peR!110");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return  connection;

    }

	public Connection getDatabaseConnection(String hostUrl,String dbxPort,String dbxSchemaName,String dbxDbUsername,String dbxDbPassword)  {

		String DB_URL = "jdbc:mysql://"+hostUrl+":"+dbxPort+"/"+dbxSchemaName;
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DB_URL, dbxDbUsername, dbxDbPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return  connection;

	}
}
