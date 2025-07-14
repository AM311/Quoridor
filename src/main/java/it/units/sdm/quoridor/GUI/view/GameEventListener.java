package it.units.sdm.quoridor.GUI.view;

import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

public interface GameEventListener {
  void onWallPlaced(Position position, WallOrientation orientation, int playerIndex, int remainingWalls);
  void displayNotification(String message, boolean isError);
  void highlightValidMoves();
  void onPawnMoved(Position oldPosition, Position newPosition, int playerIndex);
  void onInvalidAction(String message);
  void clearHighlights();
  void displayWallDirectionButtons(int playerIndex);
  void onGameFinished(StatisticsCounter statistics);
  void onRoundFinished();
}