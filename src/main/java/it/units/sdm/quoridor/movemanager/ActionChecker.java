package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.model.Game;

public interface ActionChecker<T> {
	boolean checkAction(Game game, T target);
}
