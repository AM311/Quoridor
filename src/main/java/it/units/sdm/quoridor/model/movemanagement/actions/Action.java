package it.units.sdm.quoridor.model.movemanagement.actions;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;

@FunctionalInterface
public interface Action<T> {
  void execute(AbstractGame game, T target) throws InvalidActionException;
}
