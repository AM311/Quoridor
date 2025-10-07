package it.units.sdm.quoridor.model.movemanagement.actioncheckers;

import it.units.sdm.quoridor.exceptions.NotAdjacentTilesException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractGameBoard;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.utils.directions.DiagonalDirection;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class PawnMovementChecker implements ActionChecker<AbstractTile> {
  @Override
  public CheckResult isValidAction(AbstractGame game, AbstractTile destinationTile) {
    AbstractGameBoard gameBoard = game.getGameBoard();
    AbstractPawn playingPawn = game.getPlayingPawn();
    AbstractTile currentTile = playingPawn.getCurrentTile();

    if (destinationTile.isOccupiedBy().isPresent()) {
      return QuoridorCheckResult.OCCUPIED_TILE;
    }
    if (isValidStraightMove(gameBoard, destinationTile, currentTile)
            || isValidDiagonalMove(gameBoard, destinationTile, currentTile)
            || isValidJumpingMove(gameBoard, destinationTile, currentTile)) {
      return QuoridorCheckResult.OKAY;
    }

    return QuoridorCheckResult.INVALID_MOVEMENT;
  }

  private boolean isValidStraightMove(AbstractGameBoard gameBoard, AbstractTile destinationTile, AbstractTile currentTile) {

    try {
      return !gameBoard.isThereAWall(currentTile, destinationTile);
    } catch (NotAdjacentTilesException e) {
      return false;
    }
  }

  private boolean isValidDiagonalMove(AbstractGameBoard gameBoard, AbstractTile destinationTile, AbstractTile currentTile) {
    for (DiagonalDirection direction : DiagonalDirection.values()) {
      try {
        if (destinationTile.equals(gameBoard.getAdjacentTile(currentTile, direction)))
          return isValidSpecialMove(gameBoard, currentTile, direction);
      } catch (OutOfGameBoardException ignored) {
      }
    }
    return false;
  }

  private boolean isValidJumpingMove(AbstractGameBoard gameBoard, AbstractTile destinationTile, AbstractTile currentTile) {
    for (StraightDirection direction : StraightDirection.values()) {
      try {
        if (destinationTile.equals(gameBoard.getLandingTile(currentTile, direction)))
          return (gameBoard.getAdjacentTile(currentTile, direction).isOccupiedBy().isPresent()
                  && !gameBoard.isThereAWallOrEdge(gameBoard.getAdjacentTile(currentTile, direction), direction)
                  && !gameBoard.isThereAWallOrEdge(currentTile, direction));
      } catch (OutOfGameBoardException ignored) {
      }
    }
    return false;
  }

  private boolean isValidSpecialMove(AbstractGameBoard gameBoard, AbstractTile currentTile, DiagonalDirection direction) throws OutOfGameBoardException {
    return switch (direction) {
      case UP_RIGHT -> checkSpecialMove(gameBoard, currentTile, UP, RIGHT);
      case UP_LEFT -> checkSpecialMove(gameBoard, currentTile, UP, LEFT);
      case DOWN_RIGHT -> checkSpecialMove(gameBoard, currentTile, DOWN, RIGHT);
      case DOWN_LEFT -> checkSpecialMove(gameBoard, currentTile, DOWN, LEFT);
    };
  }

  private boolean checkSpecialMove(AbstractGameBoard gameBoard, AbstractTile currentTile, StraightDirection verticalDirection, StraightDirection horizontalDirection) throws OutOfGameBoardException {
    return checkVerticalSpecialMove(gameBoard, currentTile, verticalDirection, horizontalDirection) ||
            checkHorizontalSpecialMove(gameBoard, currentTile, verticalDirection, horizontalDirection);
  }

  private boolean checkVerticalSpecialMove(AbstractGameBoard gameBoard, AbstractTile currentTile, StraightDirection verticalDirection, StraightDirection horizontalDirection) throws OutOfGameBoardException {
    AbstractTile adjacentTile = gameBoard.getAdjacentTile(currentTile, verticalDirection);

    return adjacentTile.isOccupiedBy().isPresent()
            && gameBoard.isThereAWallOrEdge(adjacentTile, verticalDirection)
            && !gameBoard.isThereAWallOrEdge(adjacentTile, horizontalDirection);
  }

  private boolean checkHorizontalSpecialMove(AbstractGameBoard gameBoard, AbstractTile currentTile, StraightDirection verticalDirection, StraightDirection horizontalDirection) throws OutOfGameBoardException {
    AbstractTile adjacentTile = gameBoard.getAdjacentTile(currentTile, horizontalDirection);

    return adjacentTile.isOccupiedBy().isPresent()
            && gameBoard.isThereAWallOrEdge(adjacentTile, horizontalDirection)
            && !gameBoard.isThereAWallOrEdge(adjacentTile, verticalDirection);
  }
}
