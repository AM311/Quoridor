package it.units.sdm.quoridor.model.movemanagement.actioncheckers;

import it.units.sdm.quoridor.model.AbstractGame;

@FunctionalInterface
public interface ActionChecker<T> {
  CheckResult isValidAction(AbstractGame game, T target);
}
