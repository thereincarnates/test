package com.movieztalk.server;

import static com.google.api.services.datastore.client.DatastoreHelper.makeKey;
import static com.google.api.services.datastore.client.DatastoreHelper.makeProperty;
import static com.google.api.services.datastore.client.DatastoreHelper.makeValue;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.movieztalk.game.model.ScoreBoard;

public class DataStoreDemoServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    ScoreBoard scoreBoard = new ScoreBoard();
    ScoreBoard.PlayerScoreBoard playerScoreBoard = scoreBoard.new PlayerScoreBoard();
    playerScoreBoard.setTotalGamePlayed("10");
    playerScoreBoard.setTotalScore("100");
    playerScoreBoard.getScores().addAll(Arrays.asList(1, 2, 4, 5));
    scoreBoard.setPlayerScoreBoard1(playerScoreBoard);
    scoreBoard.setPlayerScoreBoard2(playerScoreBoard);

    Gson gson = new Gson();
    String str = gson.toJson(scoreBoard);
    out.write(str);
    out.flush();
    out.close();

  }
}
