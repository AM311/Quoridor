package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.model.Wall;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

import java.awt.*;

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

  private boolean checkHorizontalWallPosition(GameBoard gameBoard, Tile wallStartingTile) {
    try {
      Tile tileBelowStartingTile = gameBoard.getAdjacentTile(wallStartingTile, DOWN);
      Tile tileRightToStartingTile = gameBoard.getAdjacentTile(wallStartingTile, RIGHT);

      if (gameBoard.isThereAWallOrEdge(tileBelowStartingTile, RIGHT) && gameBoard.isThereAWallOrEdge(wallStartingTile, RIGHT)) {
        return false;
      }

      return !gameBoard.isThereAWall(wallStartingTile, DOWN) && !gameBoard.isThereAWall(tileRightToStartingTile, DOWN);
    } catch (OutOfGameBoardException e) {
      return false;
    }
  }

  private boolean checkVerticalWallPosition(GameBoard gameBoard, Tile wallStartingTile) {
    try {
      Tile tileAboveStartingTile = gameBoard.getAdjacentTile(wallStartingTile, UP);
      Tile tileLeftToStartingTile = gameBoard.getAdjacentTile(wallStartingTile, LEFT);

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

  private boolean checkPathExistence(Game game, Wall wall) {
    GameBoard dummyGameBoard;
    Pawn dummyPawn;

    try {
      dummyGameBoard = game.getGameBoard().clone();
      dummyPawn = new Pawn(dummyGameBoard.getStartingAndDestinationTiles().getFirst().getKey(),
              dummyGameBoard.getStartingAndDestinationTiles().getFirst().getValue(), Color.BLACK, 1);       //todo NON BELLO...
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);      //todo check whether to replace with a custom exception
    }

    try {
      new WallPlacer().execute(dummyGameBoard, dummyPawn, wall);
    } catch (InvalidActionException e) {
      return false;
    }
    return new PathExistenceChecker().checkAction(game, dummyGameBoard);
  }

}
