package com.movieztalk.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import com.google.gson.Gson;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.helper.MovieDetailsHelper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@WebServlet("/movieReview")
public class MovieReviewServlet extends HttpServlet {

	private ServerConfiguration serverConfiguration = ServerConfiguration.getInstance();
	private MovieDetailsHelper movieDetailHelper = MovieDetailsHelper.getInstance();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String movieId = request.getParameter("movieId");

		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("movieReviews");
		Element element = cache.get(movieId);
		String json;
		if (element != null) {
			json = (String) element.getObjectValue();
			response.getWriter().println(json);
			return;
		}

		String reviewJSon = fetchMovieReviews(movieId);
		cache.put(new Element(movieId, reviewJSon));
		response.getWriter().println(reviewJSon);
		response.getWriter().close();
	}
	
	public String fetchMovieReviews(String movieId){
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Map<String, List<Map<String, String>>> compNameToReviewsSentimentMap = new HashMap<>();
		
		if (!movieDetailHelper.isMovieVisibleOnPortal(movieId)) {
			movieId = "";
		}
		
		if(Strings.isNullOrEmpty(movieId)){
			movieId = movieDetailHelper.fetchLatestMovieId();
		}
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + serverConfiguration.MYSQL_HOST + ":"
					+ serverConfiguration.MYSQL_PORT + "/" + serverConfiguration.MYSQL_MOVIE_DB_NAME + "?user="
					+ serverConfiguration.MYSQL_USER + "&password=" + serverConfiguration.MYSQL_PASSWD);
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select tweetstr, compname, sentiment from Tweet_Table where movieid='"
					+ movieId + "' and is_visible_on_portal='Y'");
			while (resultSet.next()) {
				String sentiment = resultSet.getString("sentiment");
				String compName = resultSet.getString("compname");
				String sentiments[] = sentiment.split(",");
				String compNames[] = compName.split(",");
				for (int i = 0; i < sentiments.length; i++) {
					String review = resultSet.getString("tweetstr");
					String cname = compNames[0];
					if (sentiments[0].split(":").length != 2) {
						continue;
					}
					String sName = sentiments[0].split(":")[1];
					Map<String, String> map = new HashMap<>();
					map.put(review, sName);
					if (!compNameToReviewsSentimentMap.containsKey(cname)) {
						List<Map<String, String>> reviewSentimentList = new ArrayList<>();
						reviewSentimentList.add(map);
						compNameToReviewsSentimentMap.put(cname, reviewSentimentList);
					} else {
						compNameToReviewsSentimentMap.get(cname).add(map);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return new Gson().toJson(compNameToReviewsSentimentMap);
	}
}
