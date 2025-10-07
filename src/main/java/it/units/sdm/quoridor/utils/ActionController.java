package it.units.sdm.quoridor.utils;

import it.units.sdm.quoridor.model.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.model.movemanagement.actions.Action;

public record ActionController<T>(Action<T> action, ActionChecker<T>... actionCheckers) {
  @SafeVarargs
  public ActionController {
  }
}
