package com.movieztalk.game.builder;

import static com.google.api.services.datastore.client.DatastoreHelper.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.services.datastore.DatastoreV1.CompositeFilter;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.EntityResult;
import com.google.api.services.datastore.DatastoreV1.Filter;
import com.google.api.services.datastore.DatastoreV1.GqlQuery;
import com.google.api.services.datastore.DatastoreV1.Property;
import com.google.api.services.datastore.DatastoreV1.PropertyFilter;
import com.google.api.services.datastore.DatastoreV1.Query;
import com.google.api.services.datastore.DatastoreV1.RunQueryRequest;
import com.google.api.services.datastore.DatastoreV1.RunQueryResponse;
import com.google.api.services.datastore.DatastoreV1.Value;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.UnknownFieldSet.Field;
import com.movieztalk.game.model.MovieRequest;
import com.movieztalk.game.model.ScoreBoard;
import com.movieztalk.helper.DataStoreHelper;

public class MovieRequestBuilder {

  private MovieRequest movieRequest;
  
  public MovieRequestBuilder(){
	  movieRequest = new MovieRequest();
  }
  
  public MovieRequestBuilder buildMovieRequest(String id,String score)
  {
	  movieRequest.setMoviename(fetchMovieName(id,score).toUpperCase().replaceAll(" "," , "));
	  return this;
  }
  
  public MovieRequestBuilder buildScore()
  {
      ScoreBoard scorebd = new ScoreBoard();
      movieRequest.setScoreboard(scorebd);
     
	  return this;
  }
  
  public MovieRequest build()
  {
	  Map<String, String> propertyMap = new HashMap<>();
	   
	  
	  
	  return movieRequest;
  }

  private String fetchMovieName(String id,String score)
  {
	  
	  Datastore  datastore =  DataStoreHelper.getDatastoreConnection();
	  String movie = null;
	  
	  Filter equalInitiatorIdFilter = makeFilter(
	  		    "initiatorid", PropertyFilter.Operator.EQUAL, makeValue(String.valueOf(id.toString()))).build();

	  Query.Builder q1 ;
	 
	  
	  Query.Builder q = Query.newBuilder();
      q.addKindBuilder().setName("movie");
      
      
      RunQueryRequest request = RunQueryRequest.newBuilder().setQuery(q).build();
      try {
		    RunQueryResponse response = datastore.runQuery(request);
		    for(EntityResult moviename: response.getBatch().getEntityResultList())
	        {
		    	movie = moviename.getEntity().getProperty(0).getValue().getStringValue();

     		  	Filter movienameexitFilter = makeFilter(
		  	  		    "moviename", PropertyFilter.Operator.EQUAL, makeValue(String.valueOf(movie))).build();
     		  	
     		  	Filter movieFilter = makeFilter(movienameexitFilter,equalInitiatorIdFilter).build();

     		   q1 = Query.newBuilder();
     		   q1.addKindBuilder().setName("gamestate");
     		   q1.setFilter(movieFilter);
     		   q1.setLimit(1);
     		   
       		  RunQueryRequest movierequest = RunQueryRequest.newBuilder().setQuery(q1).build();
     	      
     		  RunQueryResponse movieresponse = datastore.runQuery(movierequest);
  		      if(movieresponse.getBatch().getEntityResultList().isEmpty())
  	          {
     		     break;
	    	   
	          }
  		    System.out.println("received data: " + movie);
	    }
      }catch (DatastoreException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
    	}
     
        q1 = Query.newBuilder();
	   q1.addKindBuilder().setName("gamestate");
	   q1.setFilter(equalInitiatorIdFilter);
	   q1.setLimit(1);
	   
	   RunQueryRequest request1 = RunQueryRequest.newBuilder().setQuery(q1).build();
	     
         try {
		    RunQueryResponse response1 = datastore.runQuery(request1);
		    for(EntityResult gamedata: response1.getBatch().getEntityResultList())
	        {
		    	System.out.println("inside the loop");
		    	Entity data = gamedata.getEntity();
		    	Entity.Builder updateddata = Entity.newBuilder(data);
		    	updateddata.clearProperty();
		    	for (Property prop : data.getPropertyList()) {
		    	  System.out.println("inside the loop2");
		    	  if (prop.getName().equals("moviename")) {
		    		  
		    		 Set<Value> movieList = new HashSet<>();
		    		 movieList.addAll(prop.getValue().getListValueList());
		    		 movieList.add(makeValue(movie).build());
		    		 
		    	    updateddata.addProperty(makeProperty("moviename", makeValue(movieList)));
		    	  }
		    	  else if(prop.getName().equals("scoreboard"))
		    	  {
		    		  int score1 = 0;
		    		  if(!Strings.isNullOrEmpty(score))
		    		     {
		    			   score1 = Integer.parseInt(score);
		    		     }
		    		  
		    		  ScoreBoard sb ;
		    		  Gson gson = new Gson();
		    		  String scoreboard = prop.getValue().getStringValue();
		    		  
		    		  if(!scoreboard.isEmpty())
		    		  {
		    			  sb = gson.fromJson(scoreboard, ScoreBoard.class);
		    			  sb.getPlayerScoreBoard1().setTotalGamePlayed(sb.getPlayerScoreBoard1().getTotalGamePlayed() + 1);
		    			  sb.getPlayerScoreBoard1().setTotalScore(sb.getPlayerScoreBoard1().getTotalScore() + score1);
		    			  sb.getPlayerScoreBoard1().getScores().add(score1);
		    		  }
		    		  else
		    		  {
		    			 sb  = new ScoreBoard();
		    		  }
		    		  
		    		 		  
		    		  
		    		  scoreboard = gson.toJson(sb);
		    		  movieRequest.setScoreboard(sb);
		    		  updateddata.addProperty(makeProperty("scoreboard",makeValue(scoreboard)));
		    	  }
		    	  else {
		    	    updateddata.addProperty(prop);
		    	  }
		    	}
		    	DataStoreHelper.UpdateEntity(updateddata);
	    	    System.out.println("received data2: " + movie);
	        }
	    }catch (DatastoreException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
  	}
    
      
      return movie;
      
     
  }
}
