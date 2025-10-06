package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.server.Logger;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.io.IOException;
import java.util.List;

public class StandardGUIQuoridorGameEngine extends GUIQuoridorGameEngine {

  //todo CHECK SE FUNZIONA CON GAMEVIEW ESTERNA
  public StandardGUIQuoridorGameEngine(AbstractQuoridorBuilder quoridorBuilder, StatisticsCounter statisticsCounter, QuoridorParser parser) {
    super(quoridorBuilder, statisticsCounter, parser);
  }

  @Override
  public void runGame() throws BuilderException {
    createGame();
    statisticsCounter.setGame(game);
    gameView.displayGUI(true);
  }


  //todo OVERRIDATO IN GUIGAMEENGINE -- VEDERE SE FUNZIONA
  /*
  @Override
  public void handleRestartGame() throws BuilderException {
    restartGame();
  }

  @Override
  public void handleQuitGame() {
    quitGame();
  }
  */

  @Override
  protected void movePawn(Position targetPosition) throws InvalidParameterException, InvalidActionException {
    Position currentPosition = new Position(
            game.getPlayingPawn().getCurrentTile().getRow(),
            game.getPlayingPawn().getCurrentTile().getColumn()
    );
    game.movePlayingPawn(targetPosition);
    eventListener.onPawnMoved(currentPosition, targetPosition, getPlayingPawnIndex());
  }

  @Override
  protected void sendCommand(String command) throws IOException {
  }

  @Override
  protected void placeWall(Position position, WallOrientation orientation) throws InvalidParameterException, InvalidActionException {
    game.placeWall(position, orientation);
    eventListener.onWallPlaced(position, orientation, game.getPlayingPawnIndex(), game.getPlayingPawn().getNumberOfWalls());
  }

  @Override
  protected void quitGame() {
    gameView.disposeMainFrame();
    System.exit(0);
  }

  @Override
  protected void restartGame() throws BuilderException {
    Logger.printLog(System.out, "Restarting game");

    statisticsCounter.resetGameStats();

    createGame();
    statisticsCounter.setGame(game);
    gameView.displayGUI(true);

  }

  @Override
  public void handleTileClick(Position targetPosition) {
    try {
      switch (currentGUIAction) {
        case MOVE -> {
          movePawn(targetPosition);
          statisticsCounter.updateGameMoves(String.valueOf(game.getPlayingPawn()));

          if (game.isGameFinished()) {
            statisticsCounter.updateAllTotalStats();
            eventListener.onGameFinished();
          } else {
            eventListener.onRoundFinished(true);
          }
        }
        case PLACE_HORIZONTAL_WALL -> {
          placeWall(targetPosition, WallOrientation.HORIZONTAL);
          statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
          eventListener.onRoundFinished(true);
        }
        case PLACE_VERTICAL_WALL -> {
          placeWall(targetPosition, WallOrientation.VERTICAL);
          statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
          eventListener.onRoundFinished(true);
        }
        case DO_NOTHING -> eventListener.displayNotification("Choose an action", true);
      }

      setCurrentAction(GUIAction.DO_NOTHING);
    } catch (InvalidParameterException | InvalidActionException e) {
      eventListener.displayNotification(e.getMessage(), true);
    }
  }

  @Override
  public void setMoveAction() {
    setCurrentAction(GUIAction.MOVE);
    try {
      eventListener.highlightValidMoves();
    } catch (Exception e) {
      eventListener.displayNotification(e.getMessage(), true);
    }
  }

  @Override
  public void setPlaceWallAction() {
    setCurrentAction(GUIAction.DO_NOTHING);
    eventListener.clearHighlights();
    if (game.getPlayingPawn().getNumberOfWalls() > 0) {
      eventListener.displayWallDirectionButtons(getPlayingPawnIndex());
    } else {
      eventListener.displayNotification("No walls available!", true);
    }
  }

  @Override
  public List<Position> getValidMovePositions() {
    return game.getValidMovePositions();
  }
}