package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

public class ServerStandardCLIQuoridorGameEngine extends StandardCLIQuoridorGameEngine {
  private final BufferedReader socketReader;
  private final BufferedWriter socketWriter;

  public ServerStandardCLIQuoridorGameEngine(BufferedReader reader, QuoridorParser parser, AbstractQuoridorBuilder builder, StatisticsCounter statisticsCounter, BufferedWriter socketWriter, BufferedReader socketReader) {
    super(reader, parser, builder, statisticsCounter);
    this.socketWriter = socketWriter;
    this.socketReader = socketReader;
  }

  @Override
  protected void executeRound(AbstractGame game) {
    boolean commandExecuted = false;
    String serverMessage = null;
    try {
      System.out.println("Waiting for another player's round...");
      serverMessage = socketReader.readLine();
    } catch (IOException e) {
      System.err.println("Unable to communicate with server!" + e.getMessage());
      handleQuitGame();
    }

    do {
      try {
        if (Objects.equals(serverMessage, "PLAY")) {
          System.out.println("It's your round!\n");
          String command = askCommand();
          commandExecuted = performCommand(command, game);
        } else {
          commandExecuted = performCommand(serverMessage, game);
        }
      } catch (IOException e) {
        System.err.println("Unable to communicate with server!");
        handleQuitGame();
      } catch (InvalidActionException | InvalidParameterException | ParserException | BuilderException e) {
        System.err.println(e.getMessage());
      }
    } while (!commandExecuted);
  }

  @Override
  protected void handleEndGame() {
    try {
      switch (parser.getCommandType().orElseThrow()) {
        case QUIT -> handleQuitGame();
        case RESTART -> handleRestartGame();
      }
    } catch (BuilderException e) {
      System.err.println(e.getMessage());
      handleEndGame();
    }
  }

  @Override
  protected void handleQuitGame() {
    System.out.println("A Player disconnected.");
    try {
      socketWriter.close();
    } catch (IOException e) {
      System.err.println("Error closing socket: " + e.getMessage());
    }

    System.exit(0);
  }

  @Override
  protected void forwardCommand(String command) throws IOException {
    socketWriter.write(command + System.lineSeparator());
    socketWriter.flush();
    socketReader.readLine();
  }
}
