package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.model.Wall;

public class WallPlacementChecker implements ActionChecker<Wall> {
	public boolean checkAction(GameBoard gameBoard, Pawn playingPawn, Wall target) {
		return true;		//do something
	}
}
