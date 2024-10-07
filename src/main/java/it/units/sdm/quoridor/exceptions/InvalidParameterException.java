package it.units.sdm.quoridor.exceptions;

public class InvalidParameterException extends QuoridorException{
  public InvalidParameterException() {
  }

  public InvalidParameterException(String message) {
    super(message);
  }
}
