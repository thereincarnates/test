package com.movieztalk.server;

import static com.google.api.services.datastore.client.DatastoreHelper.makeKey;
import static com.google.api.services.datastore.client.DatastoreHelper.makeProperty;
import static com.google.api.services.datastore.client.DatastoreHelper.makeValue;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.services.datastore.DatastoreV1.BeginTransactionRequest;
import com.google.api.services.datastore.DatastoreV1.BeginTransactionResponse;
import com.google.api.services.datastore.DatastoreV1.CommitRequest;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.Mutation;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.api.services.datastore.client.DatastoreFactory;
import com.google.api.services.datastore.client.DatastoreHelper;
import com.google.protobuf.ByteString;

public class MoviePopulateServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
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
      List<String> movieList = Arrays.asList("3 Idiots", "Rang De Basanti",
          "Mughal-E-Azam", "Lagaan: Once Upon a Time in India", "Swades", "Pyaasa", "A Wednesday",
          "Anand", "Drishyam", "Gangs of Wasseypur", "Udaan", "Bhaag Milkha Bhaag",
          "Special 26", "Dil Chahta Hai", "Black Friday", "Queen", "Haider", "Paan Singh Tomar",
          "Guru", "Andaz Apna Apna", "Dev D", "Chakde India", "Sarfarosh", "Barfi",
          "Zindagi Na Milegi Dobara", "PK", "Baby", "My Name Is Khan",
          "The Legend of Bhagat Singh", "Kahaani", "Bajrangi Bhaijaan", "Shahid", "Salaam Bombay",
          "Black", "Gol Maal", "Omkara", "Guide", "Kaagaz Ke Phool", "Jo Jeeta Wohi Sikandar",
          "Vaastav The Reality", "The Chess Players", "Mother India", "Water", "Sholay",
          "Hera Pheri", "Chupke Chupke", "Manjhi The Mountain Man", "Roja",
          "Straight from the Heart", "Deewaar", "Who Pays the Piper", "Company", "Hindustani",
          "Jaane Tu Ya Jaane Na", "Rock On", "Lage Raho Munna Bhai", "Johnny Gaddaar",
          "Padosan", "Do Bigha Zamin", "Jab We Met", "Mumbai Meri Jaan", "Oye Lucky Lucky Oye",
          "Kal Ho Naa Ho", "Chhoti Si Baat", "Awaara", "Dilwale Dulhania Le Jayenge", "Ugly",
          "Bombay", "Gangaajal", "Vicky Donor", "Don", "Parinda", "Ghulam",
          "Qayamat Se Qayamat Tak", "Dil Se", "Jodhaa Akbar", "Mera Naam Joker", "Lootera",
          "Ankur", "I Am Kalam", "Devdas", "Satya", "Saaransh", "Maine Pyar Kiya", "Zanjeer",
          "Hum Aapke Hain Koun", "Masoom", "Don", "Maqbool", "Agneepath", "Shree 420",
          "Damini  Lightning", "Kai po che", "Rockstar", "Delhi Belly", "OMG Oh My God");


    List<String> kollywoodmovieList = Arrays.asList("Kaththi","Velaiilla Pattadharim","Veeram","Aranmanai","Maan Karate","Manjapai","Yaamirukka Bayamey","Madras","Jigarthanda","Pisasu","Viswaroopam","Arrambam","Varuthapadatha Valibar Sangam","Raja Rani","Soodhu Kavvum","Theeya Vela Seiyyanum Kumaru","Pandiyanadu","Ethir Neechal","Kanna Laddu Thinna Aasaiya","Maattrraan","Sundarapandian","Kalakalappu","Vettai","Leelai","Velayudham","Mankatha","Kaavalan","Vandhaan Vendraan","Thambi Vettothi Sundaram","Oru Kal Oru Kannadi","Aayirathil Oruvan","Sura","Thillalangadi","Sindhu Samaveli","Vaadaa","Aattanayagann","Maasilamani","Ayan","Aadhavan","Padikkadavan","Siva Manasula Sakthi","Malai Malai","Jeyam Kondan","Bommalattam","Yaradi Nee Mohini","Santhosh Subramaniam","Vaaranam Aayiram","Saroja","Dasavatharam"," Subramaniapuram","Anjathey","Pokkiri","Vel","Kireedam","Aalwar","Mirugam","Oram Po","Pattiyal","Thimiru","Pudhupettai","Kovai Brothers","Idhaya Thirudan");


      CommitRequest.Builder commBuilder = CommitRequest.newBuilder()
          .setMode(CommitRequest.Mode.NON_TRANSACTIONAL).setMutation(Mutation.newBuilder());

      for (String moString : kollywoodmovieList) {
        Entity.Builder employee = Entity.newBuilder().setKey(makeKey("movie"))
            .addProperty(makeProperty("name", makeValue(moString)))
            .addProperty(makeProperty("industrytype", makeValue("kollywood")));
        Entity entity = employee.build();
        commBuilder.getMutationBuilder().addInsertAutoId(entity);
      }

      /*
       * Set<Entity> entities = new HashSet<>(); for (String movie : movieList) { Entity entity;
       * Entity.Builder employee = Entity.newBuilder().setKey(makeKey("movie"))
       * .addProperty(makeProperty("name", makeValue(movie))); entity = employee.build();
       * entities.add(entity); }
       */

      // Insert the entity in the commit request mutation.
      // creq.getMutationBuilder().addAllInsertAutoId(entities);
      // Execute the Commit RPC synchronously and ignore the response.
      // Apply the insert mutation if the entity was not found and close
      // the transaction.
      datastore.commit(commBuilder.build());

    } catch (DatastoreException exception) {
      // Catch all Datastore rpc errors.

    }
  }
}
