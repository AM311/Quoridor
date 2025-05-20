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
import java.util.Objects;

public class ServerStandardCLIQuoridorGameEngine extends StandardCLIQuoridorGameEngine {
  private final BufferedReader socketReader;
  private final BufferedWriter socketWriter;

  public ServerStandardCLIQuoridorGameEngine(BufferedReader reader, QuoridorParser parser, AbstractQuoridorBuilder builder, StatisticsCounter statisticsCounter, BufferedWriter socketWriter, BufferedReader socketReader) {
    super(reader, parser, builder, statisticsCounter);
    this.socketWriter = socketWriter;
    this.socketReader = socketReader;
  }

  //todo CORRISPONDENZA TRA COLORE E NUMERO DA GESTIRE
  //todo AGGIUNGERE TESTO "ATTENDI MOSSE DEGLI ALTRI GIOCATORI"

  @Override
  protected void executeRound(AbstractGame game) {
    boolean commandExecuted = false;
    String serverMessage = null;
    try {
      serverMessage = socketReader.readLine();
    } catch (IOException e) {
      Logger.printLog(System.err, "Unable to communicate with server!");
      System.exit(0);
    }

    do {
      try {
        if (Objects.equals(serverMessage, "PLAY")) {
          System.out.println("It's your round!\n");
          String command = askCommand();
          commandExecuted = performCommand(command, game);
          if (commandExecuted) {
            socketWriter.write(command + System.lineSeparator());
            socketWriter.flush();
            socketReader.readLine();
          }
        } else {
          if (Objects.equals(serverMessage, "QUIT")) {
            System.out.println("Another Player disconnected.");
            socketWriter.close();
            System.exit(0);
          } else {
            commandExecuted = performCommand(serverMessage, game);
          }
        }
      } catch (IOException e) {
        Logger.printLog(System.err, "Unable to communicate with server!");
        System.exit(0);
      } catch (InvalidActionException | InvalidParameterException | ParserException e) {
        System.err.println(e.getMessage());
      }
    } while (!commandExecuted);
  }
}
