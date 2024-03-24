package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.model.Pawn;

public class PawnMover implements Action<Tile> {
  @Override
  public void execute(GameBoard gameBoard, Pawn pawn, Tile target) {
    Tile startingTile = pawn.getCurrentTile();
    pawn.move(target);
    gameBoard.getTile(target.getRow(), target.getColumn()).setOccupied(true);
    gameBoard.getTile(startingTile.getRow(), startingTile.getColumn()).setOccupied(false);
  }
}
