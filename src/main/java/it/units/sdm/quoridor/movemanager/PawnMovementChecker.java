package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.model.Pawn;

/**
 * @author Alessio Manià - IN2000247
 */
public class PawnMovementChecker implements ActionChecker<Tile> {
	@Override
	public boolean checkAction(GameBoard gameBoard, Pawn playingPawn, Tile target) {
		return true;		//do something
	}
}
