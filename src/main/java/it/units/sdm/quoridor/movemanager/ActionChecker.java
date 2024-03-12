package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;

public interface ActionChecker<T> {
	boolean checkAction(GameBoard gameBoard, Pawn playingPawn, T target);
}
