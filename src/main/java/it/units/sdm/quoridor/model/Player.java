package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.model.GameBoard.Tile;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.Direction.*;

public class Player {
  private final String name;
  private final Pawn pawn;
  private int numberOfWalls;

  //todo add checks on walls number
  //todo add wall number decrease
  //todo add behavior in placeWall if checkWallPosition returns false
  //todo check if a wall blocks completely the opponent

  public Player(String name, int numberOfWalls, Pawn pawn) {
    this.name = name;
    this.numberOfWalls = numberOfWalls;
    this.pawn = pawn;
  }

  public void placeWall(GameBoard gameBoard, Wall wall) {
    if (checkWallPosition(gameBoard, wall)) {
      setWallLinks(gameBoard, wall);
    }
  }

  public boolean checkWallPosition(GameBoard gameBoard, Wall wall) {
    return switch (wall.orientation()) {
      case HORIZONTAL -> checkHorizontalWallPosition(gameBoard, wall.startingTile());
      case VERTICAL -> checkVerticalWallPosition(gameBoard, wall.startingTile());
    };
  }

  private void setWallLinks(GameBoard gameBoard, Wall wall) {
    switch (wall.orientation()) {
      case HORIZONTAL -> setWallLinksForHorizontalWall(gameBoard, wall.startingTile());
      case VERTICAL -> setWallLinkForVerticalWall(gameBoard, wall.startingTile());
    }
  }

  private boolean checkHorizontalWallPosition(GameBoard gameBoard, Tile startingTile) {
    if (gameBoard.isInLastRow(startingTile) || gameBoard.isInLastColumn(startingTile)) {
      return false;
    }
    //todo extract method "checkCrossingWalls"?
    Tile tileBelowStartingTile = gameBoard.getAdjacentTile(startingTile, DOWN);
    Tile tileRightToStartingTile = gameBoard.getAdjacentTile(startingTile, RIGHT);

    if (startingTile.getLink(RIGHT) == WALL && tileBelowStartingTile.getLink(RIGHT) == WALL) {
      return false;
    }
    if (startingTile.getLink(DOWN) == WALL || tileRightToStartingTile.getLink(DOWN) == WALL) {
      return false;
    }
    return true;
  }

  private boolean checkVerticalWallPosition(GameBoard gameBoard, Tile startingTile) {
    if (gameBoard.isInFirstRow(startingTile) || gameBoard.isInFirstColumn(startingTile)) {
      return false;
    }
    //todo extract method "checkCrossingWalls"?
    Tile tileAboveStartingTile = gameBoard.getAdjacentTile(startingTile, UP);
    Tile tileLeftToStartingTile = gameBoard.getAdjacentTile(startingTile, LEFT);

    if (startingTile.getLink(UP) == WALL && tileLeftToStartingTile.getLink(UP) == WALL) {
      return false;
    }
    if (startingTile.getLink(LEFT) == WALL || tileAboveStartingTile.getLink(LEFT) == WALL) {
      return false;
    }
    return true;
  }

  private void setWallLinksForHorizontalWall(GameBoard gameBoard, Tile startingTile) {
    Tile tileBelowStartingTile = gameBoard.getAdjacentTile(startingTile, DOWN);
    Tile tileRightToStartingTile = gameBoard.getAdjacentTile(startingTile, RIGHT);
    Tile tileLowRightDiagToStartingTile = gameBoard.getAdjacentTile(gameBoard.getAdjacentTile(startingTile, RIGHT), DOWN);
    startingTile.setLink(DOWN, WALL);
    tileRightToStartingTile.setLink(DOWN, WALL);
    tileBelowStartingTile.setLink(UP, WALL);
    tileLowRightDiagToStartingTile.setLink(UP, WALL);
  }

