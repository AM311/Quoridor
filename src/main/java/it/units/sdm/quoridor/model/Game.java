package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.movemanagement.actionmanagers.ActionManager;
import it.units.sdm.quoridor.utils.ActionController;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.List;

public class Game extends AbstractGame {
  public final ActionManager actionManager;
  public ActionController<Wall> placeWallActionController;
  public ActionController<AbstractTile> movePawnActionController;

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

  @Override
  public Game clone() throws CloneNotSupportedException {
    Game clonedGame = (Game) super.clone();

    clonedGame.movePawnActionController = new ActionController<>(movePawnActionController.action());
    clonedGame.placeWallActionController = new ActionController<>(placeWallActionController.action());

    return clonedGame;
  }
}