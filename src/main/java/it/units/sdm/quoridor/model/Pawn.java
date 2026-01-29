package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.exceptions.NumberOfWallsBelowZeroException;
import it.units.sdm.quoridor.model.abstracts.AbstractPawn;
import it.units.sdm.quoridor.model.abstracts.AbstractTile;
import it.units.sdm.quoridor.view.PawnAppearance;

import java.util.Collection;

public class Pawn extends AbstractPawn {

  public Pawn(AbstractTile startingTile, Collection<AbstractTile> destinationTiles, PawnAppearance pawnAppearance, int numberOfWalls) {
    super(startingTile, destinationTiles, pawnAppearance, numberOfWalls);
  }

  public void move(AbstractTile destinationTile) {
    this.currentTile = destinationTile;
  }

  public boolean hasReachedDestination() {
    return destinationTiles.contains(currentTile);
  }

  public void decrementNumberOfWalls() {
    if (this.numberOfWalls <= 0) {
      throw new NumberOfWallsBelowZeroException("Number of walls cannot be negative!");
    }
    this.numberOfWalls--;
  }
}
