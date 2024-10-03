package it.units.sdm.quoridor.exceptions;

public class OutOfGameBoardException extends Exception{
  public OutOfGameBoardException() {
  }

  public OutOfGameBoardException(String message) {
    super(message);
  }
}
