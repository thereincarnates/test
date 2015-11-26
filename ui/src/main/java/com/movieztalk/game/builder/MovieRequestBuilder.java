package com.movieztalk.game.builder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.EntityResult;
import com.google.api.services.datastore.DatastoreV1.GqlQuery;
import com.google.api.services.datastore.DatastoreV1.Property;
import com.google.api.services.datastore.DatastoreV1.Query;
import com.google.api.services.datastore.DatastoreV1.RunQueryRequest;
import com.google.api.services.datastore.DatastoreV1.RunQueryResponse;
import com.google.api.services.datastore.DatastoreV1.Value;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.gson.FieldAttributes;
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
  
  public MovieRequestBuilder buildMovieName(String id)
  {
	  movieRequest.setMoviename(fetchMovieName(id).toUpperCase().replaceAll(" "," , "));
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
	  return movieRequest;
  }

  private String fetchMovieName(String id)
  {
	  
	  Datastore  datastore =  DataStoreHelper.getDatastoreConnection();
	  String movie = null;
	  
	  Query.Builder q = Query.newBuilder();
      q.addKindBuilder().setName("movie");
      q.setLimit(1);
      
      RunQueryRequest request = RunQueryRequest.newBuilder().setQuery(q).build();
      try {
		    RunQueryResponse response = datastore.runQuery(request);
		    for(EntityResult moviename: response.getBatch().getEntityResultList())
	        {
		    	movie = moviename.getEntity().getProperty(0).getValue().getStringValue();
	    	    System.out.println("received data: " + movie);
	        }
	    }catch (DatastoreException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
    	}
      
     
      return movie;
      
     
  }
}
