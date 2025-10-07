package it.units.sdm.quoridor.model.movemanagement.actioncheckers;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.*;

import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class WallPlacementChecker implements ActionChecker<Wall> {
  public CheckResult isValidAction(AbstractGame game, Wall target) {
    if (checkNumberOfWalls(game.getPlayingPawn())) {
      if (switch (target.orientation()) {
        case HORIZONTAL -> checkHorizontalWallPosition(game.getGameBoard(), target.startingTile());
        case VERTICAL -> checkVerticalWallPosition(game.getGameBoard(), target.startingTile());
      }) {
        return QuoridorCheckResult.OKAY;
      }
      return QuoridorCheckResult.INVALID_WALL_POSITION;
    }
    return QuoridorCheckResult.END_OF_AVAILABLE_WALLS;
  }

  private boolean checkNumberOfWalls(AbstractPawn pawn) {
    return pawn.getNumberOfWalls() > 0;
  }

  private boolean checkHorizontalWallPosition(AbstractGameBoard gameBoard, AbstractTile wallStartingTile) {
    try {
      AbstractTile tileBelowStartingTile = gameBoard.getAdjacentTile(wallStartingTile, DOWN);
      AbstractTile tileRightToStartingTile = gameBoard.getAdjacentTile(wallStartingTile, RIGHT);

      if (gameBoard.isThereAWallOrEdge(tileBelowStartingTile, RIGHT) && gameBoard.isThereAWallOrEdge(wallStartingTile, RIGHT)) {
        return false;
      }

      return !gameBoard.isThereAWall(wallStartingTile, DOWN) && !gameBoard.isThereAWall(tileRightToStartingTile, DOWN);
    } catch (OutOfGameBoardException e) {
      return false;
    }
  }

  private boolean checkVerticalWallPosition(AbstractGameBoard gameBoard, AbstractTile wallStartingTile) {
    try {
      AbstractTile tileAboveStartingTile = gameBoard.getAdjacentTile(wallStartingTile, UP);
      AbstractTile tileLeftToStartingTile = gameBoard.getAdjacentTile(wallStartingTile, LEFT);

      if (gameBoard.isThereAWall(wallStartingTile, UP) && gameBoard.isThereAWall(tileLeftToStartingTile, UP)) {
        return false;
      }

      if (gameBoard.isThereAWallOrEdge(tileLeftToStartingTile, UP) && gameBoard.isThereAWallOrEdge(wallStartingTile, UP)) {
        return false;
      }

      return !gameBoard.isThereAWallOrEdge(wallStartingTile, LEFT) && !gameBoard.isThereAWallOrEdge(tileAboveStartingTile, LEFT);
    } catch (OutOfGameBoardException e) {
      return false;
    }
  }
}
