package it.units.sdm.quoridor.GUI.view;

import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

public interface GameEventListener {
  void onWallPlaced(int playerIndex, int remainingWalls);
  void onTurnComplete();
  void displayNotification(String message, boolean isError);
  void highlightValidMoves();
  void onPawnMoved(Position oldPosition, Position newPosition, int playerIndex);
  void updateWallVisualization(Position position, WallOrientation orientation);
  void clearHighlights();
  void displayWallDirectionButtons(int playerIndex);
  void showGameFinishedDialog();
}