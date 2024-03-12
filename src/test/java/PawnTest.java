import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;

public class PawnTest {
  GameBoard gameBoard = new GameBoard();
  GameBoard.Tile tile = gameBoard.getGameState()[8][4];

  @ParameterizedTest
  @CsvSource({"10", "5", "3"})
  void decrementNumberOfWallsIsConsistent(int numberOfWalls) {
    Pawn pawn1 = new Pawn(tile, Color.black, numberOfWalls);
    pawn1.decrementNumberOfWalls();
    int expected = numberOfWalls - 1;
    int actual = pawn1.getNumberOfWalls();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void getColorIsConsistent(){
    Pawn pawn1 = new Pawn(tile, Color.black, 10);
    Assertions.assertEquals(Color.black, pawn1.getColor());
  }
}
