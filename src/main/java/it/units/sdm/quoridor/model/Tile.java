package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.model.abstracts.AbstractPawn;
import it.units.sdm.quoridor.model.abstracts.AbstractTile;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

import java.util.Optional;

import static it.units.sdm.quoridor.model.abstracts.AbstractTile.LinkState.*;

public class Tile extends AbstractTile {
  public Tile(Position position) {
    super(position);
  }

  @Override
  public Optional<AbstractPawn> isOccupiedBy() {
    return Optional.ofNullable(occupiedBy);
  }

  @Override
  public boolean isThereAWall(StraightDirection direction) {
    return links.get(direction) == WALL;
  }

  @Override
  public boolean isThereAWallOrEdge(StraightDirection direction) {
    return (links.get(direction) == WALL || links.get(direction) == EDGE);
  }

  @Override
  public void setOccupiedBy(AbstractPawn occupyingPawn) {
    this.occupiedBy = occupyingPawn;
  }

  @Override
  public void setFree() {
    this.occupiedBy = null;
  }

  @Override
  public void setLink(StraightDirection direction, LinkState linkState) {
    links.put(direction, linkState);
  }
}
