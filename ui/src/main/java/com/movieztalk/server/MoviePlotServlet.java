package com.movieztalk.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.movieztalk.extraction.model.Movie;
import com.movieztalk.movieaspects.model.MovieAspect;

public class MoviePlotServlet extends HttpServlet {
  private static final Logger logger = Logger.getLogger(MoviePlotServlet.class.getName());
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    logger.info("Testing the data");
    Gson gson = new Gson();
    String json = gson.toJson(buildMovieObject());
    PrintWriter out = response.getWriter();
    out.write(json);
    out.flush();
    out.close();
  }

  private Movie buildMovieObject() {

    List<String> songsAndTrailers = Arrays.asList("https://www.youtube.com/embed/k99-vMPh3-A",
        "https://www.youtube.com/embed/gIvVcW2_CIs", "https://www.youtube.com/embed/lYNNgGBVtTo",
        "https://www.youtube.com/embed/zRtPUIumXcY");

    MovieAspect ms = new MovieAspect();
    Set<String> reviews = Sets.newHashSet("This is test1", "This is test2", "This is test3");
    ms.getNegativeReviews().addAll(reviews);
    ms.getPositiveReviews().addAll(reviews);

    Movie movie = new Movie();
    movie
        .setActing(null)
        .setActors(null)
        .setBoxOffice("1 crore")
        .setBudget("50 Lakhs")
        .setPlot(
            "Tanu Weds Manu Returns is a 2015 Indian romantic comedy drama film directed by Anand L. Rai which serves as a sequel to the 2011 film Tanu Weds Manu. R. Madhavan, Kangana Ranaut, Jimmy Shergill, Deepak Dobriyal, Swara Bhaskar and Eijaz Khan reprise their roles from the original film. Ranaut also portrays the additional role of a Haryanvi athlete in it. The story, screenplay and the dialogues were written by Himanshu Sharma. The soundtrack and film score were composed by Krsna Solo and the lyrics were penned by Rajshekhar. Saroj Khan and Bosco–Caesar were the film′s choreographers while the editing was done by Hemal Kothari. The principle photography began on October 2014 and the film was released on 22 May 2015. The film carries the story forward showing the next chapter of the couple's life. Tanu Weds Manu Returns received positive reviews from critics and Ranaut's performance was particularly praised.[3][4] Made on a budget of ₹30 crore (US$4.5 million), the film earned ₹252 crore (US$38 million)"
                + " worldwide and became one of the highest grossing Bollywood film.")
        .setOverall(ms).setSongAndTrailers(songsAndTrailers);

    return movie;
  }
}
