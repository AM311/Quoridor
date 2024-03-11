package it.units.sdm.quoridor.exceptions;

public class InvalidWallPlacementException extends InvalidActionException{
  public InvalidWallPlacementException() {
  }

  public InvalidWallPlacementException(String message) {
    super(message);
  }
}
