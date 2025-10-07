package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.NotAdjacentTilesException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.exceptions.QuoridorRuntimeException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.TargetTiles;
import it.units.sdm.quoridor.utils.directions.Direction;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static it.units.sdm.quoridor.utils.directions.DiagonalDirection.*;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class GameBoard extends AbstractGameBoard {
  public GameBoard(AbstractTile[][] gameState) throws InvalidParameterException {
    super(gameState);
  }

  @Override
  public List<TargetTiles> getStartingAndDestinationTiles() {
    try {
      List<AbstractTile> startingTiles = List.of(
              gameState[0][sideLength / 2],
              gameState[sideLength - 1][sideLength / 2],
              gameState[sideLength / 2][0],
              gameState[sideLength / 2][sideLength - 1]
      );

      List<List<AbstractTile>> destinationTiles = List.of(
              getRowTiles(sideLength - 1),
              getRowTiles(0),
              getColumnTiles(sideLength - 1),
              getColumnTiles(0)
      );
      return IntStream.range(0, 4).mapToObj(i -> new TargetTiles(startingTiles.get(i), destinationTiles.get(i))).toList();

    } catch (InvalidParameterException e) {
      throw new QuoridorRuntimeException("Exception when getting starting and destination tiles!");
    }
  }

  @Override
  public AbstractTile getTile(Position position) throws InvalidParameterException {
    try {
      return gameState[position.row()][position.column()];
    } catch (ArrayIndexOutOfBoundsException ex) {
      throw new InvalidParameterException("The provided coordinates are outside the GameBoard!");
    }
  }

  @Override
  public List<AbstractTile> getRowTiles(int row) throws InvalidParameterException {
    try {
      return List.of(gameState[row]);
    } catch (ArrayIndexOutOfBoundsException ex) {
      throw new InvalidParameterException("The provided row is outside GameBoard!");
    }
  }

  @Override
  public List<AbstractTile> getColumnTiles(int column) throws InvalidParameterException {
    try {
      return List.of(Arrays.stream(gameState).map(x -> x[column]).toArray(AbstractTile[]::new));
    } catch (ArrayIndexOutOfBoundsException ex) {
      throw new InvalidParameterException("The provided column is outside GameBoard!");
    }
  }

  @Override
  public Collection<AbstractTile> getTiles() {
    return Arrays.stream(gameState).flatMap(Arrays::stream).collect(Collectors.toSet());
  }

  @Override
  public boolean isThereAWall(AbstractTile tile1, AbstractTile tile2) throws NotAdjacentTilesException {
    for (StraightDirection direction : StraightDirection.values()) {
      try {
        if (tile2.equals(this.getAdjacentTile(tile1, direction))) {
          return tile1.isThereAWall(direction);
        }
      } catch (OutOfGameBoardException ignored) {
      }
    }
    throw new NotAdjacentTilesException();
  }

  @Override
  public boolean isThereAWall(AbstractTile tile, StraightDirection direction) {
    return tile.isThereAWall(direction);
  }

  @Override
  public boolean isThereAWallOrEdge(AbstractTile tile, StraightDirection direction) {
    return tile.isThereAWallOrEdge(direction);
  }

  @Override
  public AbstractTile getLandingTile(AbstractTile tile, StraightDirection direction) throws OutOfGameBoardException {
    try {
      return switch (direction) {
        case UP -> gameState[tile.getRow() - 2][tile.getColumn()];
        case DOWN -> gameState[tile.getRow() + 2][tile.getColumn()];
        case RIGHT -> gameState[tile.getRow()][tile.getColumn() + 2];
        case LEFT -> gameState[tile.getRow()][tile.getColumn() - 2];
      };
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new OutOfGameBoardException("Cannot jump from the provided tile!");
    }
  }

  @Override
  public AbstractTile getAdjacentTile(AbstractTile tile, Direction direction) throws OutOfGameBoardException {
    try {
      return switch (direction) {
        case UP -> gameState[tile.getRow() - 1][tile.getColumn()];
        case DOWN -> gameState[tile.getRow() + 1][tile.getColumn()];
        case RIGHT -> gameState[tile.getRow()][tile.getColumn() + 1];
        case LEFT -> gameState[tile.getRow()][tile.getColumn() - 1];
        case UP_LEFT -> gameState[tile.getRow() - 1][tile.getColumn() - 1];
        case UP_RIGHT -> gameState[tile.getRow() - 1][tile.getColumn() + 1];
        case DOWN_LEFT -> gameState[tile.getRow() + 1][tile.getColumn() - 1];
        case DOWN_RIGHT -> gameState[tile.getRow() + 1][tile.getColumn() + 1];
        default -> throw new QuoridorRuntimeException("Unhandled case while getting adjacent tile!");
      };
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new OutOfGameBoardException("The provided tile is on the edge!");
    }
  }

}