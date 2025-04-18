package it.units.sdm.quoridor.cli.engine;

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
  private final QuoridorParser parser;

  public StandardCLIQuoridorGameEngine(BufferedReader reader, QuoridorParser parser, AbstractQuoridorBuilder builder) {
    super(builder);
    this.reader = reader;
    this.parser = parser;
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

    System.out.print(game);
    System.out.println(game.getPlayingPawn() + " has won!");

    endGame();
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

  private void executeRound(AbstractGame game) {
    boolean commandExecuted = false;

    do {
      try {
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

  private void endGame() {
    System.exit(0);
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

  private String askCommand() throws IOException {
    System.out.print("\nMake your move: ");
    return String.valueOf(reader.readLine());
  }

  private boolean performCommand(String command, AbstractGame game) throws ParserException, InvalidParameterException, InvalidActionException {
    parser.parse(command);
    Optional<Position> targetPosition = parser.getActionPosition();

    return switch (parser.getCommandType().orElseThrow()) {
      case MOVE -> {
        game.movePlayingPawn(targetPosition.orElse(null));
        yield true;
      }
      case WALL -> {
        game.placeWall(targetPosition.orElse(null), parser.getWallOrientation().orElse(null));
        yield true;
      }
      case QUIT -> {
        endGame();
        yield true;
      }
      case HELP -> {
        printWallsConvention();
        System.out.println(parser);
        yield false;
      }
    };
  }
}
