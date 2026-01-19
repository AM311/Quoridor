package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.model.abstracts.AbstractPawn;
import it.units.sdm.quoridor.model.abstracts.AbstractTile;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

import java.util.Optional;

import static it.units.sdm.quoridor.model.abstracts.AbstractTile.LinkState.FREE;
import static it.units.sdm.quoridor.model.abstracts.AbstractTile.LinkState.WALL;

public class Tile extends AbstractTile {

  public Tile(Position position) {
    super(position);
  }

  public boolean isThereAWall(StraightDirection direction) {
    return links.get(direction) == WALL;
  }

  public boolean isThereAWallOrEdge(StraightDirection direction) {
    return links.get(direction) != FREE;
  }

  public Optional<AbstractPawn> isOccupiedBy() {
    return Optional.ofNullable(occupiedBy);
  }

  public void setOccupiedBy(AbstractPawn occupyingPawn) {
    this.occupiedBy = occupyingPawn;
  }

  public void setFree() {
    this.occupiedBy = null;
  }
}
