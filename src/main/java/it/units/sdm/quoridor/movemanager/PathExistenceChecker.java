package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.utils.Directions;

import java.util.*;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.FREE;
import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;

public class PathExistenceChecker implements ActionChecker<GameBoard> {
  @Override
  public boolean checkAction(Game game, GameBoard dummyGameBoard) {
    for (Pawn pawn : game.getPawns()) {
      Map<Tile, Integer> potentials = new HashMap<>();
      Set<Tile> toVisitTiles = new HashSet<>();

      //Init
      for (Tile tile : dummyGameBoard.getTiles()) {
        toVisitTiles.add(tile);
        potentials.put(tile, Integer.MAX_VALUE);

      }

      Tile startingTile = pawn.getCurrentTile();
      potentials.put(startingTile, 0);

      //===================

      visitTiles(dummyGameBoard, startingTile, toVisitTiles, potentials);

      if (!checkPathExistence(potentials, pawn.getDestinationTiles()))
        return false;
    }

    return true;
  }

  private void visitTiles(GameBoard gameBoard, Tile startingTile, Set<Tile> toVisitTiles, Map<Tile, Integer> potentials) {
    Tile visitedTile = startingTile;

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

  private boolean checkPathExistence(Map<Tile, Integer> potentials, List<Tile> destinationTiles) {
    boolean existsPath = false;

    for (Tile tile : potentials.keySet()) {
      if (destinationTiles.contains(tile)) {
        if (potentials.get(tile) == 0)
          existsPath = true;
      }
    }

    return existsPath;
  }

  private void visitAdjacentTile(GameBoard gameBoard, Tile visitedTile, Directions.Direction direction, Map<Tile, Integer> potentials) {
    Tile adjacentTile = gameBoard.getAdjacentTile(visitedTile, direction);
    if (visitedTile.getLink(direction) == FREE) {
      if (potentials.get(adjacentTile) > potentials.get(visitedTile))
        potentials.put(adjacentTile, potentials.get(visitedTile));
    } else if (visitedTile.getLink(direction) == WALL) {
      if (potentials.get(adjacentTile) > potentials.get(visitedTile) + 1)
        potentials.put(adjacentTile, potentials.get(visitedTile) + 1);
    }
  }

  private Tile makeAndGetTileDefinitive(Set<Tile> toVisitTiles, Map<Tile, Integer> potentials) {
    Tile minTile = null;

    for (Tile tile : toVisitTiles) {
      if (minTile == null || potentials.get(minTile) > potentials.get(tile))
        minTile = tile;
    }

    return minTile;
  }
}
