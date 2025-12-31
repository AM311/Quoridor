package it.units.sdm.quoridor.controller.engine;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.controller.server.ServerProtocolCommands;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

public class ServerStandardCLIQuoridorGameEngine extends StandardCLIQuoridorGameEngine {
  private final BufferedReader socketReader;
  private final BufferedWriter socketWriter;

  protected ServerStandardCLIQuoridorGameEngine(AbstractQuoridorBuilder builder, QuoridorParser parser, StatisticsCounter statisticsCounter, BufferedReader reader, BufferedReader socketReader, BufferedWriter socketWriter) {
    super(builder, parser, statisticsCounter, reader);
    this.socketWriter = socketWriter;
    this.socketReader = socketReader;
  }

  @Override
  protected void executeRound() {
    boolean commandExecuted = false;
    String serverMessage = null;

    try {
      System.out.println("Waiting for another player's round...");
      serverMessage = socketReader.readLine();
    } catch (IOException e) {
      System.err.println("Unable to communicate with server!" + e.getMessage());
      quitGame();
    }

    do {
      try {
        if (Objects.equals(serverMessage, ServerProtocolCommands.PLAY.getCommandString())) {
          System.out.println("It's your round!" + System.lineSeparator());
          String command = askCommand();

          commandExecuted = performCommand(command, true);
        } else {
          commandExecuted = performCommand(serverMessage, false);
        }
      } catch (IOException e) {
        System.err.println("Unable to communicate with server!");
        quitGame();
      } catch (InvalidActionException | InvalidParameterException | ParserException | BuilderException e) {
        System.err.println(e.getMessage());
      }
    } while (!commandExecuted);
  }

  @Override
  protected void handleEndGame() {
    String serverMessage;

    try {
      serverMessage = socketReader.readLine();

      if (Objects.equals(serverMessage, ServerProtocolCommands.PLAY.getCommandString())) {
        super.handleEndGame();
      } else {
        System.out.println("The current player is choosing whether to start a new game. Please wait...");

        parser.parse(serverMessage);

        switch (parser.getCommandType().orElseThrow()) {
          case QUIT -> quitGame();
          case RESTART -> restartGame();
        }
      }
    } catch (IOException e) {
      System.err.println("Unable to communicate with server: " + e.getMessage());
      quitGame();
    } catch (ParserException | BuilderException e) {
      System.err.println("Exception while handling Game End: " + e.getMessage());
      quitGame();
    }
  }

  @Override
  protected void sendCommand(String command) throws IOException {
    socketWriter.write(command + System.lineSeparator());
    socketWriter.flush();
  }

  @Override
  protected void quitGame() {
    System.out.println("A Player disconnected.");
    try {
      socketWriter.close();
    } catch (IOException e) {
      System.err.println("Error closing socket: " + e.getMessage());
    }

    super.quitGame();
  }
}
