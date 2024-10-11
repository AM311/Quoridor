package it.units.sdm.quoridor.movemanagement.actionmanagers;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.utils.ActionController;

@FunctionalInterface
public interface ActionManager {
  <T> void performAction(AbstractGame game, T target, ActionController<T> actionController, boolean useOrInsteadOfAnd) throws InvalidActionException;
}
