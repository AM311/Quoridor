package it.units.sdm.quoridor;

import it.units.sdm.quoridor.cli.engine.QuoridorGameEngine;
import it.units.sdm.quoridor.cli.engine.StandardCLIQuoridorGameEngine;
import it.units.sdm.quoridor.cli.parser.StandardQuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Starter {
  public static void main(String[] args) {
    //todo Generalize to different types of UIs

    try {
      int numOfPlayers = Integer.parseInt(args[0]);

      QuoridorGameEngine engine = new StandardCLIQuoridorGameEngine(new BufferedReader(new InputStreamReader(System.in)), new StandardQuoridorParser(), new StdQuoridorBuilder(numOfPlayers));
      engine.runGame();
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      System.err.println("You must provide an integer number of players as parameter!");
    } catch (InvalidParameterException e) {
      System.err.println("Invalid number of players!");
    } catch (BuilderException e) {
      System.err.println("Exception encountered while creating the Game: " + e.getMessage());
    }
  }
}
