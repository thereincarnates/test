package com.movieztalk.demo;

import static com.google.api.services.datastore.client.DatastoreHelper.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.api.services.datastore.DatastoreV1.BeginTransactionRequest;
import com.google.api.services.datastore.DatastoreV1.BeginTransactionResponse;
import com.google.api.services.datastore.DatastoreV1.CommitRequest;
import com.google.api.services.datastore.DatastoreV1.CommitResponse;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.Key;
import com.google.api.services.datastore.DatastoreV1.Mutation;
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
      // Set the entity key with only one `path_element`: no parent.
      CommitRequest.Builder creq = CommitRequest.newBuilder();
      // Set the transaction to commit.
      creq.setTransaction(tx);
      List<String> movieList = Arrays.asList("Sholay", "Mughal e Azam", "Mother India",
          "Dilwale Dulhania Le Jayenee", "Pyaasa", "Guide", "Deewaar", "Lagaan", "Pakeezah",
          "Amar Akbar Anthony", "Do Bigha Zamin", "Jaane Bhi Do Yaaro", "3 Idiots",
          "Kaagaz Ke Phool", "Bombay", "Mr India", "Satya", "Dil Chahta Hai", "Andaz Apna Apna",
          "Awaara", "Dil To Pagal Hai", "Om Shanti Om", "Shree 420", "Jab We Met", "Parinda",
          "Shaan", "Zindagi Na Milegi Dobara", "Silsila", "Anand", "Prem Rog", "Barfi", "GolMaal",
          "Ankur", "Ek Tha Tiger", "Chak De  India", "Kaala Patthar", "Ghajini", "Jodhaa Akbar",
          "Kabhi Khushi Kabhie Gham", "Dil Se", "The Lunchbox", "Khakee", "Maine Pyar Kiya",
          "Parvarish", "Velu Nayakan", "Kuch Kuch Hota Hai", "Mera Naam Joker", "Queen",
          "Main Hoon Na", "Rockstar", "Sangam", "Tezaab", "Sahib Bibi Aur Ghulam", "Tashan",
          "Satyam Shivam Sundaram", "Aiyyaa", "Mr   Mrs  55", "Garam Hawa", "Hum Hain Rahi Pya Ke",
          "Lootera", "Parineeta", "Sharmeelee", "Bunty Aur Babli", "Jagte Raho", "Chandni",
          "Madhumati", "Gunga Jumna", "Devdas", "Jewel Thief", "Chhoti Si Baat", "Omkara",
          "Zanjeer", "Naseeb", "Teesri Kasam", "Zubeidaa", "Udaan", "Kabhie Kabhie",
          "Bandit Queen", "Gangster", "Black", "Dabangg", "Kahaani", "Dhoom", "Arth",
          "Gangs of Wasseypur  Part One", "Black Friday", "Kati Patang", "Dhobi Ghat",
          "Bhaag Milkha Bhaag", "Hera Pheri", "Ardh Satya", "Lage Raho Munna Bhai",
          "Mujhse Dosti Karoge", "Salaam Bombay", "Swades", "Umrao Jaan", "Veer Zaara",
          "Yeh Jawaani Hai Deewani", "Hum Aapke Hain Koun", "Bobby");
      Entity.Builder employee = Entity.newBuilder().setKey(makeKey("movie"))
          .addProperty(makeProperty("name", makeValue("devdas")));
      Entity entity = employee.build();
      /*
       * Set<Entity> entities = new HashSet<>(); for (String movie : movieList) { Entity entity;
       * Entity.Builder employee = Entity.newBuilder().setKey(makeKey("movie"))
       * .addProperty(makeProperty("name", makeValue(movie))); entity = employee.build();
       * entities.add(entity); }
       */
      creq.getMutationBuilder().addInsertAutoId(entity);
      // Insert the entity in the commit request mutation.
      // creq.getMutationBuilder().addAllInsertAutoId(entities);
      // Execute the Commit RPC synchronously and ignore the response.
      // Apply the insert mutation if the entity was not found and close
      // the transaction.
      datastore.commit(creq.build());

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
