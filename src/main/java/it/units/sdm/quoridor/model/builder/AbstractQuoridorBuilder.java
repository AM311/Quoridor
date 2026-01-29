package it.units.sdm.quoridor.model.builder;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;

public abstract class AbstractQuoridorBuilder {
  protected final int gameBoardSideLength;
  protected final int defaultNumberOfWalls;
  protected final int numberOfPlayers;

  public AbstractQuoridorBuilder(int gameBoardSideLength, int defaultNumberOfWalls, int numberOfPlayers) throws InvalidParameterException {
    if (gameBoardSideLength <= 0)
      throw new InvalidParameterException("GameBoard side length must be positive: " + gameBoardSideLength);

    if (defaultNumberOfWalls < 0)
      throw new InvalidParameterException("Number of walls must be null or positive: " + defaultNumberOfWalls);

    this.gameBoardSideLength = gameBoardSideLength;
    this.defaultNumberOfWalls = defaultNumberOfWalls;
    this.numberOfPlayers = numberOfPlayers;
  }

  abstract AbstractGame buildGame();

  abstract AbstractQuoridorBuilder setGameBoard();

  abstract AbstractQuoridorBuilder setPawnList();

  abstract AbstractQuoridorBuilder setActionManager();

  abstract AbstractQuoridorBuilder setPlaceWallActionController();

  abstract AbstractQuoridorBuilder setMovePawnActionController();
}