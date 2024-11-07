package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static it.units.sdm.quoridor.model.AbstractTile.LinkState.FREE;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public abstract class AbstractTile implements Cloneable {
  protected final Position position;
  protected Map<StraightDirection, Tile.LinkState> links;
  protected AbstractPawn occupiedBy;

  public AbstractTile(Position position) {
    this.position = position;
    this.occupiedBy = null;
    links = new EnumMap<>(Map.of(UP, FREE, RIGHT, FREE, DOWN, FREE, LEFT, FREE));
  }

  @Override
  public int hashCode() {
    return Objects.hash(position, links);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Tile tile))
      return false;
    return Objects.equals(position, tile.position) && Objects.equals(isOccupiedBy(), tile.isOccupiedBy()) && Objects.equals(links, tile.links);
  }

  @Override
  public Tile clone() throws CloneNotSupportedException {
    Tile clonedTile = (Tile) super.clone();
    clonedTile.links = new EnumMap<>(Map.of(UP, this.getLink(UP), RIGHT, this.getLink(RIGHT), DOWN, this.getLink(DOWN), LEFT, this.getLink(LEFT)));

    return clonedTile;
  }

  public LinkState getLink(StraightDirection direction) {
    return links.get(direction);
  }

  public abstract Optional<AbstractPawn> isOccupiedBy();

  public abstract boolean isThereAWall(StraightDirection direction);

  public abstract boolean isThereAWallOrEdge(StraightDirection direction);

  public abstract void setOccupiedBy(AbstractPawn occupyingPawn);

  public abstract void setFree();

  public void setLink(StraightDirection direction, LinkState linkState) {
    links.put(direction, linkState);
  }

  public int getRow() {
    return position.row();
  }

  public int getColumn() {
    return position.column();
  }

  public enum LinkState {
    FREE, WALL, EDGE
  }
}
