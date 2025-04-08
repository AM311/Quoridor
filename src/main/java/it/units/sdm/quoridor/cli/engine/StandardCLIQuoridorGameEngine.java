package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
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
  public void startGame() throws BuilderException {  // rename to runGame() since it does more than just starting it?
    AbstractGame game = createGame();
    System.out.println(game);

    while (!game.isGameFinished()) {
      game.changeRound();
      executeRound(game);
    }

    System.out.println(game.getPlayingPawn() + " won!");
    endGame();
  }

  private AbstractGame createGame() throws BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(builder);
    return builderDirector.makeGame();
  }

  private void endGame() {
    System.exit(0);
  }

  private void executeRound(AbstractGame game) {
    printWallsConvention();
    System.out.println(parser.toString());
    printGameStatusInfo(game);

    String command = askCommand();
    performCommand(command, game);

    System.out.println(game);
  }

  private void performCommand(String command, AbstractGame game) {
    boolean commandExecuted = false;
    do {
      try {
        parser.acceptAndParse(command);
        Optional<Position> targetPosition = parser.getActionPosition();
        switch (parser.getCommandType().orElseThrow()) {
          case MOVE -> game.movePlayingPawn(targetPosition.orElse(null));
          case WALL -> game.placeWall(targetPosition.orElse(null), parser.getWallOrientation().orElse(null));
          case QUIT -> endGame();
        }
        commandExecuted = true;
        System.out.println("Command " + command + " executed");

      } catch (InvalidActionException | InvalidParameterException | ParserException e) {
        System.out.println(e.getMessage());
        System.out.println("Enter a valid command:");
        command = askCommand();
      }
    } while (!commandExecuted);
  }

  private String askCommand() {
    String command;
    try {
      command = String.valueOf(reader.readLine());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return command;
  }

  private void printWallsConvention() {
    final String RED_SQUARE = "\u001B[31m■\u001B[0m";

    String figure = "How walls are placed:\n\n" +
            "     +     +     +        \n" +
            "     |                    \n" +
            "  v  +     +     +        \n" +
            "     |  " + RED_SQUARE + "\n" +
            "     + ─── + ─── +        \n" +
            "           h              \n" +
            "                          \n";

    System.out.print(figure);
  }

  private void printGameStatusInfo(AbstractGame game){
    System.out.println(game.getPlayingPawn() + "'s round");
    System.out.println("Current Tile: " + game.getPlayingPawn().getCurrentTile().getRow() + ", " + game.getPlayingPawn().getCurrentTile().getColumn());
    System.out.println("Destination row: " + game.getPlayingPawn().getDestinationTiles().iterator().next().getRow());
    System.out.println("Make your move:");
  }
}
