package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/latestTrailerServlet")
public class LatestTrailerServlet extends HttpServlet {
  private static final Logger logger = Logger.getLogger(MoviePlotServlet.class.getName());
  private static final long serialVersionUID = 1L;

 
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    logger.info("Testing the data");
    Gson gson = new Gson();
    String json = gson.toJson(getLatestTrailerList());
    PrintWriter out = response.getWriter();
    out.write(json);

    out.flush();
    out.close();
  }

  private List<LatestTrailer> getLatestTrailerList() {
 
	  List<String> latestTrailers = Arrays.asList("https://www.youtube.com/embed/k99-vMPh3-A",
		        "https://www.youtube.com/embed/gIvVcW2_CIs", "https://www.youtube.com/embed/lYNNgGBVtTo"/*,
		        "https://www.youtube.com/embed/zRtPUIumXcY"*/);

	  List<LatestTrailer> latestTrailerList = new ArrayList<LatestTrailer>();
	
	  LatestTrailer latestTrailer1 = new LatestTrailer("hindi", latestTrailers);
	  LatestTrailer latestTrailer2 = new LatestTrailer("english", latestTrailers);
	  LatestTrailer latestTrailer3 = new LatestTrailer("telgu", latestTrailers);
	  LatestTrailer latestTrailer4 = new LatestTrailer("tamil", latestTrailers);
	  LatestTrailer latestTrailer5 = new LatestTrailer("kannada", latestTrailers);
	  
	  latestTrailerList.add(latestTrailer1);
	  latestTrailerList.add(latestTrailer2);
	  latestTrailerList.add(latestTrailer3);
	  latestTrailerList.add(latestTrailer4);
	  latestTrailerList.add(latestTrailer5);
	
   
    return latestTrailerList;
  }
}


class LatestTrailer
{
	public String language;
	public List<String> tailorList;
	
	public LatestTrailer(String lang , List<String> trailist) {
		language = lang;
	    tailorList = trailist;
	}
		
}
