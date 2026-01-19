package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.abstracts.AbstractGameBoard;
import it.units.sdm.quoridor.model.abstracts.AbstractPawn;
import it.units.sdm.quoridor.model.abstracts.AbstractTile;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.QuoridorCheckResult;
import it.units.sdm.quoridor.model.movemanagement.actionmanagers.ActionManager;
import it.units.sdm.quoridor.model.movemanagement.ActionController;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.ArrayList;
import java.util.List;

public class Game extends AbstractGame {
  private final ActionManager actionManager;
  private ActionController<Wall> placeWallActionController;
  private ActionController<AbstractTile> movePawnActionController;

  public Game(AbstractGameBoard gameBoard, List<AbstractPawn> pawns, int playingPawn, ActionManager actionManager, ActionController<AbstractTile> movePawnActionController, ActionController<Wall> placeWallActionController) {
    super(gameBoard, pawns, playingPawn);
    this.actionManager = actionManager;
    this.placeWallActionController = placeWallActionController;
    this.movePawnActionController = movePawnActionController;
  }

  public void placeWall(Position startingPosition, WallOrientation wallOrientation) throws InvalidActionException, InvalidParameterException {
    AbstractTile startingTile = gameBoard.getTile(startingPosition);
    Wall wall = new Wall(wallOrientation, startingTile);

    actionManager.performAction(this, wall, placeWallActionController, false);
  }

  public void movePlayingPawn(Position destinationPosition) throws InvalidParameterException, InvalidActionException {
    AbstractTile destinationTile = gameBoard.getTile(destinationPosition);

    actionManager.performAction(this, destinationTile, movePawnActionController, false);
  }

  public void changeRound() {
    playingPawn = (playingPawn + 1) % pawns.size();
  }

  public boolean isGameFinished() {
    return getPlayingPawn().hasReachedDestination();
  }

  public List<Position> getValidMovePositions() {
    List<Position> validPositions = new ArrayList<>();
    ActionChecker<AbstractTile> checker = movePawnActionController.actionCheckers()[0];
    int gameBoardSize = getGameBoard().getSideLength();

    try {
      for (int i = 0; i < gameBoardSize; i++) {
        for (int j = 0; j < gameBoardSize; j++) {
          Position position = new Position(i, j);
          if (checker.isValidAction(this, this.getGameBoard().getTile(position)).equals(QuoridorCheckResult.OKAY)) {
            validPositions.add(position);
          }
        }
      }
    } catch (InvalidParameterException e) {
      System.out.println(e.getMessage());
    }

    return validPositions;
  }

  @Override
  public Game clone() throws CloneNotSupportedException {
    Game clonedGame = (Game) super.clone();

    clonedGame.movePawnActionController = new ActionController<>(movePawnActionController.action());
    clonedGame.placeWallActionController = new ActionController<>(placeWallActionController.action());

    return clonedGame;
  }
}