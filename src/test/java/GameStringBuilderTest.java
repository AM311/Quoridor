import it.units.sdm.quoridor.cli.GameStringBuilder;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import org.junit.jupiter.api.Test;
import testDoubles.StubGame;
import testDoubles.StubPawn;
import testDoubles.MockGameBoard;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameStringBuilderTest {

  @Test
  void createGameStringIsNotNull() throws InvalidParameterException {
    MockGameBoard board = new MockGameBoard(3);
    StubPawn pawn = new StubPawn("Pawn1");
    StubGame game = new StubGame(board, List.of(pawn));
    String result = GameStringBuilder.createGameString(game);

    assertNotNull(result, "La stringa non deve essere null");
  }


  @Test
  void createGameStringIsNotEmpty() throws InvalidParameterException {
    MockGameBoard board = new MockGameBoard(3);
    StubPawn pawn = new StubPawn("Pawn1");
    StubGame game = new StubGame(board, List.of(pawn));
    String result = GameStringBuilder.createGameString(game);
    assertFalse(result.isBlank(), "La stringa non deve essere vuota");
  }
}
