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
  
  class DataTable
  {
     String MovieName;
     String MovieId;
     String Date;  
  }
 
  private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {

          response.setContentType("text/html");
      logger.info("Testing the data");

      List<String> optionList = new ArrayList<String>();

      optionList.add("Carousel Movies");
      optionList.add("Latest Movies");
      optionList.add("UpComing Movies");

      Gson gson = new Gson();
      String json = gson.toJson(optionList);
      
      System.out.println("Movie json Object :  " + json);
      System.out.println("Request Paramter:  " + request.getParameter("optionid"));
      System.out.println("Request Paramter:  " + request.getParameter("movieId"));
      
      if((request.getParameter("optionid")) != null)
      {

         List<DataTable> dataTable = new ArrayList<DataTable>();


         DataTable row1 = new DataTable();
         DataTable row2 = new DataTable();
         DataTable row3 = new DataTable();

        if(request.getParameter("optionid").equals("Carousel Movies"))
        {
         row1.MovieName = "MovieC1";
         row1.MovieId = "MovieIdC1";
         row1.Date = "date1";

         row2.MovieName = "MovieC2";
         row2.MovieId = "MovieIdC2";
         row2.Date = "date1";

         row3.MovieName = "MovieC3";
         row3.MovieId = "MovieIdC3";
         row3.Date = "date1";
        }
        else if((request.getParameter("optionid")).equals("Latest Movies"))
        {
         row1.MovieName = "MovieL1";
         row1.MovieId = "MovieIdL2";
         row1.Date = "date2";

         row2.MovieName = "MovieL2";
         row2.MovieId = "MovieIdL2";
         row2.Date = "date2";

         row3.MovieName = "MovieL3";
         row3.MovieId = "MovieIdL3";
         row3.Date = "date2";
        }
         else if((request.getParameter("optionid")).equals("UpComing Movies"))
        {
         row1.MovieName = "MovieU1";
         row1.MovieId = "MovieIdU1";
         row1.Date = "date3";

         row2.MovieName = "Movie3";
         row2.MovieId = "MovieIdU2";
         row2.Date = "date3";

         row3.MovieName = "Movie3";
         row3.MovieId = "MovieIU3";
         row3.Date = "date3";
        }
        else{}

         dataTable.add(row1);
         dataTable.add(row2);
         dataTable.add(row3);
        
         Gson gson1 = new Gson();
         json = gson1.toJson(dataTable);

         System.out.println("Movie json Object :  " + json);

      }

      PrintWriter out = response.getWriter();
      out.write(json);
      
      out.flush();
      out.close();
  }
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {  
       doProcess(request,response);
  }
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {  
       doProcess(request,response);
  }
}
