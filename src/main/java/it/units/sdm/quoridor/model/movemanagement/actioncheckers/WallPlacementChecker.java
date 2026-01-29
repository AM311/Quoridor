package it.units.sdm.quoridor.model.movemanagement.actioncheckers;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.Wall;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.abstracts.AbstractGameBoard;
import it.units.sdm.quoridor.model.abstracts.AbstractTile;

import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class WallPlacementChecker implements ActionChecker<Wall> {
  @Override
  public CheckResult isValidAction(AbstractGame game, Wall target) {
    if (game.getPlayingPawn().getNumberOfWalls() > 0) {
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

  private boolean checkHorizontalWallPosition(AbstractGameBoard gameBoard, AbstractTile wallStartingTile) {
    try {
      AbstractTile tileBelowStartingTile = gameBoard.getAdjacentTile(wallStartingTile, DOWN);
      AbstractTile tileRightToStartingTile = gameBoard.getAdjacentTile(wallStartingTile, RIGHT);

      if (tileBelowStartingTile.isThereAWallOrEdge(RIGHT) && wallStartingTile.isThereAWallOrEdge(RIGHT)) {
        return false;
      }

      return !wallStartingTile.isThereAWall(DOWN) && !tileRightToStartingTile.isThereAWall(DOWN);
    } catch (OutOfGameBoardException e) {
      return false;
    }
  }

  private boolean checkVerticalWallPosition(AbstractGameBoard gameBoard, AbstractTile wallStartingTile) {
    try {
      AbstractTile tileAboveStartingTile = gameBoard.getAdjacentTile(wallStartingTile, UP);
      AbstractTile tileLeftToStartingTile = gameBoard.getAdjacentTile(wallStartingTile, LEFT);

      if (wallStartingTile.isThereAWall(UP) && tileLeftToStartingTile.isThereAWall(UP)) {
        return false;
      }

      if (tileLeftToStartingTile.isThereAWallOrEdge(UP) && wallStartingTile.isThereAWallOrEdge(UP)) {
        return false;
      }

      return !wallStartingTile.isThereAWallOrEdge(LEFT) && !tileAboveStartingTile.isThereAWallOrEdge(LEFT);
    } catch (OutOfGameBoardException e) {
      return false;
    }
  }
}
