package com.movieztalk.server.game;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.datastore.client.DatastoreException;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.movieztalk.db.DatabaseHelper;
import com.movieztalk.game.builder.GuessMovieNameGameBuilder;
import com.movieztalk.game.model.GuessMovieNameGame;

@SuppressWarnings("serial")
@WebServlet("/moviegameinit")
public class MovieGameInitializerServlet extends HttpServlet {

	public static Set<Integer> usedURLs = Sets.newHashSet();
	public static final Map<String, GuessMovieNameGame> initiatorIdToGameObjMap = new HashMap<>();
	public static final Map<String, GuessMovieNameGame> playerIdToGameObjMap = new HashMap<>();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");

		try {
			GuessMovieNameGame game = new GuessMovieNameGameBuilder().buildGameState().buildInitiatorId()
					.buildMovieName().buildOtherPlayerId().build();
			storeGuessMovieNameGame(game);
			Gson gson = new Gson();
			String json = gson.toJson(game);
			PrintWriter out = response.getWriter();
			out.write(json);
			out.flush();
			out.close();
		} catch (DatastoreException e) {
			e.printStackTrace();
		}
	}

	public void storeGuessMovieNameGame(GuessMovieNameGame game) {
		Connection connect = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/movieztalk?" + "user=root&password=root");
			preparedStatement = connect
					.prepareStatement("insert into  movieztalk.guessmoviename (playerid, otherplayerid) values(?,?)");
			preparedStatement.setString(1, game.getInitiatorId());
			preparedStatement.setString(2, game.getOtherPlayerId());
			preparedStatement.execute();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseHelper.getInstance().closeResources(connect, preparedStatement, resultSet);
		}
	}
}
