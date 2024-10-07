package it.units.sdm.quoridor.exceptions;

public class NotAdjacentTilesException extends QuoridorException{
  public NotAdjacentTilesException() {
  }

  public NotAdjacentTilesException(String message) {
    super(message);
  }
}
