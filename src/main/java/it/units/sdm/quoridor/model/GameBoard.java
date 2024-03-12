package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.utils.Directions.Direction;

import java.util.EnumMap;
import java.util.Map;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.EDGE;
import static it.units.sdm.quoridor.model.GameBoard.LinkState.FREE;
import static it.units.sdm.quoridor.utils.Directions.Direction.*;

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
        gameState[i][j] = new Tile(i, j, isInitialPosition(i, j));
      }
    }
    setEdgesLinks();
  }

  private static boolean isInitialPosition(int row, int column) {
    return (row == 0 && column == sideLength / 2) || (row == sideLength - 1 && column == sideLength / 2);
  }

  private void setEdgesLinks() {
    for (int i = 0; i < sideLength; i++) {
      gameState[0][i].setLink(UP, EDGE);
      gameState[i][0].setLink(LEFT, EDGE);
      gameState[sideLength - 1][i].setLink(DOWN, EDGE);
      gameState[i][sideLength - 1].setLink(RIGHT, EDGE);
    }
  }

  public boolean isInFirstRow(Tile tile) {
    return tile.row == 0;
  }

  public boolean isInLastRow(Tile tile) {
    return tile.row == sideLength - 1;
  }

  public boolean isInFirstColumn(Tile tile) {
    return tile.column == 0;
  }

  public boolean isInLastColumn(Tile tile) {
    return tile.column == sideLength - 1;
  }

  //-----
  //todo manage exceptions

  public Tile getAdjacentTile(Tile tile, Direction direction) throws OutOfGameBoardException {
    try {
      return switch (direction) {
        case UP -> gameState[tile.row - 1][tile.column];
        case DOWN -> gameState[tile.row + 1][tile.column];
        case RIGHT -> gameState[tile.row][tile.column + 1];
        case LEFT -> gameState[tile.row][tile.column - 1];
        case UP_LEFT -> gameState[tile.row - 1][tile.column - 1];
        case UP_RIGHT -> gameState[tile.row - 1][tile.column + 1];
        case DOWN_LEFT -> gameState[tile.row + 1][tile.column - 1];
        case DOWN_RIGHT -> gameState[tile.row + 1][tile.column + 1];
      };
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new OutOfGameBoardException();
    }
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
    private final int row;
    private final int column;
    private final Map<Direction, LinkState> links;
    private boolean occupied;

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