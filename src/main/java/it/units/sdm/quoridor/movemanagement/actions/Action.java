package it.units.sdm.quoridor.movemanagement.actions;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.model.AbstractGame;

@FunctionalInterface
public interface Action<T> {
	void execute(AbstractGame game, T target) throws InvalidActionException;
}
