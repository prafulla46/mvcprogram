package com.student.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {

	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/student";
	private static String username = "root";
	private static String password ="India@123";
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		
		//1 : Load the driver 
		Class.forName(driver);
		
		//2 : create connection
		Connection conn = DriverManager.getConnection(url, username, password);
		if(conn !=null){
			System.out.println("connection has been created");
		}
		return conn;
	}
}
