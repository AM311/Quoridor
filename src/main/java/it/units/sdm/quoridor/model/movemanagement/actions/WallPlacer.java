package it.units.sdm.quoridor.model.movemanagement.actions;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractGameBoard;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.model.Wall;

import static it.units.sdm.quoridor.model.AbstractTile.LinkState.WALL;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class WallPlacer implements Action<Wall> {
  @Override
  public void execute(AbstractGame game, Wall target) throws InvalidActionException {
    game.getPlayingPawn().decrementNumberOfWalls();
    setWallLinks(game.getGameBoard(), target);
  }

  private void setWallLinks(AbstractGameBoard gameBoard, Wall wall) throws InvalidActionException {
    switch (wall.orientation()) {
      case HORIZONTAL -> setWallLinksForHorizontalWall(gameBoard, wall.startingTile());
      case VERTICAL -> setWallLinkForVerticalWall(gameBoard, wall.startingTile());
    }
  }

  private void setWallLinksForHorizontalWall(AbstractGameBoard gameBoard, AbstractTile startingTile) throws InvalidActionException {
    try {
      AbstractTile tileBelowStartingTile = gameBoard.getAdjacentTile(startingTile, DOWN);
      AbstractTile tileRightToStartingTile = gameBoard.getAdjacentTile(startingTile, RIGHT);
      AbstractTile tileLowRightDiagToStartingTile = gameBoard.getAdjacentTile(gameBoard.getAdjacentTile(startingTile, RIGHT), DOWN);

      startingTile.setLink(DOWN, WALL);
      tileRightToStartingTile.setLink(DOWN, WALL);
      tileBelowStartingTile.setLink(UP, WALL);
      tileLowRightDiagToStartingTile.setLink(UP, WALL);
    } catch (OutOfGameBoardException e) {
      throw new InvalidActionException();
    }
  }

  private void setWallLinkForVerticalWall(AbstractGameBoard gameBoard, AbstractTile startingTile) throws InvalidActionException {
    try {
      AbstractTile tileAboveStartingTile = gameBoard.getAdjacentTile(startingTile, UP);
      AbstractTile tileLeftToStartingTile = gameBoard.getAdjacentTile(startingTile, LEFT);
      AbstractTile tileUpLeftDiagToStartingTile = gameBoard.getAdjacentTile(gameBoard.getAdjacentTile(startingTile, LEFT), UP);

      startingTile.setLink(LEFT, WALL);
      tileAboveStartingTile.setLink(LEFT, WALL);
      tileLeftToStartingTile.setLink(RIGHT, WALL);
      tileUpLeftDiagToStartingTile.setLink(RIGHT, WALL);
    } catch (OutOfGameBoardException e) {
      throw new InvalidActionException();
    }
  }
}
