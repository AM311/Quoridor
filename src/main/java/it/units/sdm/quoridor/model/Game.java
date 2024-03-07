package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.movemanager.*;
import it.units.sdm.quoridor.utils.Direction;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.Direction.*;

public class Game {
  private final List<Pawn> pawns;
  private final GameBoard gameBoard;
  private Pawn playingPawn;
  private final GameActionManager actionManager;
  private long maxIterations = 10000000;      //todo remove
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

  public void movePawn(GameBoard.Tile destinationTile) {
    actionManager.performAction(new PawnMover(), new PawnMovementChecker(), destinationTile);
  }

  public boolean pathExists(Pawn pawn) {
    Set<GameBoard.Tile> visited = new HashSet<>();
    int goalRow = (gameBoard.getSideLength() - 1) - pawn.getStartingTile().getRow();
    return dfs(pawn.getCurrentTile(), goalRow, visited);
  }

  private boolean dfs(GameBoard.Tile currentTile, int goalRow, Set<GameBoard.Tile> visited) {
    if (currentTile.getRow() == goalRow) {
      return true;
    }
    --maxIterations;
    if (maxIterations <= 0) {
      return false;
    }
    visited.add(currentTile);
    for (Map.Entry<Direction, GameBoard.LinkState> link : currentTile.getLinks().entrySet()) {
      if (link.getValue() == FREE) {
        switch (link.getKey()) {
          case RIGHT -> {
            if (!visited.contains(gameBoard.getAdjacentTile(currentTile, RIGHT))) {
              if (dfs(gameBoard.getAdjacentTile(currentTile, RIGHT), goalRow, new HashSet<>(visited))) {
                return true;
              }
            }
          }
          case LEFT -> {
            if (!visited.contains(gameBoard.getAdjacentTile(currentTile, LEFT))) {
              if (dfs(gameBoard.getAdjacentTile(currentTile, LEFT), goalRow, new HashSet<>(visited))) {
                return true;
              }
            }
          }
          case DOWN -> {
            if (!visited.contains(gameBoard.getAdjacentTile(currentTile, DOWN))) {
              if (dfs(gameBoard.getAdjacentTile(currentTile, DOWN), goalRow, new HashSet<>(visited))) {
                return true;
              }
            }
          }
          case UP -> {
            if (!visited.contains(gameBoard.getAdjacentTile(currentTile, UP))) {
              if (dfs(gameBoard.getAdjacentTile(currentTile, UP), goalRow, new HashSet<>(visited))) {
                return true;
              }
            }
          }

        }
      }
    }
    return false;
  }
}
