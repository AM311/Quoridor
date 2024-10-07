package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.exceptions.NumberOfWallsBelowZeroException;
import it.units.sdm.quoridor.model.GameBoard.Tile;

import java.awt.*;
import java.util.List;

public class Pawn {
  private Tile currentTile;
  private final List<Tile> destinationTiles;
  private final Color color;            //todo valutare se estrarre
  private int numberOfWalls;

  public Pawn(Tile startingTile, List<Tile> destinationTiles, Color color, int numberOfWalls) {
    this.destinationTiles = destinationTiles;
    this.currentTile = startingTile;
    this.color = color;
    this.numberOfWalls = numberOfWalls;
  }

  public Tile getCurrentTile() {
    return currentTile;
  }

  public void move(Tile destinationTile) {
    this.currentTile = destinationTile;
  }

  public List<Tile> getDestinationTiles() {
    return destinationTiles;
  }

  public Color getColor() {
    return color;
  }

  public int getNumberOfWalls() {
    return numberOfWalls;
  }

  public void decrementNumberOfWalls(){
    if (this.numberOfWalls<=0){
      throw new NumberOfWallsBelowZeroException("Number of walls cannot be negative!");
    }
    this.numberOfWalls--;
  }
}
