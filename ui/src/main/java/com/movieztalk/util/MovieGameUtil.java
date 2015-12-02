package com.movieztalk.util;

import static com.google.api.services.datastore.client.DatastoreHelper.makeFilter;
import static com.google.api.services.datastore.client.DatastoreHelper.makeValue;

import java.util.Set;
import java.util.logging.Logger;

import com.google.api.services.datastore.DatastoreV1.EntityResult;
import com.google.api.services.datastore.DatastoreV1.Filter;
import com.google.api.services.datastore.DatastoreV1.Property;
import com.google.api.services.datastore.DatastoreV1.PropertyFilter;
import com.google.api.services.datastore.DatastoreV1.Query;
import com.google.api.services.datastore.DatastoreV1.RunQueryRequest;
import com.google.api.services.datastore.DatastoreV1.RunQueryResponse;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.common.collect.HashMultimap;
import com.movieztalk.helper.DataStoreHelper;

public class MovieGameUtil {

  private static HashMultimap<String, String> moviesList = HashMultimap.create();
  static Logger log = Logger.getLogger(MovieGameUtil.class.getName());
  public static Set<String> fetchMovieNames(String industryType) {
    log.info("calling fetchMovieNames in util");
    Datastore datastore = DataStoreHelper.getDatastoreConnection();

    if (!moviesList.containsKey(industryType)) {
      Query.Builder q = Query.newBuilder();
      q.addKindBuilder().setName("movie");
      Filter industryTypeFilter = makeFilter("industrytype", PropertyFilter.Operator.EQUAL,
          makeValue(industryType)).build();
      q.setFilter(industryTypeFilter);
      RunQueryRequest request = RunQueryRequest.newBuilder().setQuery(q).build();

      try {
        RunQueryResponse response = datastore.runQuery(request);
        for (EntityResult moviename : response.getBatch().getEntityResultList()) {
          for(Property prop : moviename.getEntity().getPropertyList()){
            if(prop.getName().equalsIgnoreCase("name")){
              moviesList.put(industryType, prop.getValue()
                  .getStringValue());
              log.info("movie name being stored is" + prop.getValue().getStringValue() );
            }
          }

        }
      } catch (DatastoreException e) {
        e.printStackTrace();
      }
    } else {
      log.info("industry type already present");
    }
    return moviesList.get(industryType);
  }
}
