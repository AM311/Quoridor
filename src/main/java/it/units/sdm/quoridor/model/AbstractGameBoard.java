package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.NotAdjacentTilesException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.TargetTiles;
import it.units.sdm.quoridor.utils.directions.Direction;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

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
    String gameBoardString = "";

    // Clean screen
    gameBoardString += "\n".repeat(20);
  
    int sideLength = gameBoard.getSideLength();
    AbstractTile[][] gameState = gameBoard.getGameState();
  
    // upper numbers
    gameBoardString += "      ";
    for (int i = 0; i < sideLength; i++) {
      gameBoardString += i + "     ";
    }
    gameBoardString += "\n";
  
    // upper edge
    gameBoardString += "   *";
    for (int i = 0; i < sideLength; i++) {
      gameBoardString += "-----*";
    }
    gameBoardString += "\n";
  
    // row by row
    for (int row = 0; row < sideLength; row++) {
      gameBoardString += row + "  |";
      for (int col = 0; col < sideLength; col++) {
        AbstractTile currentTile = gameState[row][col];
        String content = currentTile.isOccupiedBy().isPresent() ? currentTile.isOccupiedBy().get().toString() : "     ";
        gameBoardString += content;
        gameBoardString += currentTile.isThereAWallOrEdge(RIGHT) ? "|" : " ";
      }
      gameBoardString += "\n";
  
      // row of horizontal walls, except the last one
      if (row < sideLength - 1) {
        gameBoardString += "   *";
        for (int col = 0; col < sideLength - 1; col++) {
          gameBoardString += gameState[row][col].isThereAWallOrEdge(DOWN) ? "-----" : "     ";
          gameBoardString += "+";
        }
        gameBoardString += gameState[row][sideLength - 1].isThereAWallOrEdge(DOWN) ? "-----" : "     ";
        gameBoardString += "*\n";
      }
    }
  
    // lower edge
    gameBoardString += "   *";
    for (int i = 0; i < sideLength - 1; i++) {
      gameBoardString += "-----*";
    }
    gameBoardString += "-----*\n";
  
    return gameBoardString;
  }
}

