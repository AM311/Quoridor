package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.PawnAppearance;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.utils.Color;
import it.units.sdm.quoridor.utils.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public class StandardCLIQuoridorGameEngine extends QuoridorGameEngine {

  private final BufferedReader reader;
  protected final QuoridorParser parser;
  private final StatisticsCounter statisticsCounter;

  public StandardCLIQuoridorGameEngine(BufferedReader reader, QuoridorParser parser, AbstractQuoridorBuilder builder, StatisticsCounter statisticsCounter) {
    super(builder);
    this.reader = reader;
    this.parser = parser;
    this.statisticsCounter = statisticsCounter;
  }

  @Override
  public void runGame() throws BuilderException {
    AbstractGame game = createGame();
    printInitialInformation();

    while (!game.isGameFinished()) {
      System.out.println(game);
      executeRound(game);

      if (!game.isGameFinished()) {
        game.changeRound();
      }
    }

    printEndGameInformation(game);
    statisticsCounter.resetGameStats();
    handleEndGame();
  }

  private AbstractGame createGame() throws BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(builder);
    return builderDirector.makeGame();
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

  private void printEndGameInformation(AbstractGame game) {
    statisticsCounter.updateAllTotalStats(game);
    System.out.print(game);
    System.out.println(generateSeparator());
    System.out.println(game.getPlayingPawn() + " has won!");
    System.out.println(statisticsCounter.generateStatisticsReport(game));
  }

  protected void executeRound(AbstractGame game) {
    boolean commandExecuted = false;

    do {
      try {
        System.out.print("\nMake your move: ");
        String command = askCommand();
        commandExecuted = performCommand(command, game);
      } catch (IOException e) {
        System.err.println("Error reading input: " + e.getMessage());
        System.out.println("Please try entering your command again:");
      } catch (InvalidActionException | InvalidParameterException | ParserException e) {
        System.err.println(e.getMessage());
      }
    } while (!commandExecuted);
  }

  private void handleEndGame() {
    try {
      System.out.println(generateSeparator());
      System.out.println("Quit (q) or restart (r)?");
      String command = askCommand();
      parser.parse(command);

      switch (parser.getCommandType().orElseThrow()) {
        case QUIT -> System.exit(0);
        case RESTART -> runGame();
      }
    } catch (IOException e) {
      System.err.println("Error reading input: " + e.getMessage());
      System.out.println("Please try entering your command again:");
      handleEndGame();
    } catch (ParserException | BuilderException e) {
      System.err.println(e.getMessage());
      handleEndGame();
    }
  }

  private void printWallsConvention() {
    final String SQUARE = " " + Color.WHITE.getAnsiEscapeCode() + "   " + PawnAppearance.RESET_STRING + " ";

    String figure = "How walls are placed:\n\n" +
            "     +     +     +        \n" +
            "     |                    \n" +
            "  v  +     +     +        \n" +
            "     |" + SQUARE + "\n" +
            "     +-----+-----+        \n" +
            "           h              \n" +
            "                          \n";

    System.out.print(figure);
  }

  protected String askCommand() throws IOException {
    return String.valueOf(reader.readLine());
  }

  protected boolean performCommand(String command, AbstractGame game) throws ParserException, InvalidParameterException, InvalidActionException {
    parser.parse(command);
    Optional<Position> targetPosition = parser.getActionPosition();

    return switch (parser.getCommandType().orElseThrow()) {
      case MOVE -> {
        game.movePlayingPawn(targetPosition.orElse(null));

        statisticsCounter.updateGameMoves(String.valueOf(game.getPlayingPawn()));
        yield true;
      }
      case WALL -> {
        game.placeWall(targetPosition.orElse(null), parser.getWallOrientation().orElse(null));
        statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
        yield true;
      }
      case QUIT -> {
        handleEndGame();
        yield true;
      }
      case HELP -> {
        printWallsConvention();
        System.out.println(parser);
        yield false;
      }
      case RESTART -> {
        System.out.println("Restart command is only available after quitting the current game.");
        yield false;
      }
    };
  }

  private String generateSeparator() {
    return "\n" + "-".repeat(27) + "\n";
  }

}
