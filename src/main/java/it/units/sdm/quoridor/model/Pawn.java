package it.units.sdm.quoridor.model;

import java.awt.*;
import it.units.sdm.quoridor.model.GameBoard.Tile;

public class Pawn {
  private Tile currentTile;
  private final Tile startingTile;
  private final Color color;

  public Pawn(Tile startingTile, Color color) {
    this.startingTile = startingTile;
    this.currentTile = startingTile;
    this.color = color;
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
}
