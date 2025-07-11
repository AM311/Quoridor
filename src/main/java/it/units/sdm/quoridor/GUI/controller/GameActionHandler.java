package it.units.sdm.quoridor.GUI.controller;

import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.utils.Position;

import java.util.List;

public interface GameActionHandler {
  void handleTileClick(Position position);
  void setMoveAction();
  void setPlaceWallAction();
  int getNumberOfPlayers();
  int getPlayingPawnIndex();
  int getSideLength();
  void changeRound();
  List<AbstractPawn> getPawns();
  boolean isGameFinished();
}