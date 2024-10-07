package it.units.sdm.quoridor.exceptions;

public class NumberOfWallsBelowZeroException extends QuoridorRuntimeException{
  public NumberOfWallsBelowZeroException() {
  }
  public NumberOfWallsBelowZeroException(String message) {
    super(message);
  }
}
