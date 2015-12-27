package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.datastore.client.DatastoreException;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.movieztalk.game.builder.GuessMovieNameGameBuilder;
import com.movieztalk.game.model.GuessMovieNameGame;
import com.movieztalk.helper.DataStoreHelper;

@SuppressWarnings("serial")
public class MovieGameInitializerServlet extends HttpServlet {

  public static Set<Integer> usedURLs = Sets.newHashSet();
  public static final Map<String, GuessMovieNameGame> initiatorIdToGameObjMap = new HashMap<>();
  public static final Map<String, GuessMovieNameGame> playerIdToGameObjMap = new HashMap<>();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("application/json");
    String isMultiPlayer = request.getParameter("isMultiPlayer");

    GuessMovieNameGame game;
    try {
      game = new GuessMovieNameGameBuilder().buildGameState().buildInitiatorId().buildMovieName()
          .buildOtherPlayerId().build();

      Map<String, String> propertyMap = new HashMap<>();
      for(Field field : game.getClass().getDeclaredFields()){
        field.setAccessible(true);
        try {
          propertyMap.put(field.getName().toLowerCase(), (String)field.get(game));
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      propertyMap.put("scoreboard", "");
      String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
      propertyMap.put("time", timeStamp);
      DataStoreHelper.storeData(propertyMap, "gamestate");
      Gson gson = new Gson();
      String json = gson.toJson(game);
      PrintWriter out = response.getWriter();
      System.out.println("Start" + new Date());
      out.write(json);
      out.flush();
      out.close();
    } catch (DatastoreException e) {
      e.printStackTrace();
    }
  }
}
