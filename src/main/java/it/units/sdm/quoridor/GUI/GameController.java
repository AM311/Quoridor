package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.movemanagement.actioncheckers.PawnMovementChecker;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.ArrayList;
import java.util.List;

public class GameController implements GameActionHandler {

  public enum Action {
    MOVE, PLACE_VERTICAL_WALL, PLACE_HORIZONTAL_WALL, DO_NOTHING
  }

  private final AbstractGame game;
  protected Action currentAction = Action.DO_NOTHING;
  private GameEventListener eventListener;

  public GameController(AbstractGame game) {
    this.game = game;
  }

  public void setEventListener(GameEventListener eventListener) {
    this.eventListener = eventListener;
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

  public void changeRound() {
    game.changeRound();
  }

  public void setCurrentAction(Action currentAction) {
    this.currentAction = currentAction;
  }

  @Override
  public void handleTileClick(Position targetPosition) {
    switch (currentAction) {
      case MOVE -> attemptPawnMove(targetPosition);
      case PLACE_HORIZONTAL_WALL -> attemptPlaceWall(targetPosition, WallOrientation.HORIZONTAL);
      case PLACE_VERTICAL_WALL -> attemptPlaceWall(targetPosition, WallOrientation.VERTICAL);
      case DO_NOTHING -> eventListener.displayNotification("Choose an action!", true);
    }
  }

  private void attemptPawnMove(Position targetPosition) {
    try {
      Position oldPosition = new Position(
              game.getPlayingPawn().getCurrentTile().getRow(),
              game.getPlayingPawn().getCurrentTile().getColumn()
      );
      game.movePlayingPawn(targetPosition);
      eventListener.onPawnMoved(oldPosition, targetPosition, getPlayingPawnIndex());
      setCurrentAction(Action.DO_NOTHING);

      if (isGameFinished()) {
        eventListener.showGameFinishedDialog();
      } else {
        eventListener.onTurnComplete();
      }
    } catch (InvalidParameterException | InvalidActionException e) {
      eventListener.displayNotification("Can't move to " + (targetPosition.row() + 1) + ","  + (targetPosition.column() + 1), true);
    }
  }

  private void attemptPlaceWall(Position position, WallOrientation orientation) {
    try {
      game.placeWall(position, orientation);
      eventListener.updateWallVisualization(position, orientation);
      setCurrentAction(Action.DO_NOTHING);
      eventListener.onWallPlaced(game.getPlayingPawnIndex(), game.getPlayingPawn().getNumberOfWalls());
      eventListener.onTurnComplete();
    } catch (InvalidActionException | InvalidParameterException e) {
      String orientationStr = orientation.toString().toLowerCase();
      String article = orientation.equals(WallOrientation.HORIZONTAL) ? "an " : "a ";
      String message = "Can't place " + article + orientationStr + " wall at " + (position.row() + 1) + "," + (position.column() + 1);
      eventListener.displayNotification(message, true);
    }
  }

  @Override
  public List<Position> getValidMovePositions() {
    List<Position> validPositions = new ArrayList<>();
    ActionChecker<AbstractTile> checker = new PawnMovementChecker();
    int gameBoardSize = game.getGameBoard().getSideLength();
    try {
      for (int i = 0; i < gameBoardSize; i++) {
        for (int j = 0; j < gameBoardSize; j++) {
          Position position = new Position(i, j);
          if (checker.isValidAction(game, game.getGameBoard().getTile(position))) {
            validPositions.add(position);
          }
        }
      }
    } catch (InvalidParameterException e) {
      eventListener.displayNotification("Can't get valid move positions", true);
    }
    return validPositions;
  }

  @Override
  public boolean isGameFinished() {
    return game.isGameFinished();
  }

  @Override
  public void setMoveAction() {
    setCurrentAction(Action.MOVE);
    try {
      eventListener.highlightValidMoves();
    } catch (Exception ex) {
      eventListener.displayNotification("Can't highlight moves!", true);
    }
  }

  @Override
  public void setPlaceWallAction() {
    setCurrentAction(Action.DO_NOTHING);
    eventListener.clearHighlights();
    if (game.getPlayingPawn().getNumberOfWalls() > 0) {
      eventListener.displayWallDirectionButtons(getPlayingPawnIndex());
    } else {
      eventListener.displayNotification("No walls available!", true);
    }
  }

  @Override
  public void restartGame() {
    try {
      BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(game.getPawns().size()));
      AbstractGame newGame = builderDirector.makeGame();
      GameController newGameController = new GameController(newGame);
      eventListener.displayNotification("Game restarted!", false);
      if (eventListener instanceof GameView) {
        GameView gameView = new GameView(newGameController);
        gameView.displayGUI();
      }
    } catch (InvalidParameterException | BuilderException e) {
      eventListener.displayNotification("Error restarting game: " + e.getMessage(), true);
    }
  }
}