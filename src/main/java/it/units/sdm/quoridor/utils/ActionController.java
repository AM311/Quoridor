package it.units.sdm.quoridor.utils;

import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.movemanagement.actions.Action;

public record ActionController<T>(Action<T> action, ActionChecker<T>... actionCheckers) {
}
