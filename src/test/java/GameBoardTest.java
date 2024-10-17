import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.NotAdjacentTilesException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.directions.StraightDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class GameBoardTest {
  BuilderDirector builderDirector;
  /*
  @ParameterizedTest
  @CsvSource({"8,1", "8,6", "0,4", "0,7", "4,8", "2,8", "3,0", "8,0"})
  void gameStateInitialization_tilesAreSetNotOccupied(int row, int column) throws InvalidParameterException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();;
    Assertions.assertFalse(gameBoard.getTile(row, column).isOccupied());
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "2, 1", "5, 6", "7, 2"})
  void gameStateInitialization_rowsAreConsistent(int row, int column) throws InvalidParameterException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();;
    Assertions.assertEquals(row, gameBoard.getTile(row, column).getRow());
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "2, 1", "5, 6", "7, 2"})
  void gameStateInitialization_columnsAreConsistent(int row, int column) throws InvalidParameterException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();;
    Assertions.assertEquals(column, gameBoard.getTile(row, column).getColumn());
  }
*/
  //=======================

  @ParameterizedTest
  @ValueSource(ints = {1, 3, 5})
  void getRowTiles_tilesAreConsistent(int row) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    List<AbstractTile> expected = new ArrayList<>();
    for (int i = 0; i < gameBoard.getSideLength(); i++) {
      Position position = new Position(row, i);
      expected.add(gameBoard.getTile(position));
    }
    Assertions.assertEquals(expected, gameBoard.getRowTiles(row));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 9, 99})
  void getRowTiles_InvalidParameterExceptionIsThrown(int row) throws InvalidParameterException, BuilderException{
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Assertions.assertThrows(InvalidParameterException.class, () -> gameBoard.getRowTiles(row));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 3, 5})
  void getColumnTiles_tilesAreConsistent(int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    List<AbstractTile> expected = new ArrayList<>();
    for (int i = 0; i < gameBoard.getSideLength(); i++) {
      Position position = new Position(i, column);
      expected.add(gameBoard.getTile(position));
    }
    Assertions.assertEquals(expected, gameBoard.getColumnTiles(column));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 9, 99})
  void getColumnTiles_InvalidParameterExceptionIsThrown(int column) throws BuilderException, InvalidParameterException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Assertions.assertThrows(InvalidParameterException.class, () -> gameBoard.getColumnTiles(column));
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"0,9", "-1,6", "10,22", "11,2"})
  void getTile_coordinatesOutsideGameBoardThrowException(int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position position = new Position(row, column);
    Assertions.assertThrows(InvalidParameterException.class, () -> gameBoard.getTile(position));
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"0,0,4", "1,8,4", "2,4,0", "3,4,8"})
  void getStartingAndDestinationTiles_startingTilesAreCorrect(int pawnIndex, int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(4));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position position = new Position(row, column);
    Assertions.assertEquals(gameBoard.getTile(position),
            gameBoard.getStartingAndDestinationTiles().get(pawnIndex).startingTile());
  }

  @ParameterizedTest
  @CsvSource({"0,8,1", "0,8,6", "1,0,4", "1,0,7", "2,4,8", "2,2,8", "3,3,0", "3,8,0"})
  void getStartingAndDestinationTiles_destinationTilesContainsCorrectTiles(int pawnIndex, int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(4));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position position = new Position(row, column);
    Assertions.assertTrue(gameBoard.getStartingAndDestinationTiles().get(pawnIndex).destinationTiles().contains(gameBoard.getTile(position)));
  }

  @ParameterizedTest
  @CsvSource({"1,8,1", "2,8,6", "0,0,4", "3,0,7", "1,4,8", "3,2,8", "0,3,0", "2,8,0"})
  void getStartingAndDestinationTiles_destinationTilesDoNotContainWrongTiles(int pawnIndex, int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(4));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position position = new Position(row, column);
    Assertions.assertFalse(gameBoard.getStartingAndDestinationTiles().get(pawnIndex).destinationTiles().contains(gameBoard.getTile(position)));
  }

  //=======================
/*
  @ParameterizedTest
  @CsvSource({"5, 1", "4, 6", "3, 1", "7, 6"})
  void gameStateInitialization_innerTilesLinksAreFree(int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();;
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
  void gameStateInitialization_leftEdgeTilesLinksAreCorrect(int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();;
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
  void gameStateInitialization_rightEdgeTilesLinksAreCorrect(int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();;
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
  void gameStateInitialization_upperEdgeTilesLinksAreCorrect(int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();;
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
  void gameStateInitialization_lowerEdgeTilesLinksAreCorrect(int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();;
    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row, column).getLink(DOWN),
                    gameBoard.getTile(row, column).getLink(UP)
            };
    Assertions.assertArrayEquals(expected, actual);
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inFirstRowIsCorrect(int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();;
    Assertions.assertEquals(row == 0, gameBoard.isInFirstRow(gameBoard.getTile(row, column)));
  }

  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inLastRowIsCorrect(int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();;
    Assertions.assertEquals(row == 8, gameBoard.isInLastRow(gameBoard.getTile(row, column)));
  }

  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inFirstColumnIsCorrect(int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    Assertions.assertEquals(column == 0, gameBoard.isInFirstColumn(gameBoard.getTile(row, column)));
  }

  @ParameterizedTest
  @CsvSource({"0, 4", "0, 8", "2, 0", "3, 1", "8, 0", "8, 8"})
  void tilesPosition_inLastColumnIsCorrect(int row, int column) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();;
    Assertions.assertEquals(column == 8, gameBoard.isInLastColumn(gameBoard.getTile(row, column)));
  }

  //=======================
  //todo manage exceptions and not allowed cases
*/

  @ParameterizedTest
  @CsvSource({"1, 4", "3, 8", "8, 2"})
  void getAdjacentTile_leftTileIsCorrect(int row, int column) throws OutOfGameBoardException, InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile testTile = gameBoard.getTile(new Position(row, column));
    AbstractTile leftTile = gameBoard.getTile(new Position(row, column - 1));
    Assertions.assertEquals(leftTile, gameBoard.getAdjacentTile(testTile, LEFT));
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "3, 7", "8, 2"})
  void getAdjacentTile_rightTileIsCorrect(int row, int column) throws OutOfGameBoardException, InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile testTile = gameBoard.getTile(new Position(row, column));
    AbstractTile rightTile = gameBoard.getTile(new Position(row, column + 1));
    Assertions.assertEquals(rightTile, gameBoard.getAdjacentTile(testTile, RIGHT));
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "3, 8", "8, 2"})
  void getAdjacentTile_upperTileIsCorrect(int row, int column) throws OutOfGameBoardException, InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile testTile = gameBoard.getTile(new Position(row, column));
    AbstractTile upperTile = gameBoard.getTile(new Position(row - 1, column));
    Assertions.assertEquals(upperTile, gameBoard.getAdjacentTile(testTile, UP));
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "3, 8", "7, 2"})
  void getAdjacentTile_lowerTileIsCorrect(int row, int column) throws OutOfGameBoardException, InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile testTile = gameBoard.getTile(new Position(row, column));
    AbstractTile lowerTile = gameBoard.getTile(new Position(row + 1, column));
    Assertions.assertEquals(lowerTile, gameBoard.getAdjacentTile(testTile, DOWN));
  }

  //=======================

  //TODO IMPORTANTE!!! QUI ANDREBBE USATO IL METODO PLACEWALL?! <<<<<<<<<<<<<<<<<<<<<<

  @ParameterizedTest
  @CsvSource({"3,4", "7,6", "1,2"})
  void isThereAWall_wallIsPresentBetweenTwoTiles(int testTileRow, int testTileColumn) throws InvalidParameterException, NotAdjacentTilesException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    for (int i = testTileColumn; i < testTileColumn + 1; i++) {
      gameBoard.getTile(testTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(testTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertTrue(gameBoard.isThereAWall(gameBoard.getTile(testTileRow, testTileColumn), gameBoard.getTile(testTileRow + 1, testTileColumn)));
  }

  @ParameterizedTest
  @CsvSource({"3,4", "7,6", "1,2"})
  void isThereAWallTest_wallIsAbsent(int startingTileRow, int startingTileColumn) throws InvalidParameterException, NotAdjacentTilesException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    for (int i = startingTileColumn - 2; i < startingTileColumn; i++) {
      gameBoard.getTile(startingTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(startingTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertFalse(gameBoard.isThereAWall(gameBoard.getTile(startingTileRow, startingTileColumn), gameBoard.getTile(startingTileRow + 1, startingTileColumn)));
  }

  @ParameterizedTest
  @CsvSource({"3,4", "7,6", "1,2"})
  void isThereAWallTest_tilesAreNotAdjacent(int startingTileRow, int startingTileColumn) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    for (int i = startingTileColumn; i < startingTileColumn + 2; i++) {
      gameBoard.getTile(startingTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(startingTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertThrows(NotAdjacentTilesException.class, () -> gameBoard.isThereAWall(gameBoard.getTile(startingTileRow, startingTileColumn), gameBoard.getTile(startingTileRow + 1, startingTileColumn + 1)));
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"6,3", "2,3", "4,4"})
  void isThereAWallTest2_wallIsPresent(int startingTileRow, int startingTileColumn) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    for (int i = startingTileColumn; i < startingTileColumn + 1; i++) {
      gameBoard.getTile(startingTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(startingTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertTrue(gameBoard.isThereAWall(gameBoard.getTile(startingTileRow, startingTileColumn), DOWN));
  }

  @ParameterizedTest
  @CsvSource({"2,5", "5,2", "3,4"})
  void isThereAWallTest2_wallIsAbsent(int startingTileRow, int startingTileColumn) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    for (int i = startingTileColumn - 2; i < startingTileColumn; i++) {
      gameBoard.getTile(startingTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(startingTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertFalse(gameBoard.isThereAWall(gameBoard.getTile(startingTileRow, startingTileColumn), DOWN));
  }

  @ParameterizedTest
  @CsvSource({"2,5", "1,4", "7,7"})
  void isThereAWallOrEdgeTest_wallIsPresent(int startingTileRow, int startingTileColumn) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    for (int i = startingTileColumn; i < startingTileColumn + 1; i++) {
      gameBoard.getTile(startingTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(startingTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertTrue(gameBoard.isThereAWallOrEdge(gameBoard.getTile(startingTileRow, startingTileColumn), DOWN));
  }

  @ParameterizedTest
  @CsvSource({"1,2", "5,2", "4,3"})
  void isThereAWallOrEdgeTest_wallIsAbsent(int startingTileRow, int startingTileColumn) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    for (int i = startingTileColumn - 2; i < startingTileColumn; i++) {
      gameBoard.getTile(startingTileRow, i).setLink(DOWN, WALL);
      gameBoard.getTile(startingTileRow + 1, i).setLink(UP, WALL);
    }

    Assertions.assertFalse(gameBoard.isThereAWallOrEdge(gameBoard.getTile(startingTileRow, startingTileColumn), DOWN));
  }

  @ParameterizedTest
  @CsvSource({"0,2,UP", "8,0,LEFT", "8,0,DOWN"})
  void isThereAWallOrEdgeTest_edgeIsPresent(int startingTileRow, int startingTileColumn, StraightDirection direction) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Assertions.assertTrue(gameBoard.isThereAWallOrEdge(gameBoard.getTile(startingTileRow, startingTileColumn), direction));
  }

  @ParameterizedTest
  @CsvSource({"3,2,UP", "8,0,UP", "5,5,DOWN"})
  void isThereAWallOrEdgeTest_edgeIsAbsent(int startingTileRow, int startingTileColumn, StraightDirection direction) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Assertions.assertFalse(gameBoard.isThereAWallOrEdge(gameBoard.getTile(startingTileRow, startingTileColumn), direction));
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"9,0", "9,9", "0,15", "99,99"})
  void getTileTest_InvalidParameterExceptionIsThrown(int row, int column) throws BuilderException, InvalidParameterException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    Assertions.assertThrows(InvalidParameterException.class, () -> gameBoard.getTile(row, column));
  }

  //=======================

  @Test
  void cloneTest_withoutWalls() throws CloneNotSupportedException, BuilderException, InvalidParameterException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Assertions.assertEquals(gameBoard, gameBoard.clone());
  }

  @Test
  void cloneTest_withWalls() throws CloneNotSupportedException, InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    gameBoard.getTile(2, 3).setLink(UP, WALL);
    gameBoard.getTile(5, 2).setLink(LEFT, WALL);
    gameBoard.getTile(0, 0).setLink(DOWN, WALL);

    Assertions.assertEquals(gameBoard, gameBoard.clone());
  }

  @Test
  void cloneTest_objectsAreIndependent_clonedIsModified() throws CloneNotSupportedException, InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    gameBoard.getTile(2, 3).setLink(UP, WALL);
    gameBoard.getTile(5, 2).setLink(LEFT, WALL);
    gameBoard.getTile(0, 0).setLink(DOWN, WALL);

    GameBoard clonedGameBoard = gameBoard.clone();

    clonedGameBoard.getTile(6, 2).setLink(UP, WALL);
    clonedGameBoard.getTile(1, 4).setLink(RIGHT, WALL);
    clonedGameBoard.getTile(5, 2).setLink(LEFT, FREE);

    Assertions.assertNotEquals(gameBoard, clonedGameBoard);
  }

  @Test
  void cloneTest_objectsAreIndependent_originalIsModified() throws CloneNotSupportedException, InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    gameBoard.getTile(2, 3).setLink(UP, WALL);
    gameBoard.getTile(5, 2).setLink(LEFT, WALL);
    gameBoard.getTile(0, 0).setLink(DOWN, WALL);

    GameBoard clonedGameBoard = gameBoard.clone();

    gameBoard.getTile(6, 2).setLink(UP, WALL);
    gameBoard.getTile(1, 4).setLink(RIGHT, WALL);
    gameBoard.getTile(5, 2).setLink(LEFT, FREE);

    Assertions.assertNotEquals(gameBoard, clonedGameBoard);
  }
}
