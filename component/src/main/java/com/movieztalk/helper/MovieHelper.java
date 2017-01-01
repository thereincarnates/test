package com.movieztalk.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.movieztalk.db.DatabaseHelper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class MovieHelper {

	private static MovieHelper instance = new MovieHelper();

	public static MovieHelper getInstance() {
		return instance;
	}

	public Set<String> fetchMovieNamesFromDB(String industryType) {
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("movies");
		Element element = cache.get(industryType);
		if (element != null) {
			return (Set<String>) element.getObjectValue();
		}
		Set<String> movieNames = new HashSet<>();
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			System.out.println("fetching the names of movies of a industry type" + industryType);
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select name from movie where industrytype='" + industryType + "'");
			while (resultSet.next()) {
				String movieName = resultSet.getString("name");
				movieNames.add(movieName);
			}
			cache.put(new Element(industryType, movieNames));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return movieNames;
	}

	public static void main(String args[]) {
		MovieHelper movieHelper = MovieHelper.instance;
		Stopwatch stopwatch = Stopwatch.createStarted();
		Set<String> movies = movieHelper.fetchMovieNamesFromDB("bollywood");
		System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
		
		
		Stopwatch stopwatch1 = Stopwatch.createStarted();
		Set<String> movies1 = movieHelper.fetchMovieNamesFromDB("bollywood");
		System.out.println(stopwatch1.elapsed(TimeUnit.MILLISECONDS));
		for (String str : movies1) {
			System.out.println(str);
		}
	}
}
