package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.extraction.model.Movie;
import com.movieztalk.helper.MovieDetailsHelper;
import com.movieztalk.movieaspects.model.MovieAspect;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@WebServlet("/movieplot")
public class MoviePlotServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(MoviePlotServlet.class.getName());
	private static final long serialVersionUID = 1L;
	private ServerConfiguration serverConfiguration = ServerConfiguration.getInstance();
	private MovieDetailsHelper movieDetailsHelper = MovieDetailsHelper.getInstance();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String movieId = request.getParameter("movieId");
		response.setContentType("application/json");
		logger.info("Testing the data");
		Gson gson = new Gson();
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache("moviesDetailPage");
		Element element = cache.get(movieId);
		String json;
		if(element!=null){
			json = (String)element.getObjectValue();
		} else{
			json = gson.toJson(fetchMovieObject(movieId));
			cache.put(new Element(movieId, json));
		}
		PrintWriter out = response.getWriter();
		out.write(json);
		out.flush();
		out.close();
	}

	private Movie fetchMovieObject(String movieId) {

		if (!movieDetailsHelper.isMovieVisibleOnPortal(movieId)) {
			movieId = "";
		}

		if (Strings.isNullOrEmpty(movieId)) {
			movieId = movieDetailsHelper.fetchLatestMovieId();
		}

		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Movie movie = new Movie();
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + serverConfiguration.MYSQL_HOST + ":"
					+ serverConfiguration.MYSQL_PORT + "/" + serverConfiguration.MYSQL_MOVIE_DB_NAME + "?user="
					+ serverConfiguration.MYSQL_USER + "&password=" + serverConfiguration.MYSQL_PASSWD);
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from movie where movieid='" + movieId + "'");
			System.out.println("query being called is select * from movie where movieid='" + movieId + "'");
			while (resultSet.next()) {
				String name = resultSet.getString("name");
				movieId = resultSet.getString("movieid");
				String budget = resultSet.getString("budget");
				String boxOffice = resultSet.getString("boxoffice");
				String songsAndTrailers = resultSet.getString("songsandtrailers");
				String videoReviews = resultSet.getString("videoreviews");
				String interviewsAndEvents = resultSet.getString("interviewsandevents");
				String overallRating = resultSet.getString("overallrating");
				String storyRating = resultSet.getString("storyrating");
				String actingRating = resultSet.getString("actingrating");
				String directionRating = resultSet.getString("directionrating");
				String musicRating = resultSet.getString("musicrating");
				String imageUrl = resultSet.getString("imageurl");
				String plot = resultSet.getString("plot");
				String director = resultSet.getString("director");
				String actors = resultSet.getString("actors");
				String releaseDate = resultSet.getString("release_date");
				String writers = resultSet.getString("writers");

				Set<String> actorsSet = new HashSet<String>();
				for (String retval : actors.split(",")) {
					actorsSet.add(retval);
				}

				List<String> songsAndTrailersList = Arrays.asList(songsAndTrailers.split(","));
				List<String> interviewAndEventsList = Arrays.asList(interviewsAndEvents.split(","));
				List<String> videoReviewsList = Arrays.asList(videoReviews.split(","));
				movie.setInterviewAndEvents(interviewAndEventsList).setSongAndTrailers(songsAndTrailersList)
						.setVideoReviews(videoReviewsList).setReleaseDate(releaseDate).setPlot(plot).setName(name)
						.setBudget(budget).setActors(actorsSet).setBoxOffice(boxOffice).setImageUrl(imageUrl)
						.setDirector(director).setOverall(buildMovieAspect(movieId, "overall"))
						.setStory(buildMovieAspect(movieId, "story")).setActing(buildMovieAspect(movieId, "acting"))
						.setDirection(buildMovieAspect(movieId, "direction"))
						.setMusic(buildMovieAspect(movieId, "music")).setMovieId(movieId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}

		return movie;
	}

	private MovieAspect buildMovieAspect(String movieId, String compName) {
		MovieAspect aspect = new MovieAspect();
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String result = "";
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + serverConfiguration.MYSQL_HOST + ":"
					+ serverConfiguration.MYSQL_PORT + "/" + serverConfiguration.MYSQL_MOVIE_DB_NAME + "?user="
					+ serverConfiguration.MYSQL_USER + "&password=" + serverConfiguration.MYSQL_PASSWD);
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select tweetstr, compname, sentiment from Tweet_Table where movieid='"
					+ movieId + "' and is_visible_on_portal='Y'");
			System.out.println(
					"Query being passed is" + "select tweetstr, compname, sentiment from Tweet_Table where movieid='"
							+ movieId + "' and is_visible_on_portal='Y'");

			while (resultSet.next()) {
				if (resultSet.getString("compname").contains(compName)) {
					String sentiments = resultSet.getString("sentiment");
					if (Strings.isNullOrEmpty(sentiments)) {
						continue;
					}
					String[] sentimentArr = sentiments.split(",");
					System.out.println(sentiments);
					for (String sentimentToken : sentimentArr) {
						System.out.println(sentimentToken);
						String sentimentComp = sentimentToken.split(":")[0];
						String sentimentSentiment = sentimentToken.split(":")[1];
						if (sentimentComp.equalsIgnoreCase(compName)) {
							if (sentimentSentiment.equalsIgnoreCase("pos")) {
								aspect.getPositiveReviews().add(resultSet.getString("tweetstr"));
							} else {
								aspect.getNegativeReviews().add(resultSet.getString("tweetstr"));
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		if (aspect.getPositiveReviews().size() + aspect.getNegativeReviews().size() > 0) {
			double ratio = (double) aspect.getPositiveReviews().size()
					/ (double) (aspect.getNegativeReviews().size() + aspect.getPositiveReviews().size());
			int rating = (int) (ratio * 5);
			if (rating < 4) {
				rating = rating + 1;
			}
			aspect.setRating(rating);

		} else {
			aspect.setRating(1);
		}

		return aspect;
	}

	
}
