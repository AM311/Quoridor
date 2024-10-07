package it.units.sdm.quoridor.exceptions;

public class QuoridorRuntimeException extends RuntimeException{
  public QuoridorRuntimeException() {
  }

  public QuoridorRuntimeException(String message) {
    super(message);
  }
}
