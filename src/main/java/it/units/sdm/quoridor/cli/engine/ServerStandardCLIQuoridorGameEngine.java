package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;

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

    do {
      try {
        String serverMessage = socketReader.readLine();
        if (serverMessage.equals("Play")) {
          System.out.print("\nMake your move: ");
          String command = askCommand();
          parser.parse(command);
          socketWriter.write(command);
          socketWriter.flush();
        } else {
          commandExecuted = performCommand(serverMessage, game);
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
