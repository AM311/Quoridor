package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.utils.Direction;

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
      gameState[0][i].setLink(LinkState.EDGE, Direction.UP);
      gameState[i][0].setLink(LinkState.EDGE, Direction.LEFT);
      gameState[sideLength - 1][i].setLink(LinkState.EDGE, Direction.DOWN);
      gameState[i][sideLength - 1].setLink(LinkState.EDGE, Direction.RIGHT);
    }
  }

  private static boolean isStartingBox(int row, int column) {
    return (row == 0 && column == sideLength / 2)
            || (row == sideLength - 1 && column == sideLength / 2);
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
    private LinkState leftLink;
    private LinkState rightLink;
    private LinkState upperLink;
    private LinkState lowerLink;

    public Tile(int row, int column, boolean occupied) {
      this.row = row;
      this.column = column;
      this.occupied = occupied;
      this.leftLink = LinkState.FREE;
      this.rightLink = LinkState.FREE;
      this.upperLink = LinkState.FREE;
      this.lowerLink = LinkState.FREE;
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
      return switch (direction) {
        case UP -> upperLink;
        case DOWN -> lowerLink;
        case LEFT -> leftLink;
        case RIGHT -> rightLink;
      };
    }

    public void setLink(LinkState link, Direction direction) {
      switch (direction) {
        case UP -> this.upperLink = link;
        case DOWN -> this.lowerLink = link;
        case LEFT -> this.leftLink = link;
        case RIGHT -> this.rightLink = link;
      }
    }
  }
}