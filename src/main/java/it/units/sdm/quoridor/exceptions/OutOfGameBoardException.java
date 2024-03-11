package it.units.sdm.quoridor.exceptions;

public class OutOfGameBoardException extends QuoridorException{
  public OutOfGameBoardException() {
  }

  public OutOfGameBoardException(String message) {
    super(message);
  }

  public OutOfGameBoardException(String message, Throwable cause) {
    super(message, cause);
  }

  public OutOfGameBoardException(Throwable cause) {
    super(cause);
  }
}
