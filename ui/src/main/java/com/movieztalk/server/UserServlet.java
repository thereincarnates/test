package com.movieztalk.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserServlet extends HttpServlet 
{
	
  private static final Logger logger = Logger.getLogger(UserServlet.class.getName());
  private static final long serialVersionUID = 1L;
 
  public UserServlet()
  {
    super();
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  { 
	 
    response.setContentType("text/html");
    logger.info("Testing the data");
    System.out.println("MyLogs: ");
    PrintWriter out = response.getWriter();
    out.write("A new user has been created.");
    System.out.println(request.toString());
    out.flush();
    out.close();
  }
  
 
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {
    StringBuffer sb = new StringBuffer();
    logger.info("Testing the data1");
    try 
    {
      BufferedReader reader = request.getReader();
      String line = null;
      while ((line = reader.readLine()) != null)
      {
        sb.append(line);
        logger.info("Testing the data2" + line);
      }
    } catch (Exception e) { e.printStackTrace(); }
 
    JSONParser parser = new JSONParser();
    JSONObject joUser = null;
    try
    {
      joUser = (JSONObject) parser.parse(sb.toString());
    } catch (ParseException e) { e.printStackTrace(); }
 
    String user = (String) joUser.get("name");
    System.out.println("User: " + user);
    
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.write("A new user " + user + " has been created.");
    out.flush();
    out.close();
  }
}
