package it.units.sdm.quoridor.exceptions;

public class InvalidWallPlacementException extends InvalidActionException{
  public InvalidWallPlacementException() {
  }

  public InvalidWallPlacementException(String message) {
    super(message);
  }

  public InvalidWallPlacementException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidWallPlacementException(Throwable cause) {
    super(cause);
  }
}
