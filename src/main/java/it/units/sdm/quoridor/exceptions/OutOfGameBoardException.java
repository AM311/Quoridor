package it.units.sdm.quoridor.exceptions;

public class OutOfGameBoardException extends QuoridorException{
  public OutOfGameBoardException() {
  }

  public OutOfGameBoardException(String message) {
    super(message);
  }
}
