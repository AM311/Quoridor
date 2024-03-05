package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.utils.Direction;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.HashSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.Direction.*;

public class Game {
  private final List<Player> players;
  private final GameBoard gameBoard;
  private long maxIterations = 10000000;

  public Game(List<Player> players, GameBoard gameBoard) {
    this.players = players;
    this.gameBoard = gameBoard;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public GameBoard getGameBoard() {
    return gameBoard;
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
