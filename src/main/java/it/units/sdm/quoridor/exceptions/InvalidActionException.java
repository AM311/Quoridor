package it.units.sdm.quoridor.exceptions;

public class InvalidActionException extends QuoridorException {
  public InvalidActionException() {
  }

  public InvalidActionException(String message) {
    super(message);
  }

  public InvalidActionException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidActionException(Throwable cause) {
    super(cause);
  }
}
