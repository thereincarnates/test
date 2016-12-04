package com.movieztalk.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.movieztalk.db.DatabaseHelper;

public class ConfigurationHelper {
	
	public enum ConfigurationKey{
		DROPBOXAUTHORIZATION
		;
	}

	private static ConfigurationHelper instance = new ConfigurationHelper();
	private static Map<String, String> paramterToValueMap = new HashMap<>();

	public static ConfigurationHelper getInstance() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from configurations");
			while (resultSet.next()) {
				String parameter = resultSet.getString("parameter");
				String value = resultSet.getString("value");
				paramterToValueMap.put(parameter, value);
			}
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return instance;
	}

	public Map<String, String> getParamterToValueMap() {
		return paramterToValueMap;
	}
}
