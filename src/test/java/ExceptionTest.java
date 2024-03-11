import it.units.sdm.quoridor.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExceptionTest {

  @Test
  void correctThrowQuoridorException() {
    Exception exception = Assertions.assertThrows(QuoridorException.class, () -> {
      throw new QuoridorException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }
  @Test
  void correctThrowInvalidActionException() {
    Exception exception = Assertions.assertThrows(InvalidActionException.class, () -> {
      throw new InvalidActionException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }

  @Test
  void correctThrowOutOfGameBoardException() {
    Exception exception = Assertions.assertThrows(OutOfGameBoardException.class, () -> {
      throw new OutOfGameBoardException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }

  @Test
  void correctThrowInvalidWallPlacementException() {
    Exception exception = Assertions.assertThrows(InvalidWallPlacementException.class, () -> {
      throw new InvalidWallPlacementException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }

  @Test
  void correctThrowInvalidPawnMovementException() {
    Exception exception = Assertions.assertThrows(InvalidPawnMovementException.class, () -> {
      throw new InvalidPawnMovementException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }
}
