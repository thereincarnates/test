package com.movieztalk.server;

public class ServerConfiguration {

	private ServerConfiguration() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private static ServerConfiguration instance = null;

	public static synchronized ServerConfiguration getInstance() {
		if (instance == null) {
			instance = new ServerConfiguration();
		}
		return instance;
	}

	public final String MYSQL_HOST = "localhost";
	public final String MYSQL_PORT = "3306"; // 3368 used in mysql
												// server, default is 3306
	public final String MYSQL_USER = "root";
	public final String MYSQL_PASSWD = "root";
	public final String MYSQL_MOVIE_DB_NAME = "movieztalk";

}