  private void setWallLinkForVerticalWall(GameBoard gameBoard, Tile startingTile) {
    Tile tileAboveStartingTile = gameBoard.getAdjacentTile(startingTile, UP);
    Tile tileLeftToStartingTile = gameBoard.getAdjacentTile(startingTile, LEFT);
    Tile tileUpLeftDiagToStartingTile = gameBoard.getAdjacentTile(gameBoard.getAdjacentTile(startingTile, LEFT), UP);

    startingTile.setLink(LEFT, WALL);
    tileAboveStartingTile.setLink(LEFT, WALL);
    tileLeftToStartingTile.setLink(RIGHT, WALL);
    tileUpLeftDiagToStartingTile.setLink(RIGHT, WALL);
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
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(UP) == WALL;
    }
    if (currentRow - destinationRow == -1 && currentColumn == destinationColumn) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(DOWN) == WALL;
    }
    if (currentColumn - destinationColumn == 1 && currentRow == destinationRow) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(LEFT) == WALL;
    }
    if (currentColumn - destinationColumn == -1 && currentRow == destinationRow) {
      return gameBoard.getGameState()[currentRow][currentColumn].getLink(RIGHT) == WALL;
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
            && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(RIGHT) == WALL
            && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(UP) != WALL)
            || (currentRow - destinationRow == 1 && currentColumn - destinationColumn == -1
            && gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
            && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(UP) == WALL
            && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(RIGHT) != WALL)) {
      return true;
    }
    if ((currentRow - destinationRow == 1 && currentColumn - destinationColumn == 1
            && gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
            && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(UP) == WALL
            && gameBoard.getGameState()[currentRow - 1][currentColumn].getLink(LEFT) != WALL)
            || (currentRow - destinationRow == 1 && currentColumn - destinationColumn == 1
            && gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
            && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(LEFT) == WALL
            && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(UP) != WALL)) {
      return true;
    }
    if ((currentRow - destinationRow == -1 && currentColumn - destinationColumn == 1
            && gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
            && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(LEFT) == WALL
            && gameBoard.getGameState()[currentRow][currentColumn - 1].getLink(DOWN) != WALL)
            || (currentRow - destinationRow == -1 && currentColumn - destinationColumn == 1
            && gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
            && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(DOWN) == WALL
            && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(LEFT) != WALL)) {
      return true;
    }
    return (currentRow - destinationRow == -1 && currentColumn - destinationColumn == -1
            && gameBoard.getGameState()[currentRow][currentColumn + 1].isOccupied()
            && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(RIGHT) == WALL
            && gameBoard.getGameState()[currentRow][currentColumn + 1].getLink(DOWN) != WALL)
            || (currentRow - destinationRow == -1 && currentColumn - destinationColumn == 1
            && gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
            && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(DOWN) == WALL
            && gameBoard.getGameState()[currentRow + 1][currentColumn].getLink(RIGHT) != WALL);
  }

  private boolean isJumpingPawnMove(GameBoard gameBoard, GameBoard.Tile destinationTile) {
    int currentRow = pawn.getCurrentTile().getRow();
    int currentColumn = pawn.getCurrentTile().getColumn();
    int destinationRow = destinationTile.getRow();
    int destinationColumn = destinationTile.getColumn();

    if (currentRow - destinationRow == 2 && currentColumn == destinationColumn) {
      return gameBoard.getGameState()[currentRow - 1][currentColumn].isOccupied()
              && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(DOWN) != WALL;
    }
    if (currentRow - destinationRow == -2 && currentColumn == destinationColumn) {
      return gameBoard.getGameState()[currentRow + 1][currentColumn].isOccupied()
              && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(UP) != WALL;
    }
    if (currentRow == destinationRow && currentColumn - destinationColumn == 2) {
      return gameBoard.getGameState()[currentRow][currentColumn - 1].isOccupied()
              && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(RIGHT) != WALL;
    }
    if (currentRow == destinationRow && currentColumn - destinationColumn == -2) {
      return gameBoard.getGameState()[currentRow][currentColumn + 1].isOccupied()
              && gameBoard.getGameState()[destinationRow][destinationColumn].getLink(LEFT) != WALL;
    }
    return false;
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