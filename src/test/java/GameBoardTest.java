import it.units.sdm.quoridor.model.GameBoard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]
        {
            GameBoard.LinkState.FREE,
            GameBoard.LinkState.FREE,
            GameBoard.LinkState.FREE,
            GameBoard.LinkState.FREE
        };

    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
        {
            gameBoard.getGameState()[row][column].getLeftLink(),
            gameBoard.getGameState()[row][column].getRightLink(),
            gameBoard.getGameState()[row][column].getUpperLink(),
            gameBoard.getGameState()[row][column].getLowerLink()
        };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "4, 0"})
  void gameStateInitialization_leftEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]
        {
            GameBoard.LinkState.EDGE,
            GameBoard.LinkState.FREE
        };

    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
        {
            gameBoard.getGameState()[row][column].getLeftLink(),
            gameBoard.getGameState()[row][column].getRightLink()
        };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"0, 8", "3, 8"})
  void gameStateInitialization_rightEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]
        {
            GameBoard.LinkState.EDGE,
            GameBoard.LinkState.FREE
        };

    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
        {
            gameBoard.getGameState()[row][column].getRightLink(),
            gameBoard.getGameState()[row][column].getLeftLink()
        };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "0, 6"})
  void gameStateInitialization_upperEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]
        {
            GameBoard.LinkState.EDGE,
            GameBoard.LinkState.FREE
        };

    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
        {
            gameBoard.getGameState()[row][column].getUpperLink(),
            gameBoard.getGameState()[row][column].getLowerLink()
        };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"8,8", "8, 2"})
  void gameStateInitialization_lowerEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]
        {
            GameBoard.LinkState.EDGE,
            GameBoard.LinkState.FREE
        };

    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
        {
            gameBoard.getGameState()[row][column].getLowerLink(),
            gameBoard.getGameState()[row][column].getUpperLink()
        };

    Assertions.assertArrayEquals(expected, actual);
  }
}
