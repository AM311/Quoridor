package testDoubles;

import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.model.Tile;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

import java.util.Optional;

public class StubTile extends AbstractTile {

  public StubTile(Position position) {
    super(position);
  }

  @Override
  public Optional<AbstractPawn> isOccupiedBy() {
    return Optional.empty();
  }

  @Override
  public boolean isThereAWall(StraightDirection direction) {
    return false;
  }

  @Override
  public boolean isThereAWallOrEdge(StraightDirection direction) {
    return false;
  }

  @Override
  public void setOccupiedBy(AbstractPawn occupyingPawn) {
    // no-op per test
  }

  @Override
  public void setFree() {
    // no-op per test
  }

  @Override
  public Tile clone() throws CloneNotSupportedException {
    return (Tile) super.clone();
  }

}
