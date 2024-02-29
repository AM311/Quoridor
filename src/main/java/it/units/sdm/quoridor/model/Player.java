package it.units.sdm.quoridor.model;

public class Player {
  private final String name;
  private int numberOfWalls;
  private Pawn pawn;

  //todo aggiungere controlli su numero di muri

  public Player(String name, int numberOfWalls, Pawn pawn) {
    this.name = name;
    this.numberOfWalls = numberOfWalls;
    this.pawn = pawn;
  }

  public void movePawn(GameBoard gameBoard, GameBoard.Tile tile) {
    checkMovePawn(gameBoard, tile);
  }

  public boolean checkMovePawn(GameBoard gameBoard, GameBoard.Tile tile) {
    if (tile.getRow() < 0 || tile.getRow() > 8 || tile.getColumn() < 0 || tile.getColumn() > 8) {
      return false;
    }
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    if (Math.abs(currentRow + currentColumn - tile.getRow() - tile.getColumn()) != 1) {
      return false;
    }
    if (tile.isOccupied()) {
      return false;
    }
    return true;
  }

  public void useWall() {
    numberOfWalls--;
  }

  public String getName() {
    return name;
  }

  public int getNumberOfWalls() {
    return numberOfWalls;
  }

  public Pawn getPawn() {
    return pawn;
  }
}
