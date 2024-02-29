package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.utils.Direction;

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

  public void placeWall(boolean orientation, GameBoard.Tile startingTile) {

  }


  public boolean checkWallPosition(boolean orientation, GameBoard.Tile startingTile) {
    if (orientation) {
      if (startingTile.getRow() == 8 || startingTile.getColumn() == 8) {
        return false;
      }
      GameBoard.Tile tileBelowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];
      GameBoard.Tile tileRightToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() + 1];
      if (startingTile.getLink(RIGHT) == WALL && tileBelowStartingTile.getLink(RIGHT) == WALL) {
        return false;
      }
      if (startingTile.getLink(DOWN) == WALL || tileRightToStartingTile.getLink(DOWN) == WALL) {
        return false;
      }
    } else {
      if (startingTile.getRow() == 0 || startingTile.getColumn() == 0) {
        return false;
      }
      GameBoard.Tile tileAboveStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn()];
      GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];
      if (startingTile.getLink(UP) == WALL && tileLeftToStartingTile.getLink(UP) == WALL) {
        return false;
      }
      if (startingTile.getLink(LEFT) == WALL || tileAboveStartingTile.getLink(LEFT) == WALL) {
        return false;
      }
    }
    return true;
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
