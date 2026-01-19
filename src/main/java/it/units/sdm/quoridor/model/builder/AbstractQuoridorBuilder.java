package it.units.sdm.quoridor.model.builder;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;

public abstract class AbstractQuoridorBuilder {
  public final int gameBoardSideLength;
  public final int stdNumberOfWalls;
  public final int numberOfPlayers;

  public AbstractQuoridorBuilder(int gameBoardSideLength, int stdNumberOfWalls, int numberOfPlayers) throws InvalidParameterException {
    if (gameBoardSideLength <= 0)
      throw new InvalidParameterException("GameBoard side length must be positive: " + gameBoardSideLength);

    if (stdNumberOfWalls < 0)
      throw new InvalidParameterException("Number of walls must be null or positive: " + stdNumberOfWalls);

    this.gameBoardSideLength = gameBoardSideLength;
    this.stdNumberOfWalls = stdNumberOfWalls;
    this.numberOfPlayers = numberOfPlayers;
  }

  abstract AbstractGame buildGame();

  abstract AbstractQuoridorBuilder setGameBoard();

  abstract AbstractQuoridorBuilder setPawnList();

  abstract AbstractQuoridorBuilder setActionManager();

  abstract AbstractQuoridorBuilder setPlaceWallActionController();

  abstract AbstractQuoridorBuilder setMovePawnActionController();
}