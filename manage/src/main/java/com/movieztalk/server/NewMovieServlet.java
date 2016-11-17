package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.movieztalk.model.NewMovieInputRequest;

public class NewMovieServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		NewMovieInputRequest movieInputRequest = gson.fromJson(test, NewMovieInputRequest.class);
		PrintWriter out = response.getWriter();
		out.write(movieInputRequest.toString());
		out.flush();
		out.close();
	}
}
