import it.units.sdm.quoridor.model.GameBoard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.Direction.*;

public class GameBoardTest {
  GameBoard gameBoard = new GameBoard();

  @ParameterizedTest
  @CsvSource({"0, 4", "8, 4"})
  void gameStateInitialization_occupiedIsTrue(int row, int column) {
    Assertions.assertTrue(gameBoard.getGameState()[row][column].isOccupied());
  }

  @ParameterizedTest
  @CsvSource({"6, 7", "1, 4", "5, 7", "0, 0"})
  void gameStateInitialization_occupiedIsFalse(int row, int column) {
    Assertions.assertFalse(gameBoard.getGameState()[row][column].isOccupied());
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "2, 1", "5, 6", "7, 2"})
  void gameStateInitialization_rowsAreConsistent(int row, int column) {
    Assertions.assertEquals(row, gameBoard.getGameState()[row][column].getRow());
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "2, 1", "5, 6", "7, 2"})
  void gameStateInitialization_columnsAreConsistent(int row, int column) {
    Assertions.assertEquals(column, gameBoard.getGameState()[row][column].getColumn());
  }

  //---------------------

  @ParameterizedTest
  @CsvSource({"5, 1", "4, 6", "3, 1", "7, 6"})
  void gameStateInitialization_innerTilesLinksAreFree(int row, int column) {
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, FREE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row][column].getLink(LEFT),
                    gameBoard.getGameState()[row][column].getLink(RIGHT),
                    gameBoard.getGameState()[row][column].getLink(UP),
                    gameBoard.getGameState()[row][column].getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "4, 0"})
  void gameStateInitialization_leftEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row][column].getLink(LEFT),
                    gameBoard.getGameState()[row][column].getLink(RIGHT)
            };
    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"0, 8", "3, 8"})
  void gameStateInitialization_rightEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row][column].getLink(RIGHT),
                    gameBoard.getGameState()[row][column].getLink(LEFT)
            };
    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "0, 6"})
  void gameStateInitialization_upperEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE};

    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row][column].getLink(UP),
                    gameBoard.getGameState()[row][column].getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"8,8", "8, 2"})
  void gameStateInitialization_lowerEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row][column].getLink(DOWN),
                    gameBoard.getGameState()[row][column].getLink(UP)
            };
    Assertions.assertArrayEquals(expected, actual);
  }
}
