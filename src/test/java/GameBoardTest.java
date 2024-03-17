import it.units.sdm.quoridor.model.GameBoard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.Directions.Direction.*;

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
  @CsvSource({"8, 8", "8, 2"})
  void gameStateInitialization_lowerEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row][column].getLink(DOWN),
                    gameBoard.getGameState()[row][column].getLink(UP)
            };
    Assertions.assertArrayEquals(expected, actual);
  }

  //---------------
  //todo finire test
  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inFirstRowIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(row == 0, gameBoard.isInFirstRow(gameBoard.getGameState()[row][column]));
  }

  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inLastRowIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(row == 8, gameBoard.isInLastRow(gameBoard.getGameState()[row][column]));
  }

  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inFirstColumnIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(column == 0, gameBoard.isInFirstColumn(gameBoard.getGameState()[row][column]));
  }

  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inLastColumnIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(column == 8, gameBoard.isInLastColumn(gameBoard.getGameState()[row][column]));
  }

  //-----------------
  //todo manage exceptions and not allowed cases
  @ParameterizedTest
  @CsvSource({"1, 4", "3, 8", "8, 2"})
  void nearTiles_leftTileIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(gameBoard.getGameState()[row][column - 1], gameBoard.getAdjacentTile(gameBoard.getGameState()[row][column], LEFT));
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "3, 7", "8, 2"})
  void nearTiles_rightTileIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(gameBoard.getGameState()[row][column + 1], gameBoard.getAdjacentTile(gameBoard.getGameState()[row][column], RIGHT));
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "3, 8", "8, 2"})
  void nearTiles_upperTileIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(gameBoard.getGameState()[row - 1][column], gameBoard.getAdjacentTile(gameBoard.getGameState()[row][column], UP));
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "3, 8", "7, 2"})
  void nearTiles_lowerTileIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(gameBoard.getGameState()[row + 1][column], gameBoard.getAdjacentTile(gameBoard.getGameState()[row][column], DOWN));
  }

  //=======================

  @Test
  void cloneTest_withoutWalls() throws CloneNotSupportedException {
    GameBoard gameBoard = new GameBoard();

    Assertions.assertEquals(gameBoard, gameBoard.clone());
  }

  @Test
  void cloneTest_withWalls() throws CloneNotSupportedException {
    GameBoard gameBoard = new GameBoard();
    gameBoard.getGameState()[2][3].setLink(UP, WALL);
    gameBoard.getGameState()[5][2].setLink(LEFT, WALL);
    gameBoard.getGameState()[0][0].setLink(DOWN, WALL);

    Assertions.assertEquals(gameBoard, gameBoard.clone());
  }

  @Test
  void cloneTest_objectsAreIndependent_clonedIsModified() throws CloneNotSupportedException {
    GameBoard gameBoard = new GameBoard();
    gameBoard.getGameState()[2][3].setLink(UP, WALL);
    gameBoard.getGameState()[5][2].setLink(LEFT, WALL);
    gameBoard.getGameState()[0][0].setLink(DOWN, WALL);

    GameBoard clonedGameBoard = (GameBoard) gameBoard.clone();

    clonedGameBoard.getGameState()[6][2].setLink(UP, WALL);
    clonedGameBoard.getGameState()[1][4].setLink(RIGHT, WALL);
    clonedGameBoard.getGameState()[5][2].setLink(LEFT, FREE);

    Assertions.assertNotEquals(gameBoard, clonedGameBoard);
  }

  @Test
  void cloneTest_objectsAreIndependent_originalIsModified() throws CloneNotSupportedException {
    GameBoard gameBoard = new GameBoard();
    gameBoard.getGameState()[2][3].setLink(UP, WALL);
    gameBoard.getGameState()[5][2].setLink(LEFT, WALL);
    gameBoard.getGameState()[0][0].setLink(DOWN, WALL);

    GameBoard clonedGameBoard = (GameBoard) gameBoard.clone();

    gameBoard.getGameState()[6][2].setLink(UP, WALL);
    gameBoard.getGameState()[1][4].setLink(RIGHT, WALL);
    gameBoard.getGameState()[5][2].setLink(LEFT, FREE);

    Assertions.assertNotEquals(gameBoard, clonedGameBoard);
  }
}
