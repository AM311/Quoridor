package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
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

  private boolean checkHorizontalWallPosition(GameBoard gameBoard, Tile startingTile) {
    try {
      if (gameBoard.isInLastRow(startingTile) || gameBoard.isInLastColumn(startingTile)) {
        return false;
      }
      //todo extract method "checkCrossingWalls"?
      Tile tileBelowStartingTile = gameBoard.getAdjacentTile(startingTile, DOWN);
      Tile tileRightToStartingTile = gameBoard.getAdjacentTile(startingTile, RIGHT);

      if (gameBoard.isThereAWallOrEdge(tileBelowStartingTile, RIGHT) && gameBoard.isThereAWallOrEdge(startingTile, RIGHT)) {
        return false;
      }

      return !gameBoard.isThereAWallOrEdge(startingTile, DOWN) && !gameBoard.isThereAWallOrEdge(tileRightToStartingTile, DOWN);
    } catch (OutOfGameBoardException e) {
      return false;
    }
  }

  private boolean checkVerticalWallPosition(GameBoard gameBoard, Tile startingTile) {
    try {
      if (gameBoard.isInFirstRow(startingTile) || gameBoard.isInFirstColumn(startingTile)) {
        return false;
      }
      //todo extract method "checkCrossingWalls"?
      Tile tileAboveStartingTile = gameBoard.getAdjacentTile(startingTile, UP);
      Tile tileLeftToStartingTile = gameBoard.getAdjacentTile(startingTile, LEFT);

      if (gameBoard.isThereAWallOrEdge(tileLeftToStartingTile, UP) && gameBoard.isThereAWallOrEdge(startingTile, UP)) {
        return false;
      }

      return !gameBoard.isThereAWallOrEdge(startingTile, LEFT) && !gameBoard.isThereAWallOrEdge(tileAboveStartingTile, LEFT);
    } catch (OutOfGameBoardException e) {
      return false;
    }
  }

  private boolean checkPathExistence(Game game, Wall wall) {
    GameBoard dummyGameBoard;
    Pawn dummyPawn;

    try {
      dummyGameBoard = game.getGameBoard().clone();
      dummyPawn = new Pawn(dummyGameBoard.getStartingAndDestinationTiles().getFirst().getKey(),
              dummyGameBoard.getStartingAndDestinationTiles().getFirst().getValue(), Color.BLACK, 1);
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);      //todo check whether to replace with a custom exception
    }

    new WallPlacer().execute(dummyGameBoard, dummyPawn, wall);
    return new PathExistenceChecker().checkAction(game, dummyGameBoard);
  }

}
