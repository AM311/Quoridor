package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.NotAdjacentTilesException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.TargetTiles;
import it.units.sdm.quoridor.utils.directions.Direction;
import it.units.sdm.quoridor.utils.directions.StraightDirection;
import static it.units.sdm.quoridor.view.cli.GameBoardStringBuilder.createGameBoardString;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class AbstractGameBoard implements Cloneable {
  protected AbstractTile[][] gameState;
  protected final int sideLength;

  public AbstractGameBoard(AbstractTile[][] gameState) throws InvalidParameterException{
    this.gameState = gameState;

    for (AbstractTile[] abstractTiles : gameState) {
      if (gameState.length != abstractTiles.length) {
        throw new InvalidParameterException("The gameState has to be a square matrix!");
      }
    }

    this.sideLength = gameState.length;
  }
  public int getSideLength() {
    return sideLength;
  }
  public AbstractTile[][] getGameState() {
    return Arrays.copyOf(gameState, sideLength);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof GameBoard gameBoard))
      return false;
    return Arrays.deepEquals(gameState, gameBoard.gameState);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(gameState);
  }

  @Override
  public final GameBoard clone() throws CloneNotSupportedException {
    GameBoard clonedGameBoard = (GameBoard) super.clone();
    AbstractTile[][] clonedGameState = new AbstractTile[sideLength][sideLength];
    for (int i = 0; i < sideLength; i++)
      for (int j = 0; j < sideLength; j++)
        clonedGameState[i][j] = gameState[i][j].clone();

    clonedGameBoard.gameState = clonedGameState;

    return clonedGameBoard;
  }

  public abstract List<TargetTiles> getStartingAndDestinationTiles();

  public abstract AbstractTile getTile(Position position) throws InvalidParameterException;

  public abstract List<AbstractTile> getRowTiles(int row) throws InvalidParameterException;

  public abstract List<AbstractTile> getColumnTiles(int column) throws InvalidParameterException;

  public abstract Collection<AbstractTile> getTiles();

  public abstract boolean isThereAWall(AbstractTile tile1, AbstractTile tile2) throws NotAdjacentTilesException;

  public abstract boolean isThereAWall(AbstractTile tile, StraightDirection direction);

  public abstract boolean isThereAWallOrEdge(AbstractTile tile, StraightDirection direction);

  public abstract AbstractTile getLandingTile(AbstractTile tile, StraightDirection direction) throws OutOfGameBoardException;

  public abstract AbstractTile getAdjacentTile(AbstractTile tile, Direction direction) throws OutOfGameBoardException;

  public String toString(){
    return createGameBoardString(this);
  }
}

