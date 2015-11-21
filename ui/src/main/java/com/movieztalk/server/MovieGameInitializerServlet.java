package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.movieztalk.game.model.GuessMovieNameGame;

public class MovieGameInitializerServlet extends HttpServlet {
  public static Set<Integer> usedURLs = Sets.newHashSet();
  private final Map<Integer, GuessMovieNameGame> initiatorIdToGameObjMap = new HashMap<>();
  private final Map<Integer, GuessMovieNameGame> playerIdToGameObjMap = new HashMap<>();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    String isMultiPlayer = request.getParameter("isMultiPlayer");
    GuessMovieNameGame game = GuessMovieNameGame.build();
    initiatorIdToGameObjMap.put(game.getInitiatorId(), game);
    playerIdToGameObjMap.put(game.getOtherPlayerId(), game);
    Gson gson = new Gson();
    String json = gson.toJson(game);
    PrintWriter out = response.getWriter();
    out.write(json);
  }
}
