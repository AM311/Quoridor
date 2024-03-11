package it.units.sdm.quoridor.exceptions;

public class QuoridorException extends Exception{
  public QuoridorException() {
  }

  public QuoridorException(String message) {
    super(message);
  }

  public QuoridorException(String message, Throwable cause) {
    super(message, cause);
  }

  public QuoridorException(Throwable cause) {
    super(cause);
  }
}
