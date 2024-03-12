package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.utils.Directions.Direction;
import it.units.sdm.quoridor.utils.Directions;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.Directions.Direction.*;

public class PawnMovementChecker implements ActionChecker<Tile> {
  @Override
  public boolean checkAction(GameBoard gameBoard, Pawn playingPawn, Tile target) {
    if (target.isOccupied()) {
      return false;
    }
    Tile currentTile = playingPawn.getCurrentTile();
    if (!isStraightMove(gameBoard, target, currentTile)) {
      if (isDiagonalMove(gameBoard, target, currentTile)) {
        return isSpecialMove(gameBoard, target, playingPawn);
      }
      return isJumpingPawnMove(gameBoard, target, playingPawn);
    }
    return !isThereAWall(gameBoard, target, playingPawn);
  }


  private boolean isStraightMove(GameBoard gameBoard, Tile destinationTile, Tile currentTile) {
    for (Direction direction : Directions.getStraightDirections()) {
      try {
        if (destinationTile.equals(gameBoard.getAdjacentTile(currentTile, direction)))
          return true;
      } catch (OutOfGameBoardException ignored) {
      }
    }
    return false;
  }

  private boolean isDiagonalMove(GameBoard gameBoard, Tile destinationTile, Tile currentTile) {
    for (Direction direction : Directions.getDiagonalDirections()) {
      try {
        if (destinationTile.equals(gameBoard.getAdjacentTile(currentTile, direction)))
          return true;
      } catch (OutOfGameBoardException ignored) {
      }
    }
    return false;
  }

  private boolean isThereAWall(GameBoard gameBoard, GameBoard.Tile destinationTile, Pawn playingPawn) {
    int currentRow = playingPawn.getCurrentTile().getRow();
    int currentColumn = playingPawn.getCurrentTile().getColumn();
    int destinationRow = destinationTile.getRow();
    int destinationColumn = destinationTile.getColumn();

    if (currentRow - destinationRow == 1 && currentColumn == destinationColumn) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(UP) == WALL;
    }
    if (currentRow - destinationRow == -1 && currentColumn == destinationColumn) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(DOWN) == WALL;
    }
    if (currentColumn - destinationColumn == 1 && currentRow == destinationRow) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(LEFT) == WALL;
    }
    if (currentColumn - destinationColumn == -1 && currentRow == destinationRow) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(RIGHT) == WALL;
    }
    return false;
  }

  private boolean isSpecialMove(GameBoard gameBoard, GameBoard.Tile destinationTile, Pawn playingPawn) {
    int currentRow = playingPawn.getCurrentTile().getRow();
    int currentColumn = playingPawn.getCurrentTile().getColumn();
    int destinationRow = destinationTile.getRow();
    int destinationColumn = destinationTile.getColumn();

    if ((currentRow - destinationRow == 1 && currentColumn - destinationColumn == -1
            && gameBoard.getGameState()[currentRow][currentColumn + 1].isOccupied()
            && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(RIGHT) == WALL
            && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(UP) != WALL)
            || (currentRow - destinationRow == 1 && currentColumn - destinationColumn == -1
            && gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
            && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(UP) == WALL
            && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(RIGHT) != WALL)) {
      return true;
    }
    if ((currentRow - destinationRow == 1 && currentColumn - destinationColumn == 1
            && gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
            && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(UP) == WALL
            && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(LEFT) != WALL)
            || (currentRow - destinationRow == 1 && currentColumn - destinationColumn == 1
            && gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
            && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(LEFT) == WALL
            && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(UP) != WALL)) {
      return true;
    }
    if ((currentRow - destinationRow == -1 && currentColumn - destinationColumn == 1
            && gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
            && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(LEFT) == WALL
            && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(DOWN) != WALL)
            || (currentRow - destinationRow == -1 && currentColumn - destinationColumn == 1
            && gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
            && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(DOWN) == WALL
            && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(LEFT) != WALL)) {
      return true;
    }
    return (currentRow - destinationRow == -1 && currentColumn - destinationColumn == -1
            && gameBoard.getGameState()[currentRow][currentColumn + 1].isOccupied()
            && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(RIGHT) == WALL
            && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(DOWN) != WALL)
            || (currentRow - destinationRow == -1 && currentColumn - destinationColumn == -1
            && gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
            && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(DOWN) == WALL
            && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(RIGHT) != WALL);
  }

  private boolean isJumpingPawnMove(GameBoard gameBoard, GameBoard.Tile destinationTile, Pawn playingPawn) {
    int currentRow = playingPawn.getCurrentTile().getRow();
    int currentColumn = playingPawn.getCurrentTile().getColumn();
    int destinationRow = destinationTile.getRow();
    int destinationColumn = destinationTile.getColumn();

    if (currentRow - destinationRow == 2 && currentColumn == destinationColumn) {
      return gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
              && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(DOWN) != WALL;
    }
    if (currentRow - destinationRow == -2 && currentColumn == destinationColumn) {
      return gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
              && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(UP) != WALL;
    }
    if (currentRow == destinationRow && currentColumn - destinationColumn == 2) {
      return gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
              && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(RIGHT) != WALL;
    }
    if (currentRow == destinationRow && currentColumn - destinationColumn == -2) {
      return gameBoard.getGameState()[currentRow][currentColumn + 1].isOccupied()
              && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(LEFT) != WALL;
    }
    return false;
  }
}
