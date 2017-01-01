package com.movieztalk.server.game;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.datastore.client.DatastoreException;
import com.google.gson.Gson;
import com.movieztalk.game.builder.MovieRequestBuilder;
import com.movieztalk.game.model.MovieRequest;
import com.movieztalk.game.model.ScoreBoard;

@SuppressWarnings("serial")
@WebServlet("/gamemanager")
public class MovieGameManagerServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(MovieGameManagerServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String preint = request.getParameter("preinit");
		String score = request.getParameter("score");
		String industryType = request.getParameter("industry").toLowerCase();
		if (industryType.split(" ").length > 1) {
			industryType = "bollywood";
		}

		System.out.println("Score received: " + score);
		String id = request.getParameter("id");
		System.out.println("String id receive" + id);
		String gamePostAction = request.getParameter("gamepostaction");
		System.out.println("String gamepostaction" + gamePostAction);

		if (((preint != null) && (preint.equalsIgnoreCase("computer")))
				|| ((gamePostAction != null) && (gamePostAction.equalsIgnoreCase("continue")))) {
			try {
				logger.info("input to movie request builder is " + "id\t" + id + "\t" + score + "\t" + industryType);
				MovieRequest movieRequest = new MovieRequestBuilder(id, score, industryType).buildMovieName()
						.buildScore().build();
				Gson gson = new Gson();
				PrintWriter out = response.getWriter();
				out.write(gson.toJson(movieRequest));
				out.flush();
				out.close();
			} catch (DatastoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (gamePostAction.equalsIgnoreCase("finish")) {
			PrintWriter out = response.getWriter();
			out.write("");
			out.flush();
			out.close();
		}
		logger.info("done giving movie");
	}
}
