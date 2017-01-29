package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
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
import com.movieztalk.movieaspects.model.MovieAspect;

@WebServlet("/movieplot")
public class MoviePlotServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(MoviePlotServlet.class.getName());
	private static final long serialVersionUID = 1L;
	ServerConfiguration mysqlserver = new ServerConfiguration();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String movieId = request.getParameter("movieid");
		response.setContentType("text/html");
		logger.info("Testing the data");
		Gson gson = new Gson();
		String json = gson.toJson(buildMovieObject(movieId));
		PrintWriter out = response.getWriter();
		out.write(json);
		out.flush();
		out.close();
	}

	private Movie buildMovieObject(String movieId) {

		if (!isMovieVisibleOnPortal(movieId)) {
			movieId = "";
		}

		if (Strings.isNullOrEmpty(movieId)) {
			movieId = fetchLatestMovieId();
		}
		
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Movie movie = new Movie();
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + mysqlserver.mysqlServerName + ":"
					+ mysqlserver.mysqlServerPort + "/" + mysqlserver.mysqlDBName + "?user="
					+ mysqlserver.mysqlServerUserName + "&password=" + mysqlserver.mysqlServerPassword);
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from movie where movieid='" + movieId + "'");

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

				List<String> songsAndTrailersList = Arrays.asList(songsAndTrailers.split(","));
				List<String> interviewAndEventsList = Arrays.asList(interviewsAndEvents.split(","));
				List<String> videoReviewsList = Arrays.asList(videoReviews.split(","));
				movie.setInterviewAndEvents(interviewAndEventsList).setSongAndTrailers(songsAndTrailersList)
						.setVideoReviews(videoReviewsList).setPlot(plot).setName(name).setBudget(budget)
						.setBoxOffice(boxOffice).setImageUrl(imageUrl).setDirector(director)
						.setOverall(new MovieAspect().setRating(Double.parseDouble(overallRating)))
						.setStory(new MovieAspect().setRating(Double.parseDouble(storyRating)))
						.setActing(new MovieAspect().setRating(Double.parseDouble(actingRating)))
						.setDirection(new MovieAspect().setRating(Double.parseDouble(directionRating)))
						.setMusic(new MovieAspect().setRating(Double.parseDouble(musicRating)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}

		/*
		 * MovieAspect ms = new MovieAspect(); Set<String> reviews =	
		 * Sets.newHashSet("This is test1", "This is test2", "This is test3",
		 * "This is test4", "This is test5", "This is test 6");
		 * ms.getNegativeReviews().addAll(reviews);
		 * ms.getPositiveReviews().addAll(reviews);
		 * 
		 * MovieAspect music = new MovieAspect(); reviews =
		 * Sets.newHashSet("This is music1", "this is music 2",
		 * "this is music 3"); music.getNegativeReviews().addAll(reviews);
		 * music.getPositiveReviews().addAll(reviews);
		 * 
		 * Movie movie = new Movie();
		 * movie.setActing(null).setActors(null).setBoxOffice("1 crore").
		 * setBudget("50 Lakhs") .setPlot(
		 * "Tanu Weds Manu Returns is a 2015 Indian romantic comedy drama film directed by Anand L. Rai which serves as a sequel to the 2011 film Tanu Weds Manu. R. Madhavan, Kangana Ranaut, Jimmy Shergill, Deepak Dobriyal, Swara Bhaskar and Eijaz Khan reprise their roles from the original film. Ranaut also portrays the additional role of a Haryanvi athlete in it. The story, screenplay and the dialogues were written by Himanshu Sharma. The soundtrack and film score were composed by Krsna Solo and the lyrics were penned by Rajshekhar. Saroj Khan and Bosco–Caesar were the film′s choreographers while the editing was done by Hemal Kothari. The principle photography began on October 2014 and the film was released on 22 May 2015. The film carries the story forward showing the next chapter of the couple's life. Tanu Weds Manu Returns received positive reviews from critics and Ranaut's performance was particularly praised.[3][4] Made on a budget of ₹30 crore (US$4.5 million), the film earned ₹252 crore (US$38 million)"
		 * +
		 * " worldwide and became one of the highest grossing Bollywood film.")
		 * .setOverall(ms).setSongAndTrailers(songsAndTrailers).
		 * setInterviewAndEvents(interviewAndEvents)
		 * .setVideoReviews(videoReviews).setStory(ms).setActing(ms).
		 * setDirection(ms).setMusic(music);
		 */
		return movie;
	}

	private boolean isMovieVisibleOnPortal(String movieId) {
		if (Strings.isNullOrEmpty(movieId)) {
			return false;
		}

		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String result = "";
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + mysqlserver.mysqlServerName + ":"
					+ mysqlserver.mysqlServerPort + "/" + mysqlserver.mysqlDBName + "?user="
					+ mysqlserver.mysqlServerUserName + "&password=" + mysqlserver.mysqlServerPassword);
			statement = connect.createStatement();
			resultSet = statement.executeQuery(
					"select movieid from movie where movieid='" + movieId + "' and is_visible_on_portal='Y'");
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return false;
	}

	private String fetchLatestMovieId() {

		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String result = "";
		try {
			connect = DriverManager.getConnection("jdbc:mysql://" + mysqlserver.mysqlServerName + ":"
					+ mysqlserver.mysqlServerPort + "/" + mysqlserver.mysqlDBName + "?user="
					+ mysqlserver.mysqlServerUserName + "&password=" + mysqlserver.mysqlServerPassword);
			statement = connect.createStatement();
			resultSet = statement.executeQuery(
					"select movieid from movie where is_visible_on_portal='Y' order by movieid desc limit 1");
			while (resultSet.next()) {
				result = resultSet.getString("movieid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, statement, resultSet);
		}
		return result;
	}
}
