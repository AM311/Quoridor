package it.units.sdm.quoridor.model.builder;

import it.units.sdm.quoridor.model.AbstractGame;

public abstract class AbstractQuoridorBuilder {
  public final int gameBoardSideLength;
  public final int stdNumberOfWalls;
  public final int numberOfPlayers;

  public AbstractQuoridorBuilder(int gameBoardSideLength, int stdNumberOfWalls, int numberOfPlayers) {
    this.gameBoardSideLength = gameBoardSideLength;
    this.stdNumberOfWalls = stdNumberOfWalls;
    this.numberOfPlayers = numberOfPlayers;
  }

  abstract AbstractGame buildGame();

  abstract AbstractQuoridorBuilder setIGameBoard();

  abstract AbstractQuoridorBuilder setIPawnList();

  abstract AbstractQuoridorBuilder setActionManager();

  abstract AbstractQuoridorBuilder setPlaceWallActionController();

  abstract AbstractQuoridorBuilder setMovePawnActionController();
}