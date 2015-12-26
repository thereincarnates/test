package com.movieztalk.helper;

import static com.google.api.services.datastore.client.DatastoreHelper.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import com.google.api.services.datastore.DatastoreV1.CommitRequest;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.Mutation;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.api.services.datastore.client.DatastoreFactory;
import com.google.api.services.datastore.client.DatastoreHelper;

public class DataStoreHelper {

  private static Datastore datastoreConnection;
  private static Logger log = Logger.getLogger(DataStoreHelper.class.getName());

  public static Datastore getDatastoreConnection() {
	  System.out.println("get data store connection entry" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
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
      System.out.println("get data store connection exit" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));

      return datastoreConnection;
    }
    System.out.println("get data store connection exit" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));

    return datastoreConnection;
  }

  public static boolean storeData(Map<String, String> propertyMap, String kind) {
	  log.info("Entering into storedata" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    Entity entity;
    Entity.Builder entityBuilder = Entity.newBuilder().setKey(makeKey(kind));
    for (String key : propertyMap.keySet()) {
      entityBuilder.addProperty(makeProperty(key, makeValue(propertyMap.get(key))));
      log.info("adding key:" + key +"\t" + propertyMap.get(key));
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
    System.out.println("exiting storedata" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    return false;
  }

  public static boolean UpdateEntity(Entity.Builder prop)
  {
	  CommitRequest request = CommitRequest.newBuilder()
			    .setMode(CommitRequest.Mode.NON_TRANSACTIONAL)
			    .setMutation(Mutation.newBuilder().addUpdate(prop))
			    .build();
	try {
		getDatastoreConnection().commit(request);
	} catch (DatastoreException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
  }
}
