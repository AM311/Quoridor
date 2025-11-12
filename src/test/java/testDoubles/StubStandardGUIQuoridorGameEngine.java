package testDoubles;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.engine.gui.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.List;

public class StubStandardGUIQuoridorGameEngine extends GUIQuoridorGameEngine {
  private boolean gameHasToQuit;
  private boolean gameHasToRestart;
  private boolean isGameEnded;
  private boolean isGameQuit;
  private boolean isGameRestarted;
  private boolean isInvalidParameterExceptionCaught;
  private boolean isInvalidActionExceptionCaught;

  public StubStandardGUIQuoridorGameEngine(AbstractQuoridorBuilder quoridorBuilder, StatisticsCounter statisticsCounter, QuoridorParser parser) {
    super(quoridorBuilder, statisticsCounter, parser);
  }

  @Override
  public void runGame() throws BuilderException {
    createGame();
    statisticsCounter.setGame(game);
  }

  @Override
  protected void sendCommand(String command){}

  @Override
  public void handleTileClick(Position targetPosition) {
    try {
      switch (currentGUIAction) {
        case MOVE -> {
          movePawn(targetPosition);
          statisticsCounter.updateGameMoves(String.valueOf(game.getPlayingPawn()));

          if (game.isGameFinished()) {
            statisticsCounter.updateAllTotalStats();
            handleEndGame();
          } else {
            changeRound();
          }
        }
        case PLACE_HORIZONTAL_WALL -> {
          placeWall(targetPosition, WallOrientation.HORIZONTAL);
          statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
          changeRound();
        }
        case PLACE_VERTICAL_WALL -> {
          placeWall(targetPosition, WallOrientation.VERTICAL);
          statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
          changeRound();
        }
        case DO_NOTHING -> {}
      }

      setCurrentAction(GUIAction.DO_NOTHING);
    } catch (InvalidParameterException e){
      isInvalidParameterExceptionCaught = true;
    } catch (InvalidActionException e) {
      isInvalidActionExceptionCaught = true;
    } catch (BuilderException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setMoveAction() {
    setCurrentAction(GUIAction.MOVE);
  }

  @Override
  public void setPlaceWallAction() {
    setCurrentAction(GUIAction.DO_NOTHING);
  }

  @Override
  public List<Position> getValidMovePositions() {
    return List.of();
  }

  private void handleEndGame() throws BuilderException {
    isGameEnded = true;
    if (gameHasToRestart) {
      restartGame();
    }
    if (gameHasToQuit) {
      quitGame();
    }
  }

  @Override
  protected void quitGame(){
    isGameQuit = true;
  }

  @Override
  protected void restartGame() throws BuilderException {
    statisticsCounter.resetGameStats();

    createGame();
    statisticsCounter.setGame(game);
    isGameRestarted = true;
  }

  public boolean isGameEnded() {
    return isGameEnded;
  }

  public boolean isGameQuit() {
    return isGameQuit;
  }

  public boolean isGameRestarted() {
    return isGameRestarted;
  }

  public boolean isInvalidParameterExceptionCaught() {
    return isInvalidParameterExceptionCaught;
  }

  public boolean isInvalidActionExceptionCaught() {
    return isInvalidActionExceptionCaught;
  }

  public void setGameHasToQuit(boolean gameHasToQuit) {
    this.gameHasToQuit = gameHasToQuit;
  }

  public void setGameHasToRestart(boolean gameHasToRestart) {
    this.gameHasToRestart = gameHasToRestart;
  }
}
