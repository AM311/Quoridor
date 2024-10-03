package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.utils.directions.DiagonalDirection;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class PawnMovementChecker implements ActionChecker<Tile> {
  @Override
  public boolean checkAction(Game game, Tile destinationTile) {
    GameBoard gameBoard = game.getGameBoard();
    Pawn playingPawn = game.getPlayingPawn();
    Tile currentTile = playingPawn.getCurrentTile();

    if (destinationTile.isOccupied()) {
      return false;
    }

    return isValidStraightMove(gameBoard, destinationTile, currentTile)
            || isValidDiagonalMove(gameBoard, destinationTile, currentTile)
            || isJumpingPawnMove(gameBoard, destinationTile, currentTile);
  }

  private boolean isValidStraightMove(GameBoard gameBoard, Tile destinationTile, Tile currentTile) {
    for (StraightDirection direction : StraightDirection.values()) {
      try {
        if (destinationTile.equals(gameBoard.getAdjacentTile(currentTile, direction)))
          return !gameBoard.isThereAWall(currentTile, destinationTile);
      } catch (OutOfGameBoardException ignored) {
      }
    }
    return false;
  }

  private boolean isValidDiagonalMove(GameBoard gameBoard, Tile destinationTile, Tile currentTile) {
    for (DiagonalDirection direction : DiagonalDirection.values()) {
      try {
        if (destinationTile.equals(gameBoard.getAdjacentTile(currentTile, direction)))
          return isSpecialMove(gameBoard, currentTile, direction);
      } catch (OutOfGameBoardException outOfGameBoardException) {
        return false;
      }
    }
    return false;
  }

  private boolean isJumpingPawnMove(GameBoard gameBoard, Tile destinationTile, Tile currentTile) {
    for (StraightDirection direction : StraightDirection.values()) {
      try {
        if (destinationTile.equals(gameBoard.getLandingTile(currentTile, direction)))
          return (gameBoard.getAdjacentTile(currentTile, direction).isOccupied()
                  && !gameBoard.isThereAWallOrEdge(gameBoard.getAdjacentTile(currentTile, direction), direction));
      } catch (OutOfGameBoardException ignored) {
      }
    }
    return false;
  }

  private boolean isSpecialMove(GameBoard gameBoard, Tile currentTile, DiagonalDirection direction) throws OutOfGameBoardException{
    return switch (direction) {
      case UP_RIGHT -> checkSpecialMove(gameBoard, currentTile, UP, RIGHT);
      case UP_LEFT -> checkSpecialMove(gameBoard, currentTile, UP, LEFT);
      case DOWN_RIGHT -> checkSpecialMove(gameBoard, currentTile, DOWN, RIGHT);
      case DOWN_LEFT -> checkSpecialMove(gameBoard, currentTile, DOWN, LEFT);
    };
  }

  private boolean checkSpecialMove(GameBoard gameBoard, Tile currentTile, StraightDirection verticalDirection, StraightDirection horizontalDirection) throws OutOfGameBoardException {
    return checkVerticalSpecialMove(gameBoard, currentTile, verticalDirection, horizontalDirection) ||
            checkHorizontalSpecialMove(gameBoard, currentTile, verticalDirection, horizontalDirection);
  }

  private boolean checkVerticalSpecialMove(GameBoard gameBoard, Tile currentTile, StraightDirection verticalDirection, StraightDirection horizontalDirection) throws OutOfGameBoardException{
    Tile adjacentTile = gameBoard.getAdjacentTile(currentTile, verticalDirection);

    return adjacentTile.isOccupied()
            && gameBoard.isThereAWallOrEdge(adjacentTile, verticalDirection)
            && !gameBoard.isThereAWallOrEdge(adjacentTile, horizontalDirection);
  }

  private boolean checkHorizontalSpecialMove(GameBoard gameBoard, Tile currentTile, StraightDirection verticalDirection, StraightDirection horizontalDirection) throws OutOfGameBoardException{
    Tile adjacentTile = gameBoard.getAdjacentTile(currentTile, horizontalDirection);

    return adjacentTile.isOccupied()
            && gameBoard.isThereAWallOrEdge(adjacentTile, horizontalDirection)
            && !gameBoard.isThereAWallOrEdge(adjacentTile, verticalDirection);
  }
}
