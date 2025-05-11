package it.units.sdm.quoridor.GUI.controller;

import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.utils.Position;

import java.util.List;

public interface GameActionHandler {
  void handleTileClick(Position position);
  void setMoveAction();
  void setPlaceWallAction();
  void restartGame();
  int getNumberOfPlayers();
  int getPlayingPawnIndex();
  int getSideLength();
  List<AbstractPawn> getPawns();
  java.util.List<Position> getValidMovePositions();
  boolean isGameFinished();
}