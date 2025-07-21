package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.GUI.view.GameView;
import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.movemanagement.actioncheckers.PawnMovementChecker;
import it.units.sdm.quoridor.movemanagement.actioncheckers.QuoridorCheckResult;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.ArrayList;
import java.util.List;

public class StandardGUIQuoridorGameEngine extends GUIQuoridorGameEngine {
  protected GUIAction currentGUIAction = GUIAction.DO_NOTHING;


  public StandardGUIQuoridorGameEngine(AbstractQuoridorBuilder quoridorBuilder, StatisticsCounter statisticsCounter) {
    super(quoridorBuilder, statisticsCounter);
  }

  @Override
  public void runGame() throws BuilderException {
    createGame();
    GameView gameView = new GameView(this);
    gameView.displayGUI();
  }

  @Override
  public int getNumberOfPlayers() {
    return game.getPawns().size();
  }

  @Override
  public int getSideLength() {
    return game.getGameBoard().getSideLength();
  }

  @Override
  public List<AbstractPawn> getPawns() {
    return game.getPawns();
  }

  @Override
  public int getPlayingPawnIndex() {
    return game.getPlayingPawnIndex();
  }

  @Override
  public void changeRound() {
    game.changeRound();
  }

  @Override
  public void setCurrentAction(GUIAction currentGUIAction) {
    this.currentGUIAction = currentGUIAction;
  }

  @Override
  public void handleTileClick(Position targetPosition) {
    switch (currentGUIAction) {
      case MOVE -> attemptPawnMove(targetPosition);
      case PLACE_HORIZONTAL_WALL -> attemptPlaceWall(targetPosition, WallOrientation.HORIZONTAL);
      case PLACE_VERTICAL_WALL -> attemptPlaceWall(targetPosition, WallOrientation.VERTICAL);
      case DO_NOTHING -> eventListener.onInvalidAction("Choose an action");
    }
  }

  private void attemptPawnMove(Position targetPosition) {
    try {
      Position currentPosition = new Position(
              game.getPlayingPawn().getCurrentTile().getRow(),
              game.getPlayingPawn().getCurrentTile().getColumn()
      );
      game.movePlayingPawn(targetPosition);
      statisticsCounter.updateGameMoves(String.valueOf(game.getPlayingPawn()));
      eventListener.onPawnMoved(currentPosition, targetPosition, getPlayingPawnIndex());
      if (isGameFinished()) {
        statisticsCounter.updateAllTotalStats(game);
        eventListener.onGameFinished(statisticsCounter);
      } else {
        eventListener.onRoundFinished();
      }
      setCurrentAction(GUIAction.DO_NOTHING);
    } catch (InvalidParameterException | InvalidActionException e) {
      eventListener.onInvalidAction(e.getMessage());
    }
  }

  private void attemptPlaceWall(Position position, WallOrientation orientation) {
    try {
      game.placeWall(position, orientation);
      statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
      setCurrentAction(GUIAction.DO_NOTHING);
      eventListener.onWallPlaced(position, orientation, game.getPlayingPawnIndex(), game.getPlayingPawn().getNumberOfWalls());
    } catch (InvalidActionException | InvalidParameterException e) {
      eventListener.onInvalidAction(e.getMessage());
    }
  }


  // TODO da chiamare game.getValidMovePositions()
  @Override
  public List<Position> getValidMovePositions() {
    List<Position> validPositions = new ArrayList<>();
    ActionChecker<AbstractTile> checker = new PawnMovementChecker();
    int gameBoardSize = game.getGameBoard().getSideLength();
    try {
      for (int i = 0; i < gameBoardSize; i++) {
        for (int j = 0; j < gameBoardSize; j++) {
          Position position = new Position(i, j);
          if (checker.isValidAction(game, game.getGameBoard().getTile(position)).equals(QuoridorCheckResult.OKAY)) {
            validPositions.add(position);
          }
        }
      }
    } catch (InvalidParameterException e) {
      eventListener.onInvalidAction(e.getMessage());
    }
    return validPositions;
  }

  @Override
  public boolean isGameFinished() {
    return game.isGameFinished();
  }

  @Override
  public void setMoveAction() {
    setCurrentAction(GUIAction.MOVE);
    try {
      eventListener.highlightValidMoves();
    } catch (Exception e) {
      eventListener.onInvalidAction(e.getMessage());
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

// TODO capire se lasciare così o fare in altro modo
  @Override
  public void restartGame() {
    try {
      new StandardGUIQuoridorGameEngine(new StdQuoridorBuilder(getNumberOfPlayers()), statisticsCounter).runGame();
    } catch (InvalidParameterException | BuilderException e) {
      eventListener.onInvalidAction(e.getMessage());
    }
  }
}