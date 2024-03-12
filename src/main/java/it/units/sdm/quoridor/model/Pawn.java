package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.model.GameBoard.Tile;

import java.awt.*;

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

  public int getNumberOfWalls() {
    return numberOfWalls;
  }
}
