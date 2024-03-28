import it.units.sdm.quoridor.exceptions.NotAdjacentTilesException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.utils.directions.StraightDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class GameBoardTest {
  @ParameterizedTest
  @CsvSource({"8,1", "8,6", "0,4", "0,7", "4,8", "2,8", "3,0", "8,0"})
  void gameStateInitialization_tilesAreSetNotOccupied(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertFalse(gameBoard.getTile(row, column).isOccupied());
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "2, 1", "5, 6", "7, 2"})
  void gameStateInitialization_rowsAreConsistent(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(row, gameBoard.getTile(row, column).getRow());
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "2, 1", "5, 6", "7, 2"})
  void gameStateInitialization_columnsAreConsistent(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(column, gameBoard.getTile(row, column).getColumn());
  }

  //=======================

  @ParameterizedTest
  @ValueSource(ints = {1,3,5})
  void getRowTest_tilesAreConsistent(int row) {
    GameBoard gameBoard = new GameBoard();

    List<Tile> expected = new ArrayList<>();
    for(int i = 0; i < gameBoard.getSideLength(); i++)
      expected.add(gameBoard.getTile(row,i));

    Assertions.assertEquals(expected, gameBoard.getRowTiles(row));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1,9,99})
  void getRowTest_OutOfGameBoardExceptionIsThrown(int row) {
    GameBoard gameBoard = new GameBoard();

    Assertions.assertThrows(OutOfGameBoardException.class, () -> gameBoard.getRowTiles(row));
  }

  @ParameterizedTest
  @ValueSource(ints = {1,3,5})
  void getColumnTest_tilesAreConsistent(int column) {
    GameBoard gameBoard = new GameBoard();

    List<Tile> expected = new ArrayList<>();
    for(int i = 0; i < gameBoard.getSideLength(); i++)
      expected.add(gameBoard.getTile(i, column));

    Assertions.assertEquals(expected, gameBoard.getColumnTiles(column));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1,9,99})
  void getColumnTest_OutOfGameBoardExceptionIsThrown(int column) {
    GameBoard gameBoard = new GameBoard();

    Assertions.assertThrows(OutOfGameBoardException.class, () -> gameBoard.getColumnTiles(column));
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"0,0,4", "1,8,4", "2,4,0", "3,4,8"})
  void getStartingAndDestinationTiles_startingTilesAreCorrect(int pawnIndex, int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(gameBoard.getTile(row, column), gameBoard.getStartingAndDestinationTiles().get(pawnIndex).getKey());
  }

  @ParameterizedTest
  @CsvSource({"0,8,1", "0,8,6", "1,0,4", "1,0,7", "2,4,8", "2,2,8", "3,3,0", "3,8,0"})
  void getStartingAndDestinationTiles_destinationTilesContainsCorrectTiles(int pawnIndex, int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertTrue(gameBoard.getStartingAndDestinationTiles().get(pawnIndex).getValue().contains(gameBoard.getTile(row, column)));
  }

  @ParameterizedTest
  @CsvSource({"1,8,1", "2,8,6", "0,0,4", "3,0,7", "1,4,8", "3,2,8", "0,3,0", "2,8,0"})
  void getStartingAndDestinationTiles_destinationTilesNotContainsWrongTiles(int pawnIndex, int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertFalse(gameBoard.getStartingAndDestinationTiles().get(pawnIndex).getValue().contains(gameBoard.getTile(row, column)));
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"5, 1", "4, 6", "3, 1", "7, 6"})
  void gameStateInitialization_innerTilesLinksAreFree(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, FREE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row, column).getLink(LEFT),
                    gameBoard.getTile(row, column).getLink(RIGHT),
                    gameBoard.getTile(row, column).getLink(UP),
                    gameBoard.getTile(row, column).getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "4, 0"})
  void gameStateInitialization_leftEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row, column).getLink(LEFT),
                    gameBoard.getTile(row, column).getLink(RIGHT)
            };
    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"0, 8", "3, 8"})
  void gameStateInitialization_rightEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row, column).getLink(RIGHT),
                    gameBoard.getTile(row, column).getLink(LEFT)
            };
    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "0, 6"})
  void gameStateInitialization_upperEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE};

    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row, column).getLink(UP),
                    gameBoard.getTile(row, column).getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"8, 8", "8, 2"})
  void gameStateInitialization_lowerEdgeTilesLinksAreCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row, column).getLink(DOWN),
                    gameBoard.getTile(row, column).getLink(UP)
            };
    Assertions.assertArrayEquals(expected, actual);
  }

  //=======================
  //todo finire test
  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inFirstRowIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(row == 0, gameBoard.isInFirstRow(gameBoard.getTile(row, column)));
  }

  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inLastRowIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(row == 8, gameBoard.isInLastRow(gameBoard.getTile(row, column)));
  }

  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inFirstColumnIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(column == 0, gameBoard.isInFirstColumn(gameBoard.getTile(row, column)));
  }

  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inLastColumnIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(column == 8, gameBoard.isInLastColumn(gameBoard.getTile(row, column)));
  }

  //=======================
  //todo manage exceptions and not allowed cases
  @ParameterizedTest
  @CsvSource({"1, 4", "3, 8", "8, 2"})
  void nearTiles_leftTileIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(gameBoard.getTile(row, column-1), gameBoard.getAdjacentTile(gameBoard.getTile(row, column), LEFT));
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "3, 7", "8, 2"})
  void nearTiles_rightTileIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(gameBoard.getTile(row, column+1), gameBoard.getAdjacentTile(gameBoard.getTile(row, column), RIGHT));
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "3, 8", "8, 2"})
  void nearTiles_upperTileIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(gameBoard.getTile(row-1, column), gameBoard.getAdjacentTile(gameBoard.getTile(row, column), UP));
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "3, 8", "7, 2"})
  void nearTiles_lowerTileIsCorrect(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertEquals(gameBoard.getTile(row+1, column), gameBoard.getAdjacentTile(gameBoard.getTile(row, column), DOWN));
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"3,4", "7,6", "1,2"})
  void isThereAWallTest_wallIsPresent(int startingTileRow, int startingTileColumn) {
    GameBoard gameBoard = new GameBoard();
    for (int i = startingTileColumn; i < startingTileColumn + 1; i++) {
      gameBoard.getTile(startingTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(startingTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertTrue(gameBoard.isThereAWall(gameBoard.getTile(startingTileRow, startingTileColumn), gameBoard.getTile(startingTileRow + 1, startingTileColumn)));
  }

  @ParameterizedTest
  @CsvSource({"3,4", "7,6", "1,2"})
  void isThereAWallTest_wallIsAbsent(int startingTileRow, int startingTileColumn) {
    GameBoard gameBoard = new GameBoard();
    for (int i = startingTileColumn-2; i < startingTileColumn; i++) {
      gameBoard.getTile(startingTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(startingTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertFalse(gameBoard.isThereAWall(gameBoard.getTile(startingTileRow, startingTileColumn), gameBoard.getTile(startingTileRow + 1, startingTileColumn)));
  }

  @ParameterizedTest
  @CsvSource({"3,4", "7,6", "1,2"})
  void isThereAWallTest_tilesAreNotAdjacent(int startingTileRow, int startingTileColumn) {
    GameBoard gameBoard = new GameBoard();
    for (int i = startingTileColumn; i < startingTileColumn+2; i++) {
      gameBoard.getTile(startingTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(startingTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertThrows(NotAdjacentTilesException.class, () -> gameBoard.isThereAWall(gameBoard.getTile(startingTileRow, startingTileColumn), gameBoard.getTile(startingTileRow + 1, startingTileColumn+1)));
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"2,5", "1,4", "7,7"})
  void isThereAWallOrEdgeTest_wallIsPresent(int startingTileRow, int startingTileColumn) {
    GameBoard gameBoard = new GameBoard();
    for (int i = startingTileColumn; i < startingTileColumn + 1; i++) {
      gameBoard.getTile(startingTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(startingTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertTrue(gameBoard.isThereAWallOrEdge(gameBoard.getTile(startingTileRow, startingTileColumn), DOWN));
  }

  @ParameterizedTest
  @CsvSource({"1,2", "5,2", "4,3"})
  void isThereAWallOrEdgeTest_wallIsAbsent(int startingTileRow, int startingTileColumn) {
    GameBoard gameBoard = new GameBoard();
    for (int i = startingTileColumn-2; i < startingTileColumn; i++) {
      gameBoard.getTile(startingTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(startingTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertFalse(gameBoard.isThereAWallOrEdge(gameBoard.getTile(startingTileRow, startingTileColumn), DOWN));
  }

  @ParameterizedTest
  @CsvSource({"0,2,UP", "8,0,LEFT", "8,0,DOWN"})
  void isThereAWallOrEdgeTest_edgeIsPresent(int startingTileRow, int startingTileColumn, StraightDirection direction) {
    GameBoard gameBoard = new GameBoard();

    Assertions.assertTrue(gameBoard.isThereAWallOrEdge(gameBoard.getTile(startingTileRow, startingTileColumn), direction));
  }
  @ParameterizedTest
  @CsvSource({"3,2,UP", "8,0,UP", "5,5,DOWN"})
  void isThereAWallOrEdgeTest_edgeIsAbsent(int startingTileRow, int startingTileColumn, StraightDirection direction) {
    GameBoard gameBoard = new GameBoard();

    Assertions.assertFalse(gameBoard.isThereAWallOrEdge(gameBoard.getTile(startingTileRow, startingTileColumn), direction));
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"9,0", "9,9", "0,15", "99,99"})
  void getTileTest_OutOfGameBoardExceptionIsThrown(int row, int column) {
    GameBoard gameBoard = new GameBoard();
    Assertions.assertThrows(OutOfGameBoardException.class, () -> gameBoard.getTile(row, column));
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
    gameBoard.getTile(2,3).setLink(UP, WALL);
    gameBoard.getTile(5,2).setLink(LEFT, WALL);
    gameBoard.getTile(0,0).setLink(DOWN, WALL);

    Assertions.assertEquals(gameBoard, gameBoard.clone());
  }

  @Test
  void cloneTest_objectsAreIndependent_clonedIsModified() throws CloneNotSupportedException {
    GameBoard gameBoard = new GameBoard();
    gameBoard.getTile(2,3).setLink(UP, WALL);
    gameBoard.getTile(5,2).setLink(LEFT, WALL);
    gameBoard.getTile(0,0).setLink(DOWN, WALL);

    GameBoard clonedGameBoard = gameBoard.clone();

    clonedGameBoard.getTile(6,2).setLink(UP, WALL);
    clonedGameBoard.getTile(1,4).setLink(RIGHT, WALL);
    clonedGameBoard.getTile(5,2).setLink(LEFT, FREE);

    Assertions.assertNotEquals(gameBoard, clonedGameBoard);
  }

  @Test
  void cloneTest_objectsAreIndependent_originalIsModified() throws CloneNotSupportedException {
    GameBoard gameBoard = new GameBoard();
    gameBoard.getTile(2,3).setLink(UP, WALL);
    gameBoard.getTile(5,2).setLink(LEFT, WALL);
    gameBoard.getTile(0,0).setLink(DOWN, WALL);

    GameBoard clonedGameBoard = gameBoard.clone();

    gameBoard.getTile(6,2).setLink(UP, WALL);
    gameBoard.getTile(1,4).setLink(RIGHT, WALL);
    gameBoard.getTile(5,2).setLink(LEFT, FREE);

    Assertions.assertNotEquals(gameBoard, clonedGameBoard);
  }
}
