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

  public void movePawn(GameBoard gameBoard, GameBoard.Tile destinationTile) {
    checkMovePawn(gameBoard, destinationTile);
  }

  public boolean checkMovePawn(GameBoard gameBoard, GameBoard.Tile destinationTile) {
    if (!isAdjacencyMove(destinationTile)) {
      if (isDiagonalMove(destinationTile)) {
        return isSpecialMove(gameBoard, destinationTile);
      }
      return isJumpingPawnMove(gameBoard, destinationTile);
    }
    if (destinationTile.isOccupied()) {
      return false;
    }
    return !isThereAWall(gameBoard, destinationTile);
  }

  private boolean isAdjacencyMove(GameBoard.Tile destinationTile) {
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    int destinationRow = destinationTile.getRow();
    int destinationColumn = destinationTile.getColumn();
    boolean isUpOrDown = Math.abs(currentRow - destinationRow) == 1 && currentColumn == destinationColumn;
    boolean isLeftOrRight = Math.abs(currentColumn - destinationColumn) == 1 && currentRow == destinationRow;
    return isUpOrDown || isLeftOrRight;
  }

  private boolean isDiagonalMove(GameBoard.Tile destinationTile) {
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    int destinationRow = destinationTile.getRow();
    int destinationColumn = destinationTile.getColumn();
    boolean isUpOrDownRow = Math.abs(currentRow - destinationRow) == 1;
    boolean isLeftOrRightColumn = Math.abs(currentColumn - destinationColumn) == 1;
    return isUpOrDownRow && isLeftOrRightColumn;
  }

  private boolean isThereAWall(GameBoard gameBoard, GameBoard.Tile destinationTile) {
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    int destinationRow = destinationTile.getRow();
    int destinationColumn = destinationTile.getColumn();

    if (currentRow - destinationRow == 1 && currentColumn == destinationColumn) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(Direction.UP) == GameBoard.LinkState.WALL;
    }
    if (currentRow - destinationRow == -1 && currentColumn == destinationColumn) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(Direction.DOWN) == GameBoard.LinkState.WALL;
    }
    if (currentColumn - destinationColumn == 1 && currentRow == destinationRow) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(Direction.LEFT) == GameBoard.LinkState.WALL;
    }
    if (currentColumn - destinationColumn == -1 && currentRow == destinationRow) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(Direction.RIGHT) == GameBoard.LinkState.WALL;
    }
    return false;
  }

  private boolean isSpecialMove(GameBoard gameBoard, GameBoard.Tile destinationTile) {
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    int destinationRow = destinationTile.getRow();
    int destinationColumn = destinationTile.getColumn();

    if ((currentRow - destinationRow == 1 && currentColumn - destinationColumn == -1
      && gameBoard.getGameState()[currentRow][currentColumn + 1].isOccupied()
      && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(Direction.RIGHT) == GameBoard.LinkState.WALL
      && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(Direction.UP) != GameBoard.LinkState.WALL)
      || (currentRow - destinationRow == 1 && currentColumn - destinationColumn == -1
      && gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
      && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(Direction.UP) == GameBoard.LinkState.WALL
      && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(Direction.RIGHT) != GameBoard.LinkState.WALL)) {
      return true;
    }
    if ((currentRow - destinationRow == 1 && currentColumn - destinationColumn == 1
      && gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
      && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(Direction.UP) == GameBoard.LinkState.WALL
      && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(Direction.LEFT) != GameBoard.LinkState.WALL)
      || (currentRow - destinationRow == 1 && currentColumn - destinationColumn == 1
      && gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
      && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(Direction.LEFT) == GameBoard.LinkState.WALL
      && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(Direction.UP) != GameBoard.LinkState.WALL)) {
      return true;
    }
    if ((currentRow - destinationRow == -1 && currentColumn - destinationColumn == 1
      && gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
      && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(Direction.LEFT) == GameBoard.LinkState.WALL
      && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(Direction.DOWN) != GameBoard.LinkState.WALL)
      || (currentRow - destinationRow == -1 && currentColumn - destinationColumn == 1
      && gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
      && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(Direction.DOWN) == GameBoard.LinkState.WALL
      && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(Direction.LEFT) != GameBoard.LinkState.WALL)) {
      return true;
    }
    return (currentRow - destinationRow == -1 && currentColumn - destinationColumn == -1
      && gameBoard.getGameState()[currentRow][currentColumn + 1].isOccupied()
      && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(Direction.RIGHT) == GameBoard.LinkState.WALL
      && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(Direction.DOWN) != GameBoard.LinkState.WALL)
      || (currentRow - destinationRow == -1 && currentColumn - destinationColumn == 1
      && gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
      && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(Direction.DOWN) == GameBoard.LinkState.WALL
      && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(Direction.RIGHT) != GameBoard.LinkState.WALL);
  }

  private boolean isJumpingPawnMove(GameBoard gameBoard, GameBoard.Tile destinationTile) {
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    int destinationRow = destinationTile.getRow();
    int destinationColumn = destinationTile.getColumn();

    if (currentRow - destinationRow == 2 && currentColumn == destinationColumn) {
      return gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
        && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(Direction.DOWN) != GameBoard.LinkState.WALL;
    }
    if (currentRow - destinationRow == -2 && currentColumn == destinationColumn) {
      return gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
        && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(Direction.UP) != GameBoard.LinkState.WALL;
    }
    if (currentRow == destinationRow && currentColumn - destinationColumn == 2) {
      return gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
        && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(Direction.RIGHT) != GameBoard.LinkState.WALL;
    }
    if (currentRow == destinationRow && currentColumn - destinationColumn == -2) {
      return gameBoard.getGameState()[currentRow][currentColumn + 1].isOccupied()
        && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(Direction.LEFT) != GameBoard.LinkState.WALL;
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
