package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.movieztalk.game.model.GuessMovieNameGame;

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
    GuessMovieNameGame game = GuessMovieNameGame.build();
    initiatorIdToGameObjMap.put(String.valueOf(game.getInitiatorId()), game);
    playerIdToGameObjMap.put(String.valueOf(game.getInitiatorId()), game);
    Gson gson = new Gson();
    String json = gson.toJson(game);
    PrintWriter out = response.getWriter();
    out.write(json);
    out.flush();
    out.close();
  }
}
