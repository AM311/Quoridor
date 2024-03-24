package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.utils.Directions;
import it.units.sdm.quoridor.utils.Directions.Direction;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.Directions.Direction.*;

public class PawnMovementChecker implements ActionChecker<Tile> {
  @Override
  public boolean checkAction(Game game, Tile destinationTile) {
    GameBoard gameBoard = game.getGameBoard();
    Pawn playingPawn = game.getPlayingPawn();

    if (destinationTile.isOccupied()) {
      return false;
    }

    Tile currentTile = playingPawn.getCurrentTile();
    return (isValidStraightMove(gameBoard, destinationTile, currentTile)
            || isValidDiagonalMove(gameBoard, destinationTile, currentTile)
            || isJumpingPawnMove(gameBoard, destinationTile, currentTile) ||
            isOnBorderMove(gameBoard, destinationTile, playingPawn));
  }

  private boolean isValidStraightMove(GameBoard gameBoard, Tile destinationTile, Tile currentTile) {
    for (Direction direction : Directions.getStraightDirections()) {
      try {
        if (destinationTile.equals(gameBoard.getAdjacentTile(currentTile, direction)))
          return !gameBoard.isThereAWall(destinationTile, currentTile);
      } catch (OutOfGameBoardException ignored) {
      }
    }
    return false;
  }

  private boolean isValidDiagonalMove(GameBoard gameBoard, Tile destinationTile, Tile currentTile) {
    for (Direction direction : Directions.getDiagonalDirections()) {
      try {
        if (destinationTile.equals(gameBoard.getAdjacentTile(currentTile, direction)))
          return isSpecialMove(gameBoard, currentTile, direction);
      } catch (OutOfGameBoardException ignored) {
      }
    }
    return false;
  }

  private boolean isJumpingPawnMove(GameBoard gameBoard, Tile destinationTile, Tile currentTile) {
    for (Direction direction : Directions.getStraightDirections()) {
      try {
        if (destinationTile.equals(gameBoard.getLandingTile(currentTile, direction)))
          return (gameBoard.getAdjacentTile(currentTile, direction).isOccupied()
                  && gameBoard.getAdjacentTile(currentTile, direction).getLink(direction) != WALL);
      } catch (OutOfGameBoardException ignored) {
      }
    }
    return false;
  }

  private boolean isOnBorderMove(GameBoard gameBoard, Tile destinationTile, Pawn playingPawn) {
    int currentRow = playingPawn.getCurrentTile().getRow();
    int currentColumn = playingPawn.getCurrentTile().getColumn();
    int destinationRow = destinationTile.getRow();
    int destinationColumn = destinationTile.getColumn();
    if (destinationRow == 0 || destinationRow == 8) {
      return gameBoard.getGameState()[destinationRow][currentColumn].isOccupied();
    }
    if (destinationColumn == 0 || destinationColumn == 8) {
      return gameBoard.getGameState()[currentRow][destinationColumn].isOccupied();
    }
    return false;
  }

  private boolean isSpecialMove(GameBoard gameBoard, Tile currentTile, Direction direction) {
    return switch (direction) {
      case UP_RIGHT -> checkSpecialMove(gameBoard, currentTile, UP, RIGHT);
      case UP_LEFT -> checkSpecialMove(gameBoard, currentTile, UP, LEFT);
      case DOWN_RIGHT -> checkSpecialMove(gameBoard, currentTile, DOWN, RIGHT);
      case DOWN_LEFT -> checkSpecialMove(gameBoard, currentTile, DOWN, LEFT);
      default -> throw new IllegalArgumentException();
    };
  }

  private boolean checkSpecialMove(GameBoard gameBoard, Tile currentTile, Direction verticalDirection, Direction horizontalDirection) {
    return (
            (gameBoard.getAdjacentTile(currentTile, verticalDirection).isOccupied()
                    && gameBoard.getAdjacentTile(currentTile, verticalDirection).getLink(verticalDirection) == WALL
                    && gameBoard.getAdjacentTile(currentTile, verticalDirection).getLink(horizontalDirection) != WALL)
                    ||
                    (gameBoard.getAdjacentTile(currentTile, horizontalDirection).isOccupied()
                            && gameBoard.getAdjacentTile(currentTile, horizontalDirection).getLink(horizontalDirection) == WALL
                            && gameBoard.getAdjacentTile(currentTile, horizontalDirection).getLink(verticalDirection) != WALL));
  }
}
