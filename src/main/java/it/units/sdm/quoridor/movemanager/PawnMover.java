package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.model.Pawn;

public class PawnMover implements Action<Tile> {
	PawnMovementChecker pawnMovementChecker = new PawnMovementChecker();

	@Override
	public void execute(GameBoard gameBoard, Pawn pawn, Tile target) {
		pawnMovementChecker.checkAction(gameBoard, pawn, target);
	}


}
