package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;

import java.util.Scanner;

public class CLIGameEngine implements QuoridorGameEngine{

  private final Scanner scanner;
  private final QuoridorParser parser;

  public CLIGameEngine(Scanner scanner, QuoridorParser parser) {
    this.scanner = scanner;
    this.parser = parser;
  }


  @Override
  public AbstractGame createGame() {
    System.out.println("Choose number of players (2 or 4): ");
    AbstractGame game = null;
    while (game == null) {
      try {
        int numPlayers = Integer.parseInt(scanner.nextLine());
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

  }

  @Override
  public void endGame() {

  }
}
