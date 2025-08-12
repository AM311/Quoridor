package it.units.sdm.quoridor.GUI.view;

import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

public interface GameEventListener {
  void onWallPlaced(Position position, WallOrientation orientation, int playerIndex, int remainingWalls);
  void displayNotification(String message, boolean isError);
  void displayCommandsForCurrentPlayer();
  void highlightValidMoves();
  void onPawnMoved(Position oldPosition, Position newPosition, int playerIndex);
  void clearHighlights();
  void displayWallDirectionButtons(int playerIndex);
  void onGameFinished();
  void onRoundFinished(boolean showControls);
}