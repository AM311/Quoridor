package it.units.sdm.quoridor.model;

import java.awt.*;

import it.units.sdm.quoridor.exceptions.NumberOfWallsBelowZeroException;
import it.units.sdm.quoridor.model.GameBoard.Tile;

import java.awt.*;

public class Pawn {
  private Tile currentTile;
  private final Tile startingTile;
  private final Color color;            //todo valutare se estrarre
  private int numberOfWalls;

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

  public void decrementNumberOfWalls(){
    if (this.numberOfWalls<=0){
      throw new NumberOfWallsBelowZeroException();
    }
    this.numberOfWalls--;
  }

}
