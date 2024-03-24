package it.units.sdm.quoridor.exceptions;

public class InvalidParameterException extends RuntimeException{
  public InvalidParameterException() {
  }

  public InvalidParameterException(String message) {
    super(message);
  }
}
