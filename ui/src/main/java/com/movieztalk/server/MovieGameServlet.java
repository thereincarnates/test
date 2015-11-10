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
import java.util.Arrays;

public class MovieGameServlet extends HttpServlet 
{

  private static final Logger logger = Logger.getLogger(MovieGameServlet.class.getName());
  private static final long serialVersionUID = 1L;
  
 
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {  
    response.setContentType("text/html");
    logger.info("Testing the data");
    List<List<Character>> charinMovie = new ArrayList<List<Character>>();
    
    String movieName = "PYAAR KIYA TO DARNA KYA";
    List<String> movieWords = Arrays.asList(movieName.split(" +"));
    List<Character> listC = new ArrayList<Character>();

    int count = 0;
    for(String word : movieWords)
    {
      count++;
      char[] chars = word.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        System.out.println("characters:" + chars[i]);
      }

       
       for (char c : chars) {
        listC.add(c);
       }
        
        System.out.println("Number of words: " + movieWords.size() +" Count: "+ count);
       if(count != movieWords.size())
       {
        listC.add(' ');
        listC.add(',');
        listC.add(' ');
       }
      

       charinMovie.add(listC);
    }
    System.out.println("Printing the list "+ listC);

    Gson gson = new Gson();
    String json = gson.toJson(listC);


    PrintWriter out = response.getWriter();
    out.write(json);

    out.flush();
    out.close();

    
  }
}
