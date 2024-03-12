package it.units.sdm.quoridor.exceptions;

public class OutOfGameBoardException extends RuntimeException{
  public OutOfGameBoardException() {
  }

  public OutOfGameBoardException(String message) {
    super(message);
  }
}
