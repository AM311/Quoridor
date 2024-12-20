package it.units.sdm.quoridor.movemanagement.actioncheckers;

import it.units.sdm.quoridor.model.AbstractGame;

@FunctionalInterface
public interface ActionChecker<T> {
	boolean isValidAction(AbstractGame game, T target);
}
