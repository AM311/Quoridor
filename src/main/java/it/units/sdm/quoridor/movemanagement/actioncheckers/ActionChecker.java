package it.units.sdm.quoridor.movemanagement.actioncheckers;

import it.units.sdm.quoridor.model.AbstractGame;

@FunctionalInterface
public interface ActionChecker<T> {
	CheckResult isValidAction(AbstractGame game, T target); //fai tornare enum + signature metodi che la implrmentani
}
