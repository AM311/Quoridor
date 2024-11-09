package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;


public class CLIGameEngine implements QuoridorGameEngine {

  private final InputProvider inputProvider;
  private final QuoridorParser parser;

  private boolean testingMode = false;

  public CLIGameEngine(InputProvider inputProvider, QuoridorParser parser) {
    this.inputProvider = inputProvider;
    this.parser = parser;
  }


  @Override
  public AbstractGame createGame() {
    System.out.println("Choose number of players (2 or 4): ");
    AbstractGame game = null;
    while (game == null) {
      try {
        int numPlayers = Integer.parseInt(inputProvider.nextLine());
        BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(numPlayers));
        game = builderDirector.makeGame();
      } catch (InvalidParameterException | BuilderException | NumberFormatException e) {
        System.out.println("Enter a valid number:");
      }
    }
    return game;
  }

  @Override
  public void startGame(AbstractGame game) {
    while(true){
      executeRound(game);

      if (testingMode) {
        break;
      }
    }
  }

  @Override
  public void endGame() {
    System.out.println("Game over!");
  }

  private void executeRound(AbstractGame game) {
    printWallsConvention();

    String command = inputProvider.nextLine();
    performCommand(command);

    if(game.isGameFinished()) {
      System.out.println(game.getPlayingPawn() + " won!");
      endGame();
    }
    game.changeRound();
  }

  private void performCommand(String command) {
    System.out.println("Command " + command + " executed");
  }

  private void printWallsConvention() {
    final String RED_SQUARE = "\u001B[31m■\u001B[0m";

    System.out.println("How walls are placed:");

    System.out.println("                          \n");
    System.out.println("     +     +     +        \n");
    System.out.println("     |                    \n");
    System.out.println("  v  +     +     +        \n");
    System.out.println("     |  " + RED_SQUARE + "\n");
    System.out.println("     + ─── + ─── +        \n");
    System.out.println("           h              \n");
    System.out.println("                          \n");
  }

  public void enableTestingMode() {
    this.testingMode = true;
  }
}
