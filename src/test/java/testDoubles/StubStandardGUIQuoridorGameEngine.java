package testDoubles;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.abstracts.AbstractPawn;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.List;

import static it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine.GUIAction.DO_NOTHING;
import static it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine.GUIAction.MOVE;
import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;

public class StubStandardGUIQuoridorGameEngine {
  protected AbstractQuoridorBuilder builder;
  protected AbstractGame game;
  protected QuoridorParser parser;
  protected StatisticsCounter statisticsCounter;
  protected GUIQuoridorGameEngine.GUIAction currentGUIAction = DO_NOTHING;
  protected boolean gameHasToQuit;
  protected boolean gameHasToRestart;
  protected boolean isGameEnded;
  protected boolean isGameQuit;
  protected boolean hasGameRestarted;
  protected boolean isInvalidParameterExceptionCaught;
  protected boolean isInvalidActionExceptionCaught;

  public StubStandardGUIQuoridorGameEngine(AbstractQuoridorBuilder builder, StatisticsCounter statisticsCounter, QuoridorParser parser) {
    this.builder = builder;
    this.statisticsCounter = statisticsCounter;
    this.parser = parser;
  }

  public void runGame() throws BuilderException{
    createGame();
    statisticsCounter.setGame(game);
  }

  protected void createGame() throws BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(builder);
    this.game = builderDirector.makeGame();
  }

  private void changeRound() {
    game.changeRound();
  }

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
          placeWall(targetPosition, HORIZONTAL);
          statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
          changeRound();
        }
        case PLACE_VERTICAL_WALL -> {
          placeWall(targetPosition, VERTICAL);
          statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
          changeRound();
        }
        case DO_NOTHING -> {
        }
      }

      setCurrentAction(DO_NOTHING);
    } catch (InvalidParameterException e) {
      isInvalidParameterExceptionCaught = true;
    } catch (InvalidActionException e) {
      isInvalidActionExceptionCaught = true;
    } catch (BuilderException e) {
      throw new RuntimeException(e);
    }
  }

  protected void movePawn(Position targetPosition) throws InvalidParameterException, InvalidActionException {
    game.movePlayingPawn(targetPosition);
  }

  protected void placeWall(Position position, WallOrientation orientation) throws InvalidParameterException, InvalidActionException {
    game.placeWall(position, orientation);
  }

  public void setCurrentAction(GUIQuoridorGameEngine.GUIAction currentGUIAction) {
    this.currentGUIAction = currentGUIAction;
  }

  public void setMoveAction() {
    setCurrentAction(MOVE);
  }

  public void setPlaceWallAction() {
    setCurrentAction(DO_NOTHING);
  }

  public List<AbstractPawn> getPawns() {
    return game.getPawns();
  }

  public AbstractGame getGame() {
    return game;
  }

  protected void handleEndGame() throws BuilderException {
    isGameEnded = true;
    if (gameHasToRestart) {
      restartGame();
    }
    if (gameHasToQuit) {
      quitGame();
    }
  }

  protected void quitGame() {
    isGameQuit = true;
  }

  protected void restartGame() throws BuilderException {
    statisticsCounter.resetGameStats();

    createGame();
    statisticsCounter.setGame(game);
    hasGameRestarted = true;
  }

  public boolean isGameEnded() {
    return isGameEnded;
  }

  public boolean isGameQuit() {
    return isGameQuit;
  }

  public boolean isHasGameRestarted() {
    return hasGameRestarted;
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
