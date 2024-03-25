package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.NotAdjacentTilesException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.utils.Pair;
import it.units.sdm.quoridor.utils.directions.Direction;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.directions.DiagonalDirection.*;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

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
        gameState[i][j] = new Tile(i, j);
      }
    setEdgesLinks();
  }

  public List<Pair<Tile, List<Tile>>> getStartingAndDestinationTiles() {
    List<Tile> startingTiles = List.of(
            gameState[0][sideLength / 2],
            gameState[sideLength - 1][sideLength / 2],
            gameState[sideLength / 2][0],
            gameState[sideLength / 2][sideLength - 1]
    );

    List<List<Tile>> destinationTiles = List.of(
            getRowTiles(sideLength-1),
            getRowTiles(0),
            getColumnTiles(sideLength-1),
            getColumnTiles(0)
    );

    return IntStream.range(0, 4).mapToObj(i -> new Pair<>(startingTiles.get(i), destinationTiles.get(i))).toList();     //todo
  }

  private void setEdgesLinks() {
    for (int i = 0; i < sideLength; i++) {
      gameState[0][i].setLink(UP, EDGE);
      gameState[i][0].setLink(LEFT, EDGE);
      gameState[sideLength - 1][i].setLink(DOWN, EDGE);
      gameState[i][sideLength - 1].setLink(RIGHT, EDGE);
    }
  }

  public Tile getTile(int row, int column) throws OutOfGameBoardException {
    try {
      return gameState[row][column];
    } catch (ArrayIndexOutOfBoundsException ex) {
      throw new OutOfGameBoardException();
    }
  }

  public List<Tile> getRowTiles(int row) throws OutOfGameBoardException {
    try {
      return List.of(gameState[row]);
    } catch (ArrayIndexOutOfBoundsException ex) {
      throw new OutOfGameBoardException();
    }
  }
  public List<Tile> getColumnTiles(int column) throws OutOfGameBoardException {
    try {
      return List.of(Arrays.stream(gameState).map(x -> x[column]).toArray(Tile[]::new));
    } catch (ArrayIndexOutOfBoundsException ex) {
      throw new OutOfGameBoardException();
    }
  }

  public List<Tile> getTiles() {
    return Arrays.stream(gameState).flatMap(Arrays::stream).collect(Collectors.toList());
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

  private Tile[][] getGameState() {
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
  public boolean isThereAWall(Tile tile1, Tile tile2) throws NotAdjacentTilesException {
    for (StraightDirection direction : StraightDirection.values()) {
      try {
        if (tile2.equals(this.getAdjacentTile(tile1, direction))) {
          return tile1.getLink(direction) == WALL;
        }
      } catch (OutOfGameBoardException ignored) {
      }
    }
    throw new NotAdjacentTilesException();
  }

  public Tile getLandingTile(Tile tile, StraightDirection direction) throws OutOfGameBoardException {
    try {
      return switch (direction) {
        case UP -> gameState[tile.row - 2][tile.column];
        case DOWN -> gameState[tile.row + 2][tile.column];
        case RIGHT -> gameState[tile.row][tile.column + 2];
        case LEFT -> gameState[tile.row][tile.column - 2];
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
        default -> throw new InvalidParameterException();
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
    private Map<StraightDirection, LinkState> links;
    private boolean occupied;

    public Tile(int row, int column) {
      this.row = row;
      this.column = column;
      this.occupied = false;
      links = new EnumMap<>(Map.of(UP, FREE, RIGHT, FREE, DOWN, FREE, LEFT, FREE));
    }

    public void setLink(StraightDirection direction, LinkState linkState) {
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

    public LinkState getLink(StraightDirection direction) {
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

    public Map<StraightDirection, LinkState> getLinks() {
      return links;
    }
  }
}