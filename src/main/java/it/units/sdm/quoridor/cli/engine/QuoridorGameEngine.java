package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.model.AbstractGame;

public interface QuoridorGameEngine {

  AbstractGame createGame();

  void startGame(AbstractGame game);

  void endGame();
}
