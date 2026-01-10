import it.units.sdm.quoridor.cli.GameBoardStringBuilder;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testDoubles.MockGameBoard;

public class GameBoardStringBuilderTest {

  @Test
  void gameBoardStringIsNotNull() throws InvalidParameterException {
    MockGameBoard board = new MockGameBoard(3);
    String gameBoardString = GameBoardStringBuilder.createGameBoardString(board);
    Assertions.assertNotNull(gameBoardString);
  }

  @Test
  void gameBoardStringIsNotEmpty() throws InvalidParameterException {
    MockGameBoard board = new MockGameBoard(3);
    String gameBoardString = GameBoardStringBuilder.createGameBoardString(board);
    Assertions.assertFalse(gameBoardString.isEmpty());

  }
}
