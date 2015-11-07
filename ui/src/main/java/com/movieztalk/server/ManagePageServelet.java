package com.movieztalk.server;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.google.gson.Gson;
import com.movieztalk.extraction.model.Movie;

public class ManagePageServelet extends HttpServlet 
{
	
  private static final Logger logger = Logger.getLogger(ManagePageServelet.class.getName());
  private static final long serialVersionUID = 1L;
  
 
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {  
    response.setContentType("text/html");
    logger.info("Testing the data");
    String optionValue = request.getParameter("optionid");
    logger.info("Testing the data" + optionValue);
    
  }
}
