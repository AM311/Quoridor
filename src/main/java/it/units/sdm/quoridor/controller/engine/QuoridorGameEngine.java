package it.units.sdm.quoridor.controller.engine;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.io.IOException;
import java.util.Optional;

public abstract class QuoridorGameEngine {
  protected AbstractQuoridorBuilder builder;
  protected AbstractGame game;
  protected QuoridorParser parser;
  protected StatisticsCounter statisticsCounter;

  public QuoridorGameEngine(AbstractQuoridorBuilder builder, StatisticsCounter statisticsCounter, QuoridorParser parser) {
    this.builder = builder;
    this.statisticsCounter = statisticsCounter;
    this.parser = parser;
  }

  protected void createGame() throws BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(builder);
    this.game = builderDirector.makeGame();
  }

  public abstract void runGame() throws BuilderException;

  protected boolean performCommand(String command, boolean sendCommand) throws ParserException, InvalidParameterException, InvalidActionException, IOException, BuilderException {
    parser.parse(command);
    Optional<Position> targetPosition = parser.getActionPosition();

    return switch (parser.getCommandType().orElseThrow()) {
      case MOVE -> {
        movePawn(targetPosition.orElse(null));
        if (sendCommand) {
          sendCommand(command);
        }
        statisticsCounter.updateGameMoves(String.valueOf(game.getPlayingPawn()));
        yield true;
      }
      case WALL -> {
        placeWall(targetPosition.orElse(null), parser.getWallOrientation().orElse(null));

        if (sendCommand) {
          sendCommand(command);
        }
        statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
        yield true;
      }
      case HELP -> {
        printHelp();
        yield false;
      }
      case QUIT -> {
        if (sendCommand) {
          sendCommand(command);
        }
        quitGame();
        yield false;
      }
      case RESTART -> {
        if (sendCommand) {
          sendCommand(command);
        }
        restartGame();
        yield false;
      }
    };
  }

  protected void movePawn(Position targetPosition) throws InvalidActionException, InvalidParameterException {
    game.movePlayingPawn(targetPosition);
  }

  protected abstract void sendCommand(String command) throws IOException;

  protected void placeWall(Position targetPosition, WallOrientation orientation) throws InvalidActionException, InvalidParameterException {
    game.placeWall(targetPosition, orientation);
  }

  protected abstract void printHelp();

  protected void quitGame() {
    System.exit(0);
  }

  protected abstract void restartGame() throws BuilderException;

  protected void sendCommand(QuoridorParser.CommandType commandType, Position position, WallOrientation wallOrientation) {
    try {
      sendCommand(parser.generateString(commandType, position, wallOrientation));
    } catch (IOException e) {
      System.err.println("Unable to communicate with server: " + e.getMessage());
      handleQuitGame();
    }
  }

  protected void handleQuitGame() {
    sendCommand(QuoridorParser.CommandType.QUIT, null, null);
    quitGame();
  }

  protected void handleRestartGame() throws BuilderException {
    sendCommand(QuoridorParser.CommandType.RESTART, null, null);
    restartGame();
  }

  public StatisticsCounter getStatisticsCounter() {
    return statisticsCounter;
  }

  public AbstractGame getGame() {
    return game;
  }
}
