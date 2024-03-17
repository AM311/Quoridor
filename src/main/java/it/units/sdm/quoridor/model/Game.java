package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.movemanager.*;

import java.util.List;

public class Game {
  private final List<Pawn> pawns;
  private final GameBoard gameBoard;
  private final GameActionManager actionManager;
  private Pawn playingPawn;

  public Game(List<Pawn> pawns, GameBoard gameBoard) {
    this.pawns = pawns;
    this.gameBoard = gameBoard;
    this.actionManager = new GameActionManager(this);
    this.playingPawn = pawns.getFirst();
  }

  public List<Pawn> getPawns() {
    return pawns;
  }

  public GameBoard getGameBoard() {
    return gameBoard;
  }

  public Pawn getPlayingPawn() {
    return playingPawn;
  }

  public void placeWall(Wall wall) {
    actionManager.performAction(new WallPlacer(), new WallPlacementChecker(), wall);
  }

  public void movePlayingPawn(Tile destinationTile) {
    actionManager.performAction(new PawnMover(), new PawnMovementChecker(), destinationTile);
  }

  public void changeRound() {
    try {
      playingPawn = pawns.get(pawns.indexOf(playingPawn) + 1);
    } catch (IndexOutOfBoundsException ex) {
      playingPawn = pawns.getFirst();
    }
  }
}