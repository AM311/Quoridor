package testDoubles;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.engine.gui.StandardGUIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

public class StubStandardGUIQuoridorGameEngine extends StandardGUIQuoridorGameEngine {
  private boolean isGameEnded;
  private boolean isGameQuit;
  private boolean isGameRestarted;
  private boolean showQuitRestartDialog;


  public StubStandardGUIQuoridorGameEngine(AbstractQuoridorBuilder quoridorBuilder, StatisticsCounter statisticsCounter, QuoridorParser parser) {
    super(quoridorBuilder, statisticsCounter, parser);
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
            isGameEnded = true;
            if(showQuitRestartDialog) eventListener.displayQuitRestartDialog();
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
  protected void quitGame(){
    isGameQuit = true;
  }

  @Override
  protected void restartGame() throws BuilderException {
    isGameRestarted = true;
    statisticsCounter.resetGameStats();

    createGame();
    statisticsCounter.setGame(game);
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

  public void setShowQuitRestartDialog(boolean showQuitRestartDialog) {
    this.showQuitRestartDialog = showQuitRestartDialog;
  }
}
