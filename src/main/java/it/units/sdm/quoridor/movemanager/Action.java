package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;

public interface Action<T> {
	void execute(GameBoard gameBoard, Pawn pawn, T target);
}
