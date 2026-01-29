package it.units.sdm.quoridor.controller.engine;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.engine.abstracts.CLIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.view.PawnAppearance;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.utils.Color;

import java.io.BufferedReader;
import java.io.IOException;

public class StandardCLIQuoridorGameEngine extends CLIQuoridorGameEngine {
  private final BufferedReader reader;

  protected StandardCLIQuoridorGameEngine(AbstractQuoridorBuilder builder, QuoridorParser parser, StatisticsCounter statisticsCounter, BufferedReader reader) {
    super(builder, statisticsCounter, parser);
    this.reader = reader;
  }

  @Override
  public void runGame() throws BuilderException {
    createGame();
    statisticsCounter.setGame(game);
    printInitialInformation();

    while (!game.isGameFinished()) {
      System.out.println(game);
      executeRound();

      if (!game.isGameFinished()) {
        game.changeRound();
      }
    }

    statisticsCounter.updateAllTotalStats();
    printEndGameInformation();
    statisticsCounter.resetGameStats();

    handleEndGame();
  }


  private void printInitialInformation() {
    System.out.println("\n--- Welcome to QUORIDOR! ---\n");
    System.out.println("Before starting, here you can find a couple of useful information:\n\n");
    printWallsConvention();
    System.out.println(parser);
    System.out.println("Press ENTER to start the game...");

    try {
      reader.readLine();
    } catch (IOException e) {
      System.err.println("Error reading input: " + e.getMessage());
    }
  }

  private void printEndGameInformation() {
    System.out.print(game);
    System.out.println(generateSeparator());
    System.out.println(game.getPlayingPawn() + " has won!");
    System.out.println(statisticsCounter.generateStatisticsReport());
  }

  protected void executeRound() throws BuilderException {
    boolean commandExecuted = false;

    do {
      try {
        System.out.print("\nMake your move: ");
        String command = askCommand();
        commandExecuted = performCommand(command, false);
      } catch (IOException e) {
        System.err.println("Error reading input: " + e.getMessage());
        System.out.println("Please try entering your command again:");
      } catch (InvalidActionException | InvalidParameterException | ParserException e) {
        System.err.println(e.getMessage());
      }
    } while (!commandExecuted);
  }

  protected void handleEndGame() {
    try {
      System.out.println("Do you want to Quit (q) or restart a new game (r)?");
      String command = askCommand();
      parser.parse(command);

      switch (parser.getCommandType().orElseThrow()) {
        case QUIT -> handleQuitGame();
        case RESTART -> handleRestartGame();
      }
    } catch (IOException e) {
      System.err.println("Error reading input: " + e.getMessage());
      quitGame();
    } catch (ParserException | BuilderException e) {
      System.err.println("Exception while handling Game End: " + e.getMessage());
      handleQuitGame();
    }
  }

  @Override
  protected void restartGame() throws BuilderException {
    statisticsCounter.resetGameStats();
    runGame();
  }

  @Override
  protected void printHelp() {
    printWallsConvention();
    System.out.println(parser);
  }

  private void printWallsConvention() {
    final PawnAppearance demoPawn = new PawnAppearance(Color.WHITE);

    String figure = "How walls are placed:\n\n" +
            "     +     +     +        \n" +
            "     |                    \n" +
            "  v  +     +     +        \n" +
            "     |" + demoPawn + "\n" +
            "     +-----+-----+        \n" +
            "           h              \n" +
            "                          \n";

    System.out.print(figure);
  }

  @Override
  protected void sendCommand(String command) throws IOException {
  }

  protected String askCommand() throws IOException {
    return String.valueOf(reader.readLine());
  }

  protected String generateSeparator() {
    return "\n" + "-".repeat(27) + "\n";
  }

}
