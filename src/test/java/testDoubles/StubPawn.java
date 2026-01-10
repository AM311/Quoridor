package testDoubles;

import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.model.PawnAppearance;
import it.units.sdm.quoridor.utils.Color;

import java.util.Collection;
import java.util.Collections;

public class StubPawn extends AbstractPawn {

  private final String name;
  private final Color color = Color.RED;

  public StubPawn(String name) {
    super(null, Collections.emptyList(), new PawnAppearance(Color.RED), 10);
    this.name = name;
  }

  @Override
  public void move(AbstractTile destinationTile) {
    // no-op
  }

  @Override
  public boolean hasReachedDestination() {
    return false;
  }

  @Override
  public void decrementNumberOfWalls() {
    // no-op
  }

  @Override
  public String toString() {
    return name;
  }
}
