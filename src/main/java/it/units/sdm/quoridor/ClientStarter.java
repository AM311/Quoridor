package it.units.sdm.quoridor;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.engine.EngineContext;
import it.units.sdm.quoridor.controller.engine.ServerEngineFactory;
import it.units.sdm.quoridor.controller.engine.abstracts.AbstractEngineFactory;
import it.units.sdm.quoridor.controller.engine.abstracts.QuoridorGameEngine;
import it.units.sdm.quoridor.controller.parser.StandardQuoridorParser;
import it.units.sdm.quoridor.controller.server.ServerProtocolCommands;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.FactoryException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;

import java.io.*;
import java.net.Socket;

public class ClientStarter {
  private static final AbstractEngineFactory factory = new ServerEngineFactory();
  private static final EngineContext.ContextBuilder contextBuilder = new EngineContext.ContextBuilder();

  public static void main(String[] args) {
    try {
      String serverAddress = args[0];
      int port = Integer.parseInt(args[1]);

      try (Socket socket = new Socket(serverAddress, port);
           BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

        System.out.println("Connected to the server! Waiting for other players to join...");
        if (reader.readLine().equals(ServerProtocolCommands.READY.getCommandString())) {
          System.out.println("The game is starting");
          System.out.println(reader.readLine());
        } else {
          System.err.println("Server is not respecting protocol");
        }

        int numOfPlayers = Integer.parseInt(reader.readLine());

        contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(numOfPlayers));

        QuoridorGameEngine engine = switch (args[2]) {
          case "CLI" ->
                  factory.createCLIEngine(contextBuilder.setReader(new BufferedReader(new InputStreamReader(System.in))).setSocket(reader, writer).build());
          case "GUI" -> factory.createGUIEngine(contextBuilder.setSocket(reader, writer).build());
          default -> throw new InvalidParameterException("Invalid game mode.");
        };

        engine.runGame();
      } catch (IOException e) {
        System.err.println("Could not connect to server: " + e.getMessage());
      } catch (BuilderException e) {
        System.err.println("Exception encountered while creating the Game: " + e.getMessage());
      } catch (FactoryException e) {
        System.err.println("Exception encountered while creating the Engine: " + e.getMessage());
      } catch (NumberFormatException e) {
        throw new InvalidParameterException("Invalid numeric argument: " + e.getMessage());
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.err.println("Missing parameter: " + ex.getMessage());
    } catch (InvalidParameterException e) {
      System.err.println("Invalid parameter: " + e.getMessage());
    }
  }
}
