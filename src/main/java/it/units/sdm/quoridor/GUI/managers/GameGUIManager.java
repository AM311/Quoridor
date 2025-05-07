package it.units.sdm.quoridor.GUI.managers;

import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.GUI.GameGUI;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.movemanagement.actioncheckers.PawnMovementChecker;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.ArrayList;
import java.util.List;


public class GameGUIManager {
  private final AbstractGame game;
  private GameBoardGUI gameBoardGUI;
  private GameGUI gameGUI;
  private DialogManager dialogManager;

  public GameGUIManager(AbstractGame game) {
    this.game = game;
  }

  public void connectComponents(GameGUI mainGUI, GameBoardGUI boardPanel, DialogManager dialogManager) {
    this.gameGUI = mainGUI;
    this.gameBoardGUI = boardPanel;
    this.dialogManager = dialogManager;
  }

  public AbstractGame getGame() {
    return game;
  }

  public int getPlayingPawnIndex() {
    return game.getPlayingPawnIndex();
  }

  public void changeRound() {
    game.changeRound();
  }

  public void attemptPawnMove(Position targetPosition) {
    try {
      Position oldPosition = new Position(
              game.getPlayingPawn().getCurrentTile().getRow(),
              game.getPlayingPawn().getCurrentTile().getColumn()
      );
      int playerIndex = game.getPlayingPawnIndex();

      game.movePlayingPawn(targetPosition);

      gameBoardGUI.updatePawnPosition(oldPosition, targetPosition, playerIndex);
      gameBoardGUI.clearHighlights();
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.DO_NOTHING);

      if (game.isGameFinished()) {
        dialogManager.showGameFinishedDialog();
      } else {
        gameGUI.onTurnComplete();
      }
    } catch (InvalidParameterException | InvalidActionException e) {
      dialogManager.showNotificationDialog(
              "Can't move to " + (targetPosition.row() + 1) + ","  + (targetPosition.column() + 1),
              true
      );
    }
  }

  public void attemptPlaceWall(Position position, WallOrientation orientation) {
    try {
      game.placeWall(position, orientation);

      gameBoardGUI.updateWallVisualization(position, orientation);
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.DO_NOTHING);

      gameGUI.onWallPlaced(game.getPlayingPawnIndex(), game.getPlayingPawn().getNumberOfWalls());
      gameGUI.onTurnComplete();
    } catch (InvalidActionException | InvalidParameterException e) {
      String orientationStr = orientation.toString().toLowerCase();
      String article = orientation.equals(WallOrientation.HORIZONTAL) ? "an " : "a ";
      String message = "Can't place " + article + orientationStr + " wall at "
              + (position.row() + 1) + "," + (position.column() + 1);
      dialogManager.showNotificationDialog(message, true);
    }
  }

  public List<Position> getValidMovePositions() throws InvalidParameterException {
    List<Position> validPositions = new ArrayList<>();
    ActionChecker<AbstractTile> checker = new PawnMovementChecker();
    int gameBoardSize = game.getGameBoard().getSideLength();

    for (int i = 0; i < gameBoardSize; i++) {
      for (int j = 0; j < gameBoardSize; j++) {
        Position position = new Position(i, j);
        if (checker.isValidAction(game, game.getGameBoard().getTile(position))) {
          validPositions.add(position);
        }
      }
    }

    return validPositions;
  }

  public boolean isGameFinished() {
    return game.isGameFinished();
  }
}