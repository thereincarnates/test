package com.movieztalk.helper;

import static com.google.api.services.datastore.client.DatastoreHelper.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import com.google.api.services.datastore.DatastoreV1.CommitRequest;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.Mutation;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.api.services.datastore.client.DatastoreFactory;
import com.google.api.services.datastore.client.DatastoreHelper;

public class DataStoreHelper {

  private static Datastore datastoreConnection;

  public static Datastore getDatastoreConnection() {
    if (datastoreConnection == null) {
      try {
        // Setup the connection to Google Cloud Datastore and infer credentials
        // from the environment.
        datastoreConnection = DatastoreFactory.get().create(
            DatastoreHelper.getOptionsFromEnv().dataset("purchhaseme").build());
      } catch (GeneralSecurityException exception) {
        System.err.println("Security error connecting to the datastore: " + exception.getMessage());
        System.exit(1);
      } catch (IOException exception) {
        System.err.println("I/O error connecting to the datastore: " + exception.getMessage());
        System.exit(1);
      }
      return datastoreConnection;
    }
    return datastoreConnection;
  }

  public static boolean storeData(Map<String, String> propertyMap, String kind) {
    Entity entity;
    Entity.Builder entityBuilder = Entity.newBuilder().setKey(makeKey(kind));
    for (String key : propertyMap.keySet()) {
      entityBuilder.addProperty(makeProperty(key, makeValue(propertyMap.get(key))));
    }
    entity = entityBuilder.build();
    CommitRequest request = CommitRequest.newBuilder()
        .setMode(CommitRequest.Mode.NON_TRANSACTIONAL)
        .setMutation(Mutation.newBuilder().addInsertAutoId(entity)).build();
    try {
      getDatastoreConnection().commit(request);
    } catch (DatastoreException e) {
      e.printStackTrace();
    }
    return false;
  }
}
