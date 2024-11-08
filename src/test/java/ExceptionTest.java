import it.units.sdm.quoridor.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
  void correctThrowQuoridorRuntimeException() {
    Assertions.assertThrows(QuoridorRuntimeException.class, () -> {
      throw new QuoridorRuntimeException();
    });

    Exception exception = Assertions.assertThrows(QuoridorRuntimeException.class, () -> {
      throw new QuoridorRuntimeException("Test");
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
  void correctThrowInvalidParameterException() {
    Assertions.assertThrows(InvalidParameterException.class, () -> {
      throw new InvalidParameterException();
    });

    Exception exception = Assertions.assertThrows(InvalidParameterException.class, () -> {
      throw new InvalidParameterException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }


  @Test
  void correctThrowNotAdjacentTilesException() {
    Assertions.assertThrows(NotAdjacentTilesException.class, () -> {
      throw new NotAdjacentTilesException();
    });

    Exception exception = Assertions.assertThrows(NotAdjacentTilesException.class, () -> {
      throw new NotAdjacentTilesException("Test");
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
  void correctThrowNumberOfWallsBelowZeroException() {
    Assertions.assertThrows(NumberOfWallsBelowZeroException.class, () -> {
      throw new NumberOfWallsBelowZeroException();
    });

    Exception exception = Assertions.assertThrows(NumberOfWallsBelowZeroException.class, () -> {
      throw new NumberOfWallsBelowZeroException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }

  @Test
  void correctBuilderException() {
    Assertions.assertThrows(BuilderException.class, () -> {
      throw new BuilderException();
    });

    Exception exception = Assertions.assertThrows(BuilderException.class, () -> {
      throw new BuilderException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }

  @Test
  void correctThrowParserException() {
    Assertions.assertThrows(ParserException.class, () -> {
      throw new ParserException();
    });

    Exception exception = Assertions.assertThrows(ParserException.class, () -> {
      throw new ParserException("Test");
    });

    Assertions.assertEquals("Test", exception.getMessage());
  }

}
