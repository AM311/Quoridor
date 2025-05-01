package it.units.sdm.quoridor.GUI;

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


public class GameController {
  private final AbstractGame game;
  private GameBoardPanel boardPanel;
  private GameGUI mainGUI;
  private DialogManager dialogManager;

  public GameController(AbstractGame game) {
    this.game = game;
  }

  public void connectComponents(GameGUI mainGUI, GameBoardPanel boardPanel, DialogManager dialogManager) {
    this.mainGUI = mainGUI;
    this.boardPanel = boardPanel;
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

      boardPanel.updatePawnPosition(oldPosition, targetPosition, playerIndex);
      boardPanel.clearHighlights();
      boardPanel.setCurrentAction(GameBoardPanel.Action.DO_NOTHING);

      if (game.isGameFinished()) {
        dialogManager.showGameFinishedDialog(playerIndex);
      } else {
        mainGUI.onTurnComplete();
      }
    } catch (InvalidParameterException | InvalidActionException e) {
      dialogManager.showNotificationDialog("Can't move to " + (targetPosition.row() + 1) + ","
              + (targetPosition.column() + 1), game.getPlayingPawnIndex());
    }
  }

  public void attemptPlaceWall(Position position, WallOrientation orientation) {
    try {
      game.placeWall(position, orientation);

      boardPanel.updateWallVisualization(position, orientation);
      boardPanel.setCurrentAction(GameBoardPanel.Action.DO_NOTHING);

      mainGUI.onWallPlaced(game.getPlayingPawnIndex(), game.getPlayingPawn().getNumberOfWalls());
      mainGUI.onTurnComplete();
    } catch (InvalidActionException | InvalidParameterException e) {
      String orientationStr = orientation.toString().toLowerCase();
      String article = orientation.equals(WallOrientation.HORIZONTAL) ? "an " : "a ";
      String message = "Can't place " + article + orientationStr + " wall at "
              + (position.row() + 1) + "," + (position.column() + 1);
      dialogManager.showNotificationDialog(message, game.getPlayingPawnIndex());
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