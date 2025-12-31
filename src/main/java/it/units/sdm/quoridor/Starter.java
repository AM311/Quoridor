package it.units.sdm.quoridor;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.engine.abstracts.QuoridorGameEngine;
import it.units.sdm.quoridor.controller.engine.abstracts.AbstractEngineFactory;
import it.units.sdm.quoridor.controller.engine.EngineContext.ContextBuilder;
import it.units.sdm.quoridor.controller.engine.LocalEngineFactory;
import it.units.sdm.quoridor.controller.parser.StandardQuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.FactoryException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Starter {
  private static final AbstractEngineFactory factory = new LocalEngineFactory();
  private static final ContextBuilder contextBuilder = new ContextBuilder();

  public static void main(String[] args) {
    try {
      QuoridorGameEngine engine = getQuoridorGameEngine(args);

      engine.runGame();
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      System.err.println("You must provide an integer number of players as parameter!");
    } catch (InvalidParameterException e) {
      System.err.println("Invalid parameters: " + e.getMessage());
    } catch (BuilderException e) {
      System.err.println("Exception encountered while creating the Game: " + e.getMessage());
    } catch (FactoryException e) {
      System.err.println("Exception encountered while creating the Engine: " + e.getMessage());
    }
  }

  private static QuoridorGameEngine getQuoridorGameEngine(String[] args) throws InvalidParameterException, FactoryException {
    int numOfPlayers = Integer.parseInt(args[0]);

    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(numOfPlayers));

    return switch (args[1]) {
      case "CLI" ->
              factory.createCLIEngine(contextBuilder.setReader(new BufferedReader(new InputStreamReader(System.in))).build());
      case "GUI" -> factory.createGUIEngine(contextBuilder.build());
      default -> throw new InvalidParameterException("Invalid game mode.");
    };
  }
}
