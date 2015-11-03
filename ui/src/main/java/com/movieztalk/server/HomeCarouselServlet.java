package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.movieztalk.extraction.model.Movie;

public class HomeCarouselServlet extends HttpServlet {

	private static final Logger logger = Logger
			.getLogger(HomeCarouselServlet.class.getName());
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		logger.info("Testing the data");

		List<Movie> movies = getListOfMovies();
		Gson gson = new Gson();
		String json = gson.toJson(movies);

		System.out.println("Movie json Object :  " + json);

		PrintWriter out = response.getWriter();
		out.write(json);

		out.flush();
		out.close();
	}

	private List<Movie> getListOfMovies() {
		List<Movie> movieList = new ArrayList<Movie>();
		Movie movieObj = new Movie();
		movieObj.setName("Tanu Weds Manu ");
		movieObj.setBudget("100 Crore");
		movieObj.setBoxOffice("1000 crore in 2 days");
		movieList.add(movieObj);
		Movie movieObj1 = new Movie();
		movieObj1.setName("Tanu Weds Manu2 ");
		movieObj1.setBudget("100 Crore");
		movieObj1.setBoxOffice("1000 crore in 2 days");
		movieList.add(movieObj1);
		return movieList;
	}
}
