import it.units.sdm.quoridor.exceptions.*;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class ExceptionTest {

  @Test
  void correctThrowQuoridorException() {
    Assertions.assertThrows(QuoridorException.class, () -> {
      throw new QuoridorException();
    });

    Exception exception = Assertions.assertThrows(QuoridorException.class, () -> {
      throw new QuoridorException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }
  @Test
  void correctThrowInvalidActionException() {
    Assertions.assertThrows(InvalidActionException.class, () -> {
      throw new InvalidActionException();
    });

    Exception exception = Assertions.assertThrows(InvalidActionException.class, () -> {
      throw new InvalidActionException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }

  @Test
  void correctThrowOutOfGameBoardException() {
    Assertions.assertThrows(OutOfGameBoardException.class, () -> {
      throw new OutOfGameBoardException();
    });

    Exception exception = Assertions.assertThrows(OutOfGameBoardException.class, () -> {
      throw new OutOfGameBoardException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }

  @Test
  void correctThrowInvalidWallPlacementException() {
    Assertions.assertThrows(InvalidWallPlacementException.class, () -> {
      throw new InvalidWallPlacementException();
    });

    Exception exception = Assertions.assertThrows(InvalidWallPlacementException.class, () -> {
      throw new InvalidWallPlacementException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }

  @Test
  void correctThrowInvalidPawnMovementException() {
    Assertions.assertThrows(InvalidPawnMovementException.class, () -> {
      throw new InvalidPawnMovementException();
    });

    Exception exception = Assertions.assertThrows(InvalidPawnMovementException.class, () -> {
      throw new InvalidPawnMovementException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }

  @Test
  void correctThrowNumberOfWallsBelowZeroException() {
    GameBoard gameBoard = new GameBoard();
    GameBoard.Tile tile = gameBoard.getGameState()[0][4];
    Pawn pawn = new Pawn(tile, Color.black, 0);

    Assertions.assertThrows(NumberOfWallsBelowZeroException.class, pawn::decrementNumberOfWalls);

  }
}
