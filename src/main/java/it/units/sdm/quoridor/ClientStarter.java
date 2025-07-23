package it.units.sdm.quoridor;

import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.cli.engine.QuoridorGameEngine;
import it.units.sdm.quoridor.cli.engine.ServerStandardCLIQuoridorGameEngine;
import it.units.sdm.quoridor.cli.parser.StandardQuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;

import java.io.*;
import java.net.Socket;

public class ClientStarter {
  public static void main(String[] args) {
    try {
      String serverAddress = args[0];
      int port = Integer.parseInt(args[1]);

      try (Socket socket = new Socket(serverAddress, port);
           BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

        System.out.println("Connected to the server! Waiting for other players to join...");
        if (reader.readLine().equals("READY")) {
          System.out.println("The game is starting");
          System.out.println(reader.readLine());
        } else {
          System.err.println("Server is not respecting protocol");
        }

        int numOfPlayers = Integer.parseInt(reader.readLine());
        QuoridorGameEngine engine = new ServerStandardCLIQuoridorGameEngine(new BufferedReader(new InputStreamReader(System.in)), new StandardQuoridorParser(), new StdQuoridorBuilder(numOfPlayers), new StatisticsCounter(), writer, reader);
        engine.runGame();
      } catch (IOException e) {
        System.err.println("Could not connect to server: " + e.getMessage());
      } catch (InvalidParameterException e) {
        System.err.println("Invalid number of players!");
      } catch (BuilderException e) {
        System.err.println("Exception encountered while creating the Game: " + e.getMessage());
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.err.println("Missing parameter: " + ex.getMessage());
    }
  }
}
