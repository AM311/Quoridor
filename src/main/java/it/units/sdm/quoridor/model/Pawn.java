package it.units.sdm.quoridor.model;

import java.awt.*;

import it.units.sdm.quoridor.model.GameBoard.Tile;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.Direction.*;

public class Pawn {
  //todo add checks on walls number
  //todo add wall number decrease
  //todo add behavior in placeWall if checkWallPosition returns false
  //todo check if a wall blocks completely the opponent
  private Tile currentTile;
  private final Tile startingTile;
  private final Color color;
  private int numberOfWalls;      //todo da gestire

  public Pawn(Tile startingTile, Color color, int numberOfWalls) {
    this.startingTile = startingTile;
    this.currentTile = startingTile;
    this.color = color;
    this.numberOfWalls = numberOfWalls;
  }

  public Tile getCurrentTile() {
    return currentTile;
  }

  public void setCurrentTile(Tile currentTile) {
    this.currentTile = currentTile;
  }

  public Tile getStartingTile() {
    return startingTile;
  }

  public Color getColor() {
    return color;
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

  public int getNumberOfWalls() {
    return numberOfWalls;
  }
}
