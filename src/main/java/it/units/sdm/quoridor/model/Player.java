package it.units.sdm.quoridor.model;
import it.units.sdm.quoridor.utils.Direction;


import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.utils.WallOrientation;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.Direction.*;

public class Player {
  private final String name;
  private int numberOfWalls;
  private final Pawn pawn;

  //todo add checks on walls number
  //todo add wall number decrease
  //todo add behavior in placeWall if checkWallPosition returns false
  //todo check if a wall blocks completely the opponent
  //todo aggiungere controlli su numero di muri

  public Player(String name, int numberOfWalls, Pawn pawn) {
    this.name = name;
    this.numberOfWalls = numberOfWalls;
    this.pawn = pawn;
  }

  public void movePawn(GameBoard gameBoard, GameBoard.Tile destinationTile) {
    checkMovePawn(gameBoard, destinationTile);
  }
  public void placeWall(GameBoard gameBoard, WallOrientation orientation, Tile startingTile) {
    if (checkWallPosition(gameBoard, orientation, startingTile)) {
      setWallLinks(gameBoard, orientation, startingTile);
    }
  }

  public boolean checkWallPosition(GameBoard gameBoard, WallOrientation orientation, Tile startingTile) {
    return switch (orientation) {
      case HORIZONTAL -> checkHorizontalWallPosition(gameBoard, startingTile);
      case VERTICAL -> checkVerticalWallPosition(gameBoard, startingTile);
    };
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
  private void setWallLinks(GameBoard gameBoard, WallOrientation orientation, Tile startingTile) {
    switch (orientation) {
      case HORIZONTAL -> setWallLinksForHorizontalWall(gameBoard, startingTile);
      case VERTICAL -> setWallLinkForVerticalWall(gameBoard, startingTile);
    }
  }

  private boolean checkHorizontalWallPosition(GameBoard gameBoard, Tile startingTile) {
    if (gameBoard.isInLastRow(startingTile) || gameBoard.isInLastColumn(startingTile)) {
      return false;
    }
    //todo extract method "checkCrossingWalls"?
    Tile tileBelowStartingTile = gameBoard.getLowerTile(startingTile);
    Tile tileRightToStartingTile = gameBoard.getRightTile(startingTile);

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
    Tile tileAboveStartingTile = gameBoard.getUpperTile(startingTile);
    Tile tileLeftToStartingTile = gameBoard.getLeftTile(startingTile);

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
    if (startingTile.getLink(UP) == WALL && tileLeftToStartingTile.getLink(UP) == WALL) {
      return false;
    }
    if (startingTile.getLink(LEFT) == WALL || tileAboveStartingTile.getLink(LEFT) == WALL) {
      return false;
    }
    return true;
  }

  private void setWallLinksForHorizontalWall(GameBoard gameBoard, Tile startingTile) {
    Tile tileBelowStartingTile = gameBoard.getLowerTile(startingTile);
    Tile tileRightToStartingTile = gameBoard.getRightTile(startingTile);
    Tile tileLowRightDiagToStartingTile = gameBoard.getLowerTile(gameBoard.getRightTile(startingTile));

    startingTile.setLink(DOWN, WALL);
    tileRightToStartingTile.setLink(DOWN, WALL);
    tileBelowStartingTile.setLink(UP, WALL);
    tileLowRightDiagToStartingTile.setLink(UP, WALL);
  }

  private void setWallLinkForVerticalWall(GameBoard gameBoard, Tile startingTile) {
    Tile tileAboveStartingTile = gameBoard.getUpperTile(startingTile);
    Tile tileLeftToStartingTile = gameBoard.getLeftTile(startingTile);
    Tile tileUpLeftDiagToStartingTile = gameBoard.getUpperTile(gameBoard.getLeftTile(startingTile));

    startingTile.setLink(LEFT, WALL);
    tileAboveStartingTile.setLink(LEFT, WALL);
    tileLeftToStartingTile.setLink(RIGHT, WALL);
    tileUpLeftDiagToStartingTile.setLink(RIGHT, WALL);
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
