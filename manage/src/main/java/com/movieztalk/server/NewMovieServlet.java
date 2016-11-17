package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewMovieServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		PrintWriter out = response.getWriter();
		out.write(test);
		out.flush();
		out.close();
	}
}
