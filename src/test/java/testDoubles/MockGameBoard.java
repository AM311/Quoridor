package testDoubles;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGameBoard;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.TargetTiles;
import it.units.sdm.quoridor.utils.directions.Direction;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockGameBoard extends AbstractGameBoard {

  public MockGameBoard(int sideLength) throws InvalidParameterException {
    super(createEmptyState(sideLength));
  }

  private static AbstractTile[][] createEmptyState(int sideLength) {
    AbstractTile[][] state = new AbstractTile[sideLength][sideLength];

    for (int r = 0; r < sideLength; r++) {
      for (int c = 0; c < sideLength; c++) {
        state[r][c] = new StubTile(new Position(r, c));
      }
    }

    return state;
  }

  // Implementazione minima dei metodi astratti
  @Override
  public List<TargetTiles> getStartingAndDestinationTiles() {
    return new ArrayList<>();
  }

  @Override
  public AbstractTile getTile(Position position) {
    return gameState[position.row()][position.column()];
  }

  @Override
  public List<AbstractTile> getRowTiles(int row) {
    List<AbstractTile> rowTiles = new ArrayList<>();
    for (AbstractTile tile : gameState[row]) {
      rowTiles.add(tile);
    }
    return rowTiles;
  }

  @Override
  public List<AbstractTile> getColumnTiles(int column) {
    List<AbstractTile> colTiles = new ArrayList<>();
    for (int r = 0; r < sideLength; r++) {
      colTiles.add(gameState[r][column]);
    }
    return colTiles;
  }

  @Override
  public Collection<AbstractTile> getTiles() {
    List<AbstractTile> tiles = new ArrayList<>();
    for (AbstractTile[] row : gameState) {
      for (AbstractTile tile : row) {
        tiles.add(tile);
      }
    }
    return tiles;
  }

  @Override
  public boolean isThereAWall(AbstractTile tile1, AbstractTile tile2) {
    return false;
  }

  @Override
  public boolean isThereAWall(AbstractTile tile, StraightDirection direction) {
    return false;
  }

  @Override
  public boolean isThereAWallOrEdge(AbstractTile tile, StraightDirection direction) {
    return false;
  }

  @Override
  public AbstractTile getLandingTile(AbstractTile tile, StraightDirection direction) {
    return tile;
  }

  @Override
  public AbstractTile getAdjacentTile(AbstractTile tile, Direction direction) {
    return tile;
  }
}
