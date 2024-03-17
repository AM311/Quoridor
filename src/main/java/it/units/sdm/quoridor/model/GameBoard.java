package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.utils.Directions;
import it.units.sdm.quoridor.utils.Directions.Direction;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.Directions.Direction.*;

public class GameBoard implements Cloneable {
  public static final int sideLength = 9;
  private Tile[][] gameState;

  public GameBoard() {
    gameState = new Tile[sideLength][sideLength];
    fillGameState();
  }

  private void fillGameState() {
    for (int i = 0; i < sideLength; i++)
      for (int j = 0; j < sideLength; j++) {
        gameState[i][j] = new Tile(i, j, isInitialPosition(i, j));
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

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof GameBoard gameBoard))
      return false;
    return Arrays.deepEquals(getGameState(), gameBoard.getGameState());
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    GameBoard cloneGameBoard = (GameBoard) super.clone();
    Tile[][] cloneGameState = new Tile[sideLength][sideLength];
    for (int i = 0; i < sideLength; i++)
      for (int j = 0; j < sideLength; j++)
        cloneGameState[i][j] = (Tile) gameState[i][j].clone();

    cloneGameBoard.gameState = cloneGameState;

    return cloneGameBoard;
  }

  public Tile[][] getGameState() {
    return gameState;
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
  public boolean isThereAWall(Tile tile1, Tile tile2) {
    for (Direction direction : Directions.getStraightDirections()) {
      try {
        if (tile2.equals(this.getAdjacentTile(tile1, direction))) {
          return tile1.getLink(direction) == WALL;
        }
      } catch (OutOfGameBoardException ignored) {
      }
    }
    return false;
  }

  public Tile getLandingTile(Tile tile, Direction direction) throws OutOfGameBoardException {
    try {
      return switch (direction) {
        case UP -> gameState[tile.row - 2][tile.column];
        case DOWN -> gameState[tile.row + 2][tile.column];
        case RIGHT -> gameState[tile.row][tile.column + 2];
        case LEFT -> gameState[tile.row][tile.column - 2];
        default -> throw new IllegalArgumentException();
      };
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new OutOfGameBoardException();
    }
  }

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

  public enum LinkState {
    FREE, WALL, EDGE
  }

  public class Tile implements Cloneable {
    private final int row;
    private final int column;
    private Map<Direction, LinkState> links;
    private boolean occupied;

    public Tile(int row, int column, boolean occupied) {
      this.row = row;
      this.column = column;
      this.occupied = occupied;
      links = new EnumMap<>(Map.of(UP, FREE, RIGHT, FREE, DOWN, FREE, LEFT, FREE));
    }

    public void setLink(Direction direction, LinkState linkState) {
      links.put(direction, linkState);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof Tile tile))
        return false;
      return getRow() == tile.getRow() && getColumn() == tile.getColumn() && isOccupied() == tile.isOccupied() && Objects.equals(getLinks(), tile.getLinks());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
      Tile cloneTile = (Tile) super.clone();
      cloneTile.links = new EnumMap<>(Map.of(UP, this.getLink(UP), RIGHT, this.getLink(RIGHT), DOWN, this.getLink(DOWN), LEFT, this.getLink(LEFT)));

      return cloneTile;
    }

    public LinkState getLink(Direction direction) {
      return links.get(direction);
    }

    public int getRow() {
      return row;
    }

    public int getColumn() {
      return column;
    }

    public boolean isOccupied() {
      return occupied;
    }

    public void setOccupied(boolean occupied) {
      this.occupied = occupied;
    }

    public Map<Direction, LinkState> getLinks() {
      return links;
    }
  }
}