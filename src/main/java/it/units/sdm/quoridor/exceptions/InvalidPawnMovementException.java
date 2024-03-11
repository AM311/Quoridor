package it.units.sdm.quoridor.exceptions;

public class InvalidPawnMovementException extends InvalidActionException {

  public InvalidPawnMovementException() {
  }

  public InvalidPawnMovementException(String message) {
    super(message);
  }

  public InvalidPawnMovementException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidPawnMovementException(Throwable cause) {
    super(cause);
  }
}
