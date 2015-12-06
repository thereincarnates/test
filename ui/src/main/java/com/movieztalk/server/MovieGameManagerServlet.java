package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.movieztalk.game.builder.MovieRequestBuilder;
import com.movieztalk.game.model.MovieRequest;
import com.movieztalk.game.model.ScoreBoard;

@SuppressWarnings("serial")
public class MovieGameManagerServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    String intiatorId = request.getParameter("initiatorid");
    String gameState = request.getParameter("gamestate");
    String preint = request.getParameter("preinit");
    String score = request.getParameter("score");
    String industryType = request.getParameter("industry").toLowerCase();
    if(industryType.split(" ").length>1)
    {
      industryType = "bollywood";
    }

    System.out.println("Score received: " + score);
    String id = request.getParameter("id");
    System.out.println("String id receive" + id);
    String gamePostAction = request.getParameter("gamepostaction");
    System.out.println("String gamepostaction" + gamePostAction);
    if (((preint != null) && (preint.equalsIgnoreCase("computer")))
        || ((gamePostAction != null) && (gamePostAction.equalsIgnoreCase("continue")))) {
      MovieRequest movieRequest = new MovieRequestBuilder().buildMovieRequest(id, score,industryType).build();

      Gson gson = new Gson();
      PrintWriter out = response.getWriter();
      out.write(gson.toJson(movieRequest));
      out.flush();
      out.close();
    }

    if (gamePostAction.equalsIgnoreCase("finish")) {
      PrintWriter out = response.getWriter();
      out.write("");
      out.flush();
      out.close();
    }
  }
}
