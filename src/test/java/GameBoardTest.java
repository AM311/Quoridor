import it.units.sdm.quoridor.exceptions.*;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;

import static it.units.sdm.quoridor.utils.WallOrientation.*;

import it.units.sdm.quoridor.utils.directions.StraightDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
  void getRowTiles_InvalidParameterExceptionIsThrown(int row) throws InvalidParameterException, BuilderException {
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
  void isThereAWall_horizontalWallIsPresentBetweenTwoAdjacentTiles(int testTileRow, int testTileColumn) throws InvalidParameterException, NotAdjacentTilesException, BuilderException, OutOfGameBoardException, InvalidActionException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position testTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile testTile = gameBoard.getTile(testTilePosition);
    AbstractTile adjacentTestTile = gameBoard.getAdjacentTile(testTile, DOWN);
    game.placeWall(testTilePosition, HORIZONTAL);
    Assertions.assertTrue(gameBoard.isThereAWall(testTile, adjacentTestTile));
  }

  @ParameterizedTest
  @CsvSource({"5,6", "6,6", "3,1"})
  void isThereAWall_verticalWallIsPresentBetweenTwoAdjacentTiles(int testTileRow, int testTileColumn) throws InvalidParameterException, NotAdjacentTilesException, BuilderException, OutOfGameBoardException, InvalidActionException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position testTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile testTile = gameBoard.getTile(testTilePosition);
    AbstractTile adjacentTestTile = gameBoard.getAdjacentTile(testTile, LEFT);
    game.placeWall(testTilePosition, VERTICAL);
    Assertions.assertTrue(gameBoard.isThereAWall(testTile, adjacentTestTile));
  }

  @ParameterizedTest
  @CsvSource({"3,4", "7,6", "1,2"})
  void isThereAWallTest_wallIsAbsent(int testTileRow, int testTileColumn) throws InvalidParameterException, NotAdjacentTilesException, BuilderException, InvalidActionException, OutOfGameBoardException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position testTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile testTile = gameBoard.getTile(testTilePosition);
    AbstractTile adjacentTestTile = gameBoard.getAdjacentTile(testTile, RIGHT);
    game.placeWall(testTilePosition, VERTICAL);
    game.placeWall(testTilePosition, HORIZONTAL);
    Assertions.assertFalse(gameBoard.isThereAWall(testTile, adjacentTestTile));
  }

  @ParameterizedTest
  @CsvSource({"3,4", "7,6", "1,2"})
  void isThereAWallTest_tilesAreNotAdjacent(int testTileRow, int testTileColumn) throws InvalidParameterException, BuilderException, OutOfGameBoardException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position testTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile testTile = gameBoard.getTile(testTilePosition);
    AbstractTile adjacentTestTile = gameBoard.getAdjacentTile(testTile, LEFT);
    AbstractTile notAdjacentTile = gameBoard.getAdjacentTile(adjacentTestTile, UP);
    Assertions.assertThrows(NotAdjacentTilesException.class,
            () -> gameBoard.isThereAWall(testTile, notAdjacentTile));
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"2,5", "1,4", "7,7"})
  void isThereAWallOrEdgeTest_horizontalWallIsPresent(int testTileRow, int testTileColumn) throws InvalidParameterException, BuilderException, InvalidActionException, OutOfGameBoardException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position testTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile testTile = gameBoard.getTile(testTilePosition);
    game.placeWall(testTilePosition, HORIZONTAL);
    Assertions.assertTrue(gameBoard.isThereAWallOrEdge(testTile, DOWN));
  }

  @ParameterizedTest
  @CsvSource({"2,5", "1,4", "7,7"})
  void isThereAWallOrEdgeTest_verticalWallIsPresent(int testTileRow, int testTileColumn) throws InvalidParameterException, BuilderException, OutOfGameBoardException, InvalidActionException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position testTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile testTile = gameBoard.getTile(testTilePosition);
    game.placeWall(testTilePosition, VERTICAL);
    Assertions.assertTrue(gameBoard.isThereAWallOrEdge(testTile, LEFT));
  }

  @ParameterizedTest
  @CsvSource({"1,2", "5,2", "4,3"})
  void isThereAWallOrEdgeTest_wallOrEdgeIsAbsent(int testTileRow, int testTileColumn) throws InvalidParameterException, BuilderException, InvalidActionException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position testTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile testTile = gameBoard.getTile(testTilePosition);
    game.placeWall(testTilePosition, VERTICAL);
    game.placeWall(testTilePosition, HORIZONTAL);
    Assertions.assertFalse(gameBoard.isThereAWall(testTile, RIGHT));
  }

  @ParameterizedTest
  @CsvSource({"0,2,UP", "8,0,LEFT", "8,0,DOWN"})
  void isThereAWallOrEdgeTest_edgeIsPresent(int testTileRow, int testTileColumn, StraightDirection direction) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile testTile = gameBoard.getTile(new Position(testTileRow, testTileColumn));
    Assertions.assertTrue(gameBoard.isThereAWallOrEdge(testTile, direction));
  }

  //=======================

  @ParameterizedTest
  @CsvSource({"9,0", "9,9", "0,15", "99,99"})
  void getTileTest_InvalidParameterExceptionIsThrown(int row, int column) throws BuilderException, InvalidParameterException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Assertions.assertThrows(InvalidParameterException.class,
            () -> gameBoard.getTile(new Position(row, column)));
  }

  //=======================
  @Test
  void cloneTest_cloneEqualToNewGameBoard() throws CloneNotSupportedException, InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    Assertions.assertEquals(gameBoard, gameBoard.clone());
  }

  @Test
  void cloneTest_withWalls() throws CloneNotSupportedException, InvalidParameterException, BuilderException, InvalidActionException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    game.placeWall(new Position(3, 4), HORIZONTAL);
    game.placeWall(new Position(1, 6), VERTICAL);
    game.placeWall(new Position(6, 5), HORIZONTAL);
    Assertions.assertEquals(gameBoard, gameBoard.clone());
  }

  @Test
  void cloneTest_objectsAreIndependent_clonedIsModified() throws CloneNotSupportedException, InvalidParameterException, BuilderException, InvalidActionException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    game.placeWall(new Position(7, 3), HORIZONTAL);

    GameBoard clonedGameBoard = gameBoard.clone();

    game.placeWall(new Position(4, 1), VERTICAL);

    Assertions.assertNotEquals(gameBoard, clonedGameBoard);
  }

  @Test
  void cloneTest_objectsAreIndependent_originalIsModified() throws CloneNotSupportedException, InvalidParameterException, BuilderException, InvalidActionException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    game.placeWall(new Position(7, 3), HORIZONTAL);

    GameBoard clonedGameBoard = gameBoard.clone();

    game.placeWall(new Position(4, 1), VERTICAL);

    Assertions.assertNotEquals(gameBoard, clonedGameBoard);
  }
}
