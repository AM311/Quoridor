package testDoubles;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.controller.server.Logger;
import it.units.sdm.quoridor.controller.server.ServerProtocolCommands;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static it.units.sdm.quoridor.controller.parser.QuoridorParser.CommandType.MOVE;
import static it.units.sdm.quoridor.controller.parser.QuoridorParser.CommandType.WALL;
import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;

public class StubServerStandardGUIQuoridorGameEngine extends StubStandardGUIQuoridorGameEngine{
  private final BufferedReader reader;
  private final BufferedReader socketReader;
  private final BufferedWriter socketWriter;

  protected int loopCounter;
  protected boolean isLoopStoppedAfterOneRound;
  protected boolean isPawnMoved;
  protected boolean isWallPlaced;
  protected boolean hasGameToActuallyRestart;
  protected boolean hasGameActuallyRestarted;

  public StubServerStandardGUIQuoridorGameEngine(BufferedReader reader, AbstractQuoridorBuilder builder, StatisticsCounter statisticsCounter, QuoridorParser parser, BufferedWriter socketWriter, BufferedReader socketReader) {
    super(builder, statisticsCounter, parser);
    this.reader = reader;
    this.socketReader = socketReader;
    this.socketWriter = socketWriter;
  }

  @Override
  public void runGame() throws BuilderException{
    createGame();
    statisticsCounter.setGame(game);

    try {
      listenSocket();
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void handleTileClick(Position targetPosition) {

    try {
      switch (currentGUIAction) {
        case MOVE -> {
          movePawn(targetPosition);
          isPawnMoved = true;
          statisticsCounter.updateGameMoves(String.valueOf(game.getPlayingPawn()));
          sendCommand(MOVE, targetPosition, null);

          if (game.isGameFinished()) {
            handleEndGame(false);
          } else {
            game.changeRound();
          }
        }
        case PLACE_HORIZONTAL_WALL -> {
          placeWall(targetPosition, HORIZONTAL);
          isWallPlaced = true;
          statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
          sendCommand(WALL, targetPosition, HORIZONTAL);
          game.changeRound();
        }
        case PLACE_VERTICAL_WALL -> {
          placeWall(targetPosition, VERTICAL);
          isWallPlaced = true;
          statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
          sendCommand(WALL, targetPosition,VERTICAL);
          game.changeRound();
        }
        case DO_NOTHING -> {}
      }

      setCurrentAction(GUIQuoridorGameEngine.GUIAction.DO_NOTHING);
    } catch (InvalidParameterException | InvalidActionException e) {
      throw new RuntimeException(e);
    }
  }

  private void listenSocket() throws InvalidParameterException {
    do {
      if (hasGameToActuallyRestart && hasGameRestarted) {
        hasGameActuallyRestarted = true;
        break;
      }

      String serverMessage = null;

      try {
        serverMessage = socketReader.readLine();
      } catch (IOException e) {
        quitGame();
      }

      try {
        if (Objects.equals(serverMessage, ServerProtocolCommands.PLAY.getCommandString())) {
          Logger.printLog(System.out, "Playing");
        } else {
          Logger.printLog(System.out, "Executing remote command");
          boolean commandExecuted = performCommand(serverMessage, false);

          if (commandExecuted) {
            if (game.isGameFinished()) {
              handleEndGame(true);
            } else {
              game.changeRound();
            }
          }
        }
      } catch (IOException e) {
        quitGame();
      } catch (InvalidActionException | InvalidParameterException | ParserException | BuilderException e) {
        System.err.println(e.getMessage());
      }

      loopCounter++;
      if (isLoopStoppedAfterOneRound && loopCounter == 1) {
        break;
      }
    } while (!game.isGameFinished());
  }

  protected void sendCommand(QuoridorParser.CommandType commandType, Position position, WallOrientation wallOrientation) {
    try {
      sendCommand(parser.generateString(commandType, position, wallOrientation));
    } catch (IOException e) {
      handleQuitGame();
    }
  }

  private void handleQuitGame() {
  }

  protected void sendCommand(String command) throws IOException {
    socketWriter.write(command + System.lineSeparator());
    socketWriter.flush();
  }

  protected boolean performCommand(String command, boolean sendCommand) throws ParserException, InvalidParameterException, InvalidActionException, IOException, BuilderException {
    parser.parse(command);
    Optional<Position> targetPosition = parser.getActionPosition();

    return switch (parser.getCommandType().orElseThrow()) {
      case MOVE -> {
        movePawn(targetPosition.orElse(null));
        isPawnMoved = true;
        if (sendCommand) {
          sendCommand(command);
        }
        statisticsCounter.updateGameMoves(String.valueOf(game.getPlayingPawn()));
        yield true;
      }
      case WALL -> {
        placeWall(targetPosition.orElse(null), parser.getWallOrientation().orElse(null));
        isWallPlaced = true;
        if (sendCommand) {
          sendCommand(command);
        }
        statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
        yield true;
      }
      case HELP -> false;
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

  protected void handleEndGame(boolean waitForCommand) {
    String serverMessage = null;
    isGameEnded = true;

    statisticsCounter.updateAllTotalStats();

    if (waitForCommand) {
      try {
        serverMessage = socketReader.readLine();

        if (Objects.equals(serverMessage, ServerProtocolCommands.PLAY.getCommandString())) {
          Logger.printLog(System.out, "Playing");
        } else {
          parser.parse(serverMessage);

          switch (parser.getCommandType().orElseThrow()) {
            case QUIT -> quitGame();
            case RESTART -> restartGame();
          }
        }
      } catch (IOException | ParserException | BuilderException e) {
        quitGame();
      } catch (NoSuchElementException e) {
        System.err.println("Exception with command '" + serverMessage + " ': " + e.getMessage());
      }
    }
  }

  public void setLoopStoppedAfterOneRound(boolean loopStoppedAfterOneRound) {
    isLoopStoppedAfterOneRound = loopStoppedAfterOneRound;
  }

  public void setGameHasToActuallyRestart(boolean gameHasToActuallyRestart) {
    hasGameToActuallyRestart = gameHasToActuallyRestart;
  }

  public boolean hasGameActuallyRestarted() {
    return hasGameActuallyRestarted;
  }

  public boolean isPawnMoved() {
    return isPawnMoved;
  }

  public boolean isWallPlaced() {
    return isWallPlaced;
  }

  
}
