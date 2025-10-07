package it.units.sdm.quoridor;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.parser.StandardQuoridorParser;
import it.units.sdm.quoridor.controller.engine.QuoridorGameEngine;
import it.units.sdm.quoridor.controller.engine.cli.StandardCLIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.engine.gui.StandardGUIQuoridorGameEngine;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Starter {
  public static void main(String[] args) {
    try {
      int numOfPlayers = Integer.parseInt(args[0]);

      QuoridorGameEngine engine = switch (args[1]) {
          case "CLI" ->
                  new StandardCLIQuoridorGameEngine(new BufferedReader(new InputStreamReader(System.in)), new StandardQuoridorParser(), new StdQuoridorBuilder(numOfPlayers), new StatisticsCounter());
          case "GUI" ->
                  new StandardGUIQuoridorGameEngine(new StdQuoridorBuilder(numOfPlayers), new StatisticsCounter(), new StandardQuoridorParser());
          default -> throw new InvalidParameterException("Invalid game mode.");
        };

      engine.runGame();
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      System.err.println("You must provide an integer number of players as parameter!");
    } catch (InvalidParameterException e) {
      System.err.println("Invalid parameters: " + e.getMessage());
    } catch (BuilderException e) {
      System.err.println("Exception encountered while creating the Game: " + e.getMessage());
    }
  }
}
