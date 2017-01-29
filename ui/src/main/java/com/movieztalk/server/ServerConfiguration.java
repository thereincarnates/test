package com.movieztalk.server;


public class ServerConfiguration {
	
	public ServerConfiguration(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public static final String mysqlServerName = "localhost";
	public static final String mysqlServerPort = "3306";  //3368 used in mysql server, default is 3306
	public static final String mysqlServerUserName = "root";
	public static final String mysqlServerPassword = "root";
	public static final String mysqlDBName = "movieztalk";

}
