package it.units.sdm.quoridor.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public abstract class AbstractPawn implements Cloneable {
  protected final Collection<AbstractTile> destinationTiles;
  protected final PawnAppearance pawnAppearance;
  protected AbstractTile currentTile;
  protected int numberOfWalls;

  public AbstractPawn(AbstractTile startingTile, Collection<AbstractTile> destinationTiles, PawnAppearance pawnAppearance, int numberOfWalls) {
    this.currentTile = startingTile;
    this.destinationTiles = destinationTiles;
    this.pawnAppearance = pawnAppearance;
    this.numberOfWalls = Math.max(numberOfWalls, 0);
  }

  public AbstractTile getCurrentTile() {
    return currentTile;
  }

  public abstract void move(AbstractTile destinationTile);

  public Collection<AbstractTile> getDestinationTiles() {
    return Collections.unmodifiableCollection(destinationTiles);
  }

  public int getNumberOfWalls() {
    return numberOfWalls;
  }

  public abstract boolean hasReachedDestination();

  public abstract void decrementNumberOfWalls();

  public PawnAppearance getPawnAppearance() {
    return pawnAppearance;
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentTile, destinationTiles, pawnAppearance, numberOfWalls);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof AbstractPawn that))
      return false;
    return numberOfWalls == that.numberOfWalls && Objects.equals(currentTile, that.currentTile) && Objects.equals(destinationTiles, that.destinationTiles) && Objects.equals(pawnAppearance, that.pawnAppearance);
  }

  @Override
  public AbstractPawn clone() throws CloneNotSupportedException {
    AbstractPawn clonedPawn = (AbstractPawn) super.clone();
    clonedPawn.currentTile = currentTile.clone();
    return clonedPawn;
  }

  public String toString() {
    return pawnAppearance.toString();
  }
}
