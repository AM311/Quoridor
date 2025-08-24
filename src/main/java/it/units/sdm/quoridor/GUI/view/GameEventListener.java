package it.units.sdm.quoridor.GUI.view;

import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

public interface GameEventListener {
  void onWallPlaced(Position position, WallOrientation orientation, int playerIndex, int remainingWalls);

  void displayNotification(String message, boolean isError);

  void displayQuitRestartDialog();

  void displayCommandsForCurrentPlayer();

  void highlightValidMoves();

  void disposeMainFrame();

  void onPawnMoved(Position oldPosition, Position newPosition, int playerIndex);

  void clearHighlights();

  void displayWallDirectionButtons(int playerIndex);

  void displayStatistics();             //todo GESTIRE --> separare

  void onRoundFinished(boolean showControls);
}