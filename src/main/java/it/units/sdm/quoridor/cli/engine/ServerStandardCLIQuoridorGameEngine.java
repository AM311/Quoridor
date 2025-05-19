package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.server.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

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
      serverMessage = socketReader.readLine();
    } catch (IOException e) {
      Logger.printLog(System.err, "Communication error");
    }

    do {
      try {
        if (serverMessage.equals("Play")) {
          System.out.println("It's your turn!\n");
          String command = askCommand();
          commandExecuted = performCommand(command, game);
          if (commandExecuted) {
            socketWriter.write(command + System.lineSeparator());
            socketWriter.flush();
            socketReader.readLine();
          }
        } else {
          if (serverMessage.equals("Quit")) {
            System.out.println("Another Player disconnected.");
            socketWriter.close();
            System.exit(0);
          } else {
            commandExecuted = performCommand(serverMessage, game);
          }
        }
      } catch (IOException e) {
        System.err.println("Error reading input: " + e.getMessage());
        System.out.println("Please try entering your command again:");
      } catch (InvalidActionException | InvalidParameterException | ParserException e) {
        System.err.println(e.getMessage());
      }
    } while (!commandExecuted);
  }
}
