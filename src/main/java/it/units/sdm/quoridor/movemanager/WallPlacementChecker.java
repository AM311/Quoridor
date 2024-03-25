package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.model.Wall;

import java.awt.*;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class WallPlacementChecker implements ActionChecker<Wall> {
  public boolean checkAction(Game game, Wall target) {
    if (checkNumberOfWalls(game.getPlayingPawn())) {
      return switch (target.orientation()) {
        case HORIZONTAL -> checkHorizontalWallPosition(game.getGameBoard(), target.startingTile());
        case VERTICAL -> checkVerticalWallPosition(game.getGameBoard(), target.startingTile());
      } && checkPathExistence(game, target);
    } else
      return false;
  }

  private boolean checkNumberOfWalls(Pawn pawn) {
    return pawn.getNumberOfWalls() > 0;
  }

  private boolean checkHorizontalWallPosition(GameBoard gameBoard, GameBoard.Tile startingTile) {
    if (gameBoard.isInLastRow(startingTile) || gameBoard.isInLastColumn(startingTile)) {
      return false;
    }
    //todo extract method "checkCrossingWalls"?
    GameBoard.Tile tileBelowStartingTile = gameBoard.getAdjacentTile(startingTile, DOWN);
    GameBoard.Tile tileRightToStartingTile = gameBoard.getAdjacentTile(startingTile, RIGHT);

    if (startingTile.getLink(RIGHT) == WALL && tileBelowStartingTile.getLink(RIGHT) == WALL) {
      return false;
    }
    if (startingTile.getLink(DOWN) == WALL || tileRightToStartingTile.getLink(DOWN) == WALL) {
      return false;
    }
    return true;
  }

  private boolean checkVerticalWallPosition(GameBoard gameBoard, GameBoard.Tile startingTile) {
    if (gameBoard.isInFirstRow(startingTile) || gameBoard.isInFirstColumn(startingTile)) {
      return false;
    }
    //todo extract method "checkCrossingWalls"?
    GameBoard.Tile tileAboveStartingTile = gameBoard.getAdjacentTile(startingTile, UP);
    GameBoard.Tile tileLeftToStartingTile = gameBoard.getAdjacentTile(startingTile, LEFT);

    if (startingTile.getLink(UP) == WALL && tileLeftToStartingTile.getLink(UP) == WALL) {
      return false;
    }
    if (startingTile.getLink(LEFT) == WALL || tileAboveStartingTile.getLink(LEFT) == WALL) {
      return false;
    }
    return true;
  }

  private boolean checkPathExistence(Game game, Wall wall) {       //todo CHECK!!!
    GameBoard dummyGameBoard;
    Pawn dummyPawn;

    try {
      dummyGameBoard = (GameBoard) game.getGameBoard().clone();
      dummyPawn = new Pawn(dummyGameBoard.getStartingAndDestinationTiles().getFirst().getKey(),
              dummyGameBoard.getStartingAndDestinationTiles().getFirst().getValue(), Color.BLACK, 1);
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);      //todo check whether to replace with a custom exception
    }

    new WallPlacer().execute(dummyGameBoard, dummyPawn, wall);
    return new PathExistenceChecker().checkAction(game, dummyGameBoard);
  }

}
