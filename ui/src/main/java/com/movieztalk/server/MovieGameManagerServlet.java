package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.movieztalk.game.model.MovieRequest;
import com.movieztalk.game.model.ScoreBoard;

@SuppressWarnings("serial")
public class MovieGameManagerServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String intiatorId = request.getParameter("initiatorid");
    String gameState = request.getParameter("gamestate");
    String preint = request.getParameter("preinit");
    String id = request.getParameter("id");
    System.out.println(preint + id);

    MovieRequest movieRequest = new MovieRequest();
    movieRequest.setMoviename("PYAAR TO HONA HI THA");
    ScoreBoard scoreBoard = new ScoreBoard();
    scoreBoard.getScores().add(0);
    scoreBoard.setTotalScore("0");
    scoreBoard.setTotalGamePlayed("0");
    movieRequest.setScoreboard(scoreBoard);

    Gson gson = new Gson();

    PrintWriter out = response.getWriter();
    out.write(gson.toJson(movieRequest));
    out.flush();
    out.close();

    /*
     * PrintWriter out = response.getWriter();
     * 
     * if (MovieGameInitializerServlet.initiatorIdToGameObjMap.keySet().contains(intiatorId)) {
     * MovieGameInitializerServlet.initiatorIdToGameObjMap.get(intiatorId).setGameState(gameState);
     * out.write(gameState);
     * 
     * } else if (MovieGameInitializerServlet.playerIdToGameObjMap.keySet().contains(intiatorId)) {
     * out.println(MovieGameInitializerServlet.playerIdToGameObjMap.get(intiatorId).getGameState());
     * }
     */}
}
