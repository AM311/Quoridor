package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

public interface GameEventListener {

  void onWallPlaced(int playerIndex, int remainingWalls);

  void onInvalidWallPlacement(Position position, WallOrientation orientation);

  void onInvalidMove(Position position);

  void onTurnComplete();

  void onGameFinished(int winnerIndex);
}