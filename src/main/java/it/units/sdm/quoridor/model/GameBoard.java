package it.units.sdm.quoridor.model;

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
      gameState[0][i].setUpperLink(LinkState.EDGE);
      gameState[i][0].setLeftLink(LinkState.EDGE);
      gameState[sideLength - 1][i].setLowerLink(LinkState.EDGE);
      gameState[i][sideLength - 1].setRightLink(LinkState.EDGE);

    }
  }

  private static boolean isStartingBox(int row, int column) {
    return (row == 0 && column == sideLength / 2) || (row == sideLength - 1 && column == sideLength / 2);
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

    public LinkState getLeftLink() {
      return leftLink;
    }

    public void setLeftLink(LinkState leftLink) {
      this.leftLink = leftLink;
    }

    public LinkState getRightLink() {
      return rightLink;
    }

    public void setRightLink(LinkState rightLink) {
      this.rightLink = rightLink;
    }

    public LinkState getUpperLink() {
      return upperLink;
    }

    public void setUpperLink(LinkState upperLink) {
      this.upperLink = upperLink;
    }

    public LinkState getLowerLink() {
      return lowerLink;
    }

    public void setLowerLink(LinkState lowerLink) {
      this.lowerLink = lowerLink;
    }
  }
}