package it.units.sdm.quoridor.exceptions;

public class InvalidPawnMovementException extends InvalidActionException {

  public InvalidPawnMovementException() {
  }

  public InvalidPawnMovementException(String message) {
    super(message);
  }
}
