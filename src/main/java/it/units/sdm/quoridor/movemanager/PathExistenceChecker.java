package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.utils.Directions;

import java.util.*;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.FREE;
import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;

public class PathExistenceChecker implements ActionChecker<GameBoard> {
  @Override
  public boolean checkAction(Game game, GameBoard dummyGameBoard) {
    for (Pawn pawn : game.getPawns()) {
      Map<GameBoard.Tile, Integer> potentials = new HashMap<>();
      Set<GameBoard.Tile> toVisitTiles = new HashSet<>();
      int goalRow = (dummyGameBoard.getSideLength() - 1) - pawn.getStartingTile().getRow();                //todo extend for 4 players

      //Init
      for (GameBoard.Tile[] tileArr : dummyGameBoard.getGameState()) {
        for (GameBoard.Tile tile : tileArr) {
          toVisitTiles.add(tile);
          potentials.put(tile, Integer.MAX_VALUE);
        }
      }

      GameBoard.Tile startingTile = pawn.getCurrentTile();
      potentials.put(startingTile, 0);

      //===================

      visitTiles(dummyGameBoard, startingTile, toVisitTiles, potentials);

      if (!checkPathExistence(potentials, goalRow))
        return false;
    }

    return true;
  }

  private void visitTiles(GameBoard gameBoard, GameBoard.Tile startingTile, Set<GameBoard.Tile> toVisitTiles, Map<GameBoard.Tile, Integer> potentials) {
    GameBoard.Tile visitedTile = startingTile;

    while (!toVisitTiles.isEmpty()) {
      toVisitTiles.remove(visitedTile);

      for (Directions.Direction dir : Directions.Direction.values()) {
        try {
          visitAdjacentTile(gameBoard, visitedTile, dir, potentials);
        } catch (OutOfGameBoardException ignored) {
        }
      }
      visitedTile = makeAndGetTileDefinitive(toVisitTiles, potentials);
    }
  }

  private boolean checkPathExistence(Map<GameBoard.Tile, Integer> potentials, int goalRow) {
    boolean existsPath = false;

    for (GameBoard.Tile tile : potentials.keySet()) {
      if (tile.getRow() == goalRow) {
        if (potentials.get(tile) == 0)
          existsPath = true;
      }
    }

    return existsPath;
  }

  private void visitAdjacentTile(GameBoard gameBoard, GameBoard.Tile visitedTile, Directions.Direction direction, Map<GameBoard.Tile, Integer> potentials) {
    GameBoard.Tile adjacentTile = gameBoard.getAdjacentTile(visitedTile, direction);
    if (visitedTile.getLink(direction) == FREE) {
      if (potentials.get(adjacentTile) > potentials.get(visitedTile))
        potentials.put(adjacentTile, potentials.get(visitedTile));
    } else if (visitedTile.getLink(direction) == WALL) {
      if (potentials.get(adjacentTile) > potentials.get(visitedTile) + 1)
        potentials.put(adjacentTile, potentials.get(visitedTile) + 1);
    }
  }

  private GameBoard.Tile makeAndGetTileDefinitive(Set<GameBoard.Tile> toVisitTiles, Map<GameBoard.Tile, Integer> potentials) {
    GameBoard.Tile minTile = null;

    for (GameBoard.Tile tile : toVisitTiles) {
      if (minTile == null || potentials.get(minTile) > potentials.get(tile))
        minTile = tile;
    }

    return minTile;
  }
}
