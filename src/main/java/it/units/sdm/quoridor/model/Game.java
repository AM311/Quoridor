package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.movemanager.*;
import it.units.sdm.quoridor.utils.Direction;

import java.util.*;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.Direction.*;

public class Game {
  private final List<Pawn> pawns;
  private final GameBoard gameBoard;
  private final GameActionManager actionManager;
  private Pawn playingPawn;

  public Game(List<Pawn> pawns, GameBoard gameBoard) {
    this.pawns = pawns;
    this.gameBoard = gameBoard;
    this.actionManager = new GameActionManager(this);
  }

  public List<Pawn> getPawns() {
    return pawns;
  }

  public GameBoard getGameBoard() {
    return gameBoard;
  }

  public Pawn getPlayingPawn() {
    return playingPawn;
  }

  public void setPlayingPawn(Pawn playingPawn) {
    this.playingPawn = playingPawn;
  }

  public void placeWall(Wall wall) {
    actionManager.performAction(new WallPlacer(), new WallPlacementChecker(), wall);
  }

  public void movePawn(Tile destinationTile) {
    actionManager.performAction(new PawnMover(), new PawnMovementChecker(), destinationTile);
  }

  //============================================================================

  // >>> NEW ALGORITHM IMPLEMENTATION <<<

  public boolean pathExists() {
    for (Pawn pawn : pawns) {
      Map<Tile, Integer> potentials = new HashMap<>();
      Set<Tile> toVisitTiles = new HashSet<>();
      int goalRow = (gameBoard.getSideLength() - 1) - pawn.getStartingTile().getRow();                //works only for 2 players

      //Init
      for (Tile[] tileArr : gameBoard.getGameState()) {
        for (Tile tile : tileArr) {
          toVisitTiles.add(tile);
          potentials.put(tile, Integer.MAX_VALUE);
        }
      }

      Tile startingTile = pawn.getCurrentTile();
      potentials.put(startingTile, 0);

      //===================

      visitTiles(startingTile, toVisitTiles, potentials);

      if (!checkPathExistence(potentials, goalRow))
        return false;
    }

    return true;
  }

  private void visitTiles(Tile startingTile, Set<Tile> toVisitTiles, Map<Tile, Integer> potentials) {
    Tile visitedTile = startingTile;

    while (!toVisitTiles.isEmpty()) {
      toVisitTiles.remove(visitedTile);

      for(Direction dir : Direction.values()) {
        visitAdjacentTile(visitedTile, dir, potentials);
      }

      visitedTile = makeAndGetTileDefinitive(toVisitTiles, potentials);
    }
  }

  private boolean checkPathExistence(Map<Tile, Integer> potentials, int goalRow) {
    boolean existsPath = false;

    for (Tile tile : potentials.keySet()) {
      if (tile.getRow() == goalRow) {
        if (potentials.get(tile) == 0)
          existsPath = true;
      }
    }

    return existsPath;
  }

  private void visitAdjacentTile(Tile visitedTile, Direction left, Map<Tile, Integer> potentials) {
    if (visitedTile.getLink(left) != EDGE) {
      Tile adjacentTile = gameBoard.getAdjacentTile(visitedTile, left);

      if (visitedTile.getLink(left) == FREE) {
        if (potentials.get(adjacentTile) > potentials.get(visitedTile))
          potentials.put(adjacentTile, potentials.get(visitedTile));
      } else if (visitedTile.getLink(left) == WALL) {
        if (potentials.get(adjacentTile) > potentials.get(visitedTile) + 1)
          potentials.put(adjacentTile, potentials.get(visitedTile) + 1);
      }
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