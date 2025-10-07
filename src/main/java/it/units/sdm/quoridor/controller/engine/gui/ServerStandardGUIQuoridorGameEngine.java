package it.units.sdm.quoridor.controller.engine.gui;

import it.units.sdm.quoridor.controller.StatisticsCounter;
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

public class ServerStandardGUIQuoridorGameEngine extends StandardGUIQuoridorGameEngine {
  private final BufferedReader socketReader;
  private final BufferedWriter socketWriter;

  public ServerStandardGUIQuoridorGameEngine(AbstractQuoridorBuilder quoridorBuilder, StatisticsCounter statisticsCounter, BufferedReader socketReader, BufferedWriter socketWriter, QuoridorParser parser) {
    super(quoridorBuilder, statisticsCounter, parser);
    this.socketReader = socketReader;
    this.socketWriter = socketWriter;
  }

  @Override
  public void runGame() throws BuilderException {
    createGame();
    statisticsCounter.setGame(game);
    gameView.displayGUI(false);

    listenSocket();
  }

  @Override
  public void handleTileClick(Position targetPosition) {
    Logger.printLog(System.out, "Handling tile click");

    try {
      switch (currentGUIAction) {
        case MOVE -> {
          movePawn(targetPosition);
          statisticsCounter.updateGameMoves(String.valueOf(game.getPlayingPawn()));
          sendCommand(QuoridorParser.CommandType.MOVE, targetPosition, null);

          if (game.isGameFinished()) {
            handleEndGame(false);
          } else {
            eventListener.onRoundFinished(false);
          }
        }
        case PLACE_HORIZONTAL_WALL -> {
          placeWall(targetPosition, WallOrientation.HORIZONTAL);
          statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
          sendCommand(QuoridorParser.CommandType.WALL, targetPosition, WallOrientation.HORIZONTAL);
          eventListener.onRoundFinished(false);
        }
        case PLACE_VERTICAL_WALL -> {
          placeWall(targetPosition, WallOrientation.VERTICAL);
          statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
          sendCommand(QuoridorParser.CommandType.WALL, targetPosition, WallOrientation.VERTICAL);
          eventListener.onRoundFinished(false);
        }
        case DO_NOTHING -> eventListener.displayNotification("Wait for your round or choose an action", true);
      }

      setCurrentAction(GUIAction.DO_NOTHING);
    } catch (InvalidParameterException | InvalidActionException e) {
      eventListener.displayNotification(e.getMessage(), true);
    }
  }

  protected void restartGame() throws BuilderException {
    Logger.printLog(System.out, "Restarting game");

    statisticsCounter.resetGameStats();

    createGame();
    statisticsCounter.setGame(game);
    gameView.displayGUI(false);
  }

  @Override
  protected void sendCommand(String command) throws IOException {
    socketWriter.write(command + System.lineSeparator());
    socketWriter.flush();
  }

  private void listenSocket() {
    do {
      String serverMessage = null;

      try {
        serverMessage = socketReader.readLine();
      } catch (IOException e) {
        System.err.println("Unable to communicate with server: " + e.getMessage());
        quitGame();
      }

      Logger.printLog(System.out, "Command received: " + serverMessage);

      try {
        if (Objects.equals(serverMessage, ServerProtocolCommands.PLAY.getCommandString())) {
          Logger.printLog(System.out, "Playing");
          gameView.displayCommandsForCurrentPlayer();
        } else {
          Logger.printLog(System.out, "Executing remote command");
          boolean commandExecuted = performCommand(serverMessage, false);

          if (commandExecuted) {
            if (game.isGameFinished()) {
              handleEndGame(true);
            } else {
              eventListener.onRoundFinished(false);
            }
          }
        }
      } catch (IOException e) {
        System.err.println("Unable to communicate with server: " + e.getMessage());
        quitGame();
      } catch (InvalidActionException | InvalidParameterException | ParserException | BuilderException e) {
        System.err.println(e.getMessage());
      }
    } while (!game.isGameFinished());
  }

  protected void handleEndGame(boolean waitForCommand) {
    Logger.printLog(System.out, "Ending game");
    String serverMessage = null;

    statisticsCounter.updateAllTotalStats();
    eventListener.onGameFinished();

    if (waitForCommand) {
      try {
        serverMessage = socketReader.readLine();

        if (Objects.equals(serverMessage, ServerProtocolCommands.PLAY.getCommandString())) {
          eventListener.displayQuitRestartDialog();
        } else {
          parser.parse(serverMessage);

          switch (parser.getCommandType().orElseThrow()) {
            case QUIT -> quitGame();
            case RESTART -> restartGame();
          }
        }
      } catch (IOException e) {
        System.err.println("Unable to communicate with server: " + e.getMessage());
        quitGame();
      } catch (ParserException | BuilderException e) {
        System.err.println("Exception while handling Game End: " + e.getMessage());
        quitGame();
      } catch (NoSuchElementException e) {
        System.err.println("Exception with command '" + serverMessage + " ': " + e.getMessage());
      }
    }
  }
}