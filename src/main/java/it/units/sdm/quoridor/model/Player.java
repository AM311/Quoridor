package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.utils.Direction;

public class Player {
  private final String name;
  private int numberOfWalls;
  private final Pawn pawn;

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
    if (checkNotAdjacency(tile)) {
      if (moveIsDiagonal(tile)) {
        return checkSpecialMove(gameBoard, tile);
      }
      return checkClimbingPawn(gameBoard, tile);
    }
    if (tile.isOccupied()) {
      return false;
    }
    return !checkIfThereIsAWall(gameBoard, tile);
  }

  private boolean checkSpecialMove(GameBoard gameBoard, GameBoard.Tile tile) {
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    int destinationRow = tile.getRow();
    int destinationColumn = tile.getColumn();
    if ((currentRow - destinationRow == 1 && currentColumn - destinationColumn == -1
      && gameBoard.getGameState()[currentRow][currentColumn + 1].isOccupied()
      && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(Direction.RIGHT).equals(GameBoard.LinkState.WALL)
      && !gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(Direction.UP).equals(GameBoard.LinkState.WALL))
      || (currentRow - destinationRow == 1 && currentColumn - destinationColumn == -1
      && gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
      && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(Direction.UP).equals(GameBoard.LinkState.WALL)
      && !gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(Direction.RIGHT).equals(GameBoard.LinkState.WALL))) {
      return true;
    }
    if ((currentRow - destinationRow == 1 && currentColumn - destinationColumn == 1
      && gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
      && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(Direction.UP).equals(GameBoard.LinkState.WALL)
      && !gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(Direction.LEFT).equals(GameBoard.LinkState.WALL))
      || (currentRow - destinationRow == 1 && currentColumn - destinationColumn == 1
      && gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
      && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(Direction.LEFT).equals(GameBoard.LinkState.WALL)
      && !gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(Direction.UP).equals(GameBoard.LinkState.WALL))) {
      return true;
    }
    if ((currentRow - destinationRow == -1 && currentColumn - destinationColumn == 1
      && gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
      && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(Direction.LEFT).equals(GameBoard.LinkState.WALL)
      && !gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(Direction.DOWN).equals(GameBoard.LinkState.WALL))
      || (currentRow - destinationRow == -1 && currentColumn - destinationColumn == 1
      && gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
      && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(Direction.DOWN).equals(GameBoard.LinkState.WALL)
      && !gameBoard.getGameState()[currentRow +1][currentColumn].getLink(Direction.LEFT).equals(GameBoard.LinkState.WALL))) {
      return true;
    }
    return (currentRow - destinationRow == -1 && currentColumn - destinationColumn == -1
      && gameBoard.getGameState()[currentRow][currentColumn + 1].isOccupied()
      && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(Direction.RIGHT).equals(GameBoard.LinkState.WALL)
      && !gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(Direction.DOWN).equals(GameBoard.LinkState.WALL))
      || (currentRow - destinationRow == -1 && currentColumn - destinationColumn == 1
      && gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
      && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(Direction.DOWN).equals(GameBoard.LinkState.WALL)
      && !gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(Direction.RIGHT).equals(GameBoard.LinkState.WALL));
  }

  private boolean moveIsDiagonal(GameBoard.Tile tile) {
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    int destinationRow = tile.getRow();
    int destinationColumn = tile.getColumn();
    return Math.abs(currentRow - destinationRow) == 1 && Math.abs(currentColumn - destinationColumn) == 1;
  }

  private boolean checkNotAdjacency(GameBoard.Tile tile) {
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    int destinationRow = tile.getRow();
    int destinationColumn = tile.getColumn();
    return (Math.abs(currentRow - destinationRow) != 1 || currentColumn != destinationColumn)
      && (Math.abs(currentColumn - destinationColumn) != 1 || currentRow != destinationRow);
  }

  private boolean checkClimbingPawn(GameBoard gameBoard, GameBoard.Tile tile) {
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    int destinationRow = tile.getRow();
    int destinationColumn = tile.getColumn();

    if (currentRow - destinationRow == 2 && currentColumn - destinationColumn == 0) {
      return gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
        && !gameBoard.getGameState()[destinationRow][destinationColumn].getLink(Direction.DOWN).equals(GameBoard.LinkState.WALL);
    }
    if (currentRow - destinationRow == -2 && currentColumn - destinationColumn == 0) {
      return gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
        && !gameBoard.getGameState()[destinationRow][destinationColumn].getLink(Direction.UP).equals(GameBoard.LinkState.WALL);
    }
    if (currentRow - destinationRow == 0 && currentColumn - destinationColumn == 2) {
      return gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
        && !gameBoard.getGameState()[destinationRow][destinationColumn].getLink(Direction.RIGHT).equals(GameBoard.LinkState.WALL);
    }
    if (currentRow - destinationRow == 0 && currentColumn - destinationColumn == -2) {
      return gameBoard.getGameState()[currentRow][currentColumn + 1].isOccupied()
        && !gameBoard.getGameState()[destinationRow][destinationColumn].getLink(Direction.LEFT).equals(GameBoard.LinkState.WALL);
    }
    return false;
  }

  private boolean checkIfThereIsAWall(GameBoard gameBoard, GameBoard.Tile tile) {
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    int destinationRow = tile.getRow();
    int destinationColumn = tile.getColumn();
    if (currentRow - destinationRow == 1 && currentColumn - destinationColumn == 0) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(Direction.UP).equals(GameBoard.LinkState.WALL);
    }
    if (currentRow - destinationRow == -1 && currentColumn - destinationColumn == 0) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(Direction.DOWN).equals(GameBoard.LinkState.WALL);
    }
    if (currentRow - destinationRow == 0 && currentColumn - destinationColumn == 1) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(Direction.LEFT).equals(GameBoard.LinkState.WALL);
    }
    if (currentRow - destinationRow == 0 && currentColumn - destinationColumn == -1) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(Direction.RIGHT).equals(GameBoard.LinkState.WALL);
    }
    return false;
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
