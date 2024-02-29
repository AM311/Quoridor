package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.utils.Direction;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.Direction.*;

import java.util.EnumMap;
import java.util.Map;

public class GameBoard {
  public static final int sideLength = 9;
  private final Tile[][] gameState;

  public GameBoard() {
    gameState = new Tile[sideLength][sideLength];
    fillGameState();
  }

  private void fillGameState() {
    for (int i = 0; i < sideLength; i++) {
      for (int j = 0; j < sideLength; j++) {
        gameState[i][j] = new Tile(i, j, isStartingBox(i, j));
      }
    }
    setEdgesLinks();
  }

  private void setEdgesLinks() {
    for (int i = 0; i < sideLength; i++) {
      gameState[0][i].setLink(UP, EDGE);
      gameState[i][0].setLink(LEFT, EDGE);
      gameState[sideLength - 1][i].setLink(DOWN, EDGE);
      gameState[i][sideLength - 1].setLink(RIGHT, EDGE);
    }
  }

  private static boolean isStartingBox(int row, int column) {
    return (row == 0 && column == sideLength / 2)
            || (row == sideLength - 1 && column == sideLength / 2);
  }

  public Tile getAdjacentTile(Tile tile, Direction direction) {
    return switch (direction) {
      case UP -> gameState[tile.row - 1][tile.column];
      case DOWN -> gameState[tile.row + 1][tile.column];
      case RIGHT -> gameState[tile.row][tile.column + 1];
      case LEFT -> gameState[tile.row][tile.column - 1];
    };
  }

  public int getSideLength() {
    return sideLength;
  }

  public Tile[][] getGameState() {
    return gameState;
  }

  public enum LinkState {
    FREE, WALL, EDGE
  }

  public class Tile {
    private boolean occupied;
    private final int row;
    private final int column;
    private final Map<Direction, LinkState> links;

    public Tile(int row, int column, boolean occupied) {
      this.row = row;
      this.column = column;
      this.occupied = occupied;
      links = new EnumMap<>(Map.of(UP, FREE, RIGHT, FREE, DOWN, FREE, LEFT, FREE));
    }

    public boolean isOccupied() {
      return occupied;
    }

    public void setOccupied(boolean occupied) {
      this.occupied = occupied;
    }

    public int getRow() {
      return row;
    }

    public int getColumn() {
      return column;
    }

    public LinkState getLink(Direction direction) {
      return links.get(direction);
    }

    public Map<Direction, LinkState> getLinks() {
      return links;
    }

    public void setLink(Direction direction, LinkState linkState) {
      links.put(direction, linkState);
    }
  }
}