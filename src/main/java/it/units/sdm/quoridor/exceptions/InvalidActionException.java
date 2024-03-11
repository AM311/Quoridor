package it.units.sdm.quoridor.exceptions;

public class InvalidActionException extends QuoridorException {
  public InvalidActionException() {
  }

  public InvalidActionException(String message) {
    super(message);
  }
}
