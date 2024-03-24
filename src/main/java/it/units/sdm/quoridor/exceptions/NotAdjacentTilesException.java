package it.units.sdm.quoridor.exceptions;

public class NotAdjacentTilesException extends RuntimeException{
  public NotAdjacentTilesException() {
  }

  public NotAdjacentTilesException(String message) {
    super(message);
  }
}
