package com.movieztalk.demo;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.services.datastore.DatastoreV1.BeginTransactionRequest;
import com.google.api.services.datastore.DatastoreV1.BeginTransactionResponse;
import com.google.api.services.datastore.DatastoreV1.CommitRequest;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.Key;
import com.google.api.services.datastore.DatastoreV1.LookupRequest;
import com.google.api.services.datastore.DatastoreV1.LookupResponse;
import com.google.api.services.datastore.DatastoreV1.Property;
import com.google.api.services.datastore.DatastoreV1.Value;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.api.services.datastore.client.DatastoreFactory;
import com.google.api.services.datastore.client.DatastoreHelper;
import com.google.protobuf.ByteString;

public class DataStoreDemo {
  public static void main(String args[]) {
    String datasetId = "purchhaseme";
    Datastore datastore = null;
    try {
      // Setup the connection to Google Cloud Datastore and infer credentials
      // from the environment.
      datastore = DatastoreFactory.get().create(
          DatastoreHelper.getOptionsFromEnv().dataset(datasetId).build());
    } catch (GeneralSecurityException exception) {
      System.err.println("Security error connecting to the datastore: " + exception.getMessage());
      System.exit(1);
    } catch (IOException exception) {
      System.err.println("I/O error connecting to the datastore: " + exception.getMessage());
      System.exit(1);
    }

    try {
      // Create an RPC request to begin a new transaction.
      BeginTransactionRequest.Builder treq = BeginTransactionRequest.newBuilder();
      // Execute the RPC synchronously.
      BeginTransactionResponse tres = datastore.beginTransaction(treq.build());
      // Get the transaction handle from the response.
      ByteString tx = tres.getTransaction();

      // Create an RPC request to get entities by key.
      LookupRequest.Builder lreq = LookupRequest.newBuilder();
      // Set the entity key with only one `path_element`: no parent.
      Key.Builder key = Key.newBuilder().addPathElement(
          Key.PathElement.newBuilder().setKind("Trivia").setName("hgtg2"));
      // Add one key to the lookup request.
      lreq.addKey(key);
      // Set the transaction, so we get a consistent snapshot of the
      // entity at the time the transaction started.
      lreq.getReadOptionsBuilder().setTransaction(tx);
      // Execute the RPC and get the response.
      LookupResponse lresp = datastore.lookup(lreq.build());
      // Create an RPC request to commit the transaction.
      CommitRequest.Builder creq = CommitRequest.newBuilder();
      // Set the transaction to commit.
      creq.setTransaction(tx);
      Entity entity;
      if (lresp.getFoundCount() > 0) {
        entity = lresp.getFound(0).getEntity();
        System.out.println("Entity Found");
      } else {
        // If no entity was found, create a new one.
        Entity.Builder entityBuilder = Entity.newBuilder();
        // Set the entity key.
        entityBuilder.setKey(key);
        // Add two entity properties:
        // - a utf-8 string: `question`
        entityBuilder
            .addProperty(Property
                .newBuilder()
                .setName("question")
                .setValue(
                    Value
                        .newBuilder()
                        .setStringValue(
                            "Meaning of Life, what is your name?vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv")
                        .setIndexed(false)));
        // - a 64bit integer: `answer`
        entityBuilder.addProperty(Property.newBuilder().setName("answer")
            .setValue(Value.newBuilder().setIntegerValue(4)));
        // Build the entity.
        entity = entityBuilder.build();
        // Insert the entity in the commit request mutation.
        creq.getMutationBuilder().addInsert(entity);
      }
      // Execute the Commit RPC synchronously and ignore the response.
      // Apply the insert mutation if the entity was not found and close
      // the transaction.
      datastore.commit(creq.build());
      // Get `question` property value.
      String question = entity.getProperty(0).getValue().getStringValue();
      // Get `answer` property value.
      System.out.println("Question is" + question);
      Long answer = entity.getProperty(1).getValue().getIntegerValue();
      System.out.println(question);
      String result = System.console().readLine("> ");
      if (result.equals(answer.toString())) {
        System.out.println("fascinating, extraordinary and, "
            + "when you think hard about it, completely obvious.");
      } else {
        System.out.println("Don't Panic!" + answer.toString());
      }
    } catch (DatastoreException exception) {
      // Catch all Datastore rpc errors.
      System.err.println("Error while doing datastore operation");
      // Log the exception, the name of the method called and the error code.
      System.err.println(String.format("DatastoreException(%s): %s %s", exception.getMessage(),
          exception.getMethodName(), exception.getCode()));
      System.exit(1);
    }

  }

}
