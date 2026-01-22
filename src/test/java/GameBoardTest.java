import it.units.sdm.quoridor.exceptions.*;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.abstracts.AbstractGameBoard;
import it.units.sdm.quoridor.model.abstracts.AbstractTile;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.directions.StraightDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class GameBoardTest {
  private static AbstractGame buildGame(int nOfPlayers) throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StandardQuoridorBuilder(nOfPlayers));
    return builderDirector.makeGame();
  }

  @Test
  void constructorTest() {
    AbstractTile[][] invalidGameState = {{null, null}, {null}};
    Assertions.assertThrows(InvalidParameterException.class, () -> new GameBoard(invalidGameState));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 3, 5})
  void getRowTiles_tilesAreConsistent(int row) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(2);
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
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    Assertions.assertThrows(InvalidParameterException.class, () -> gameBoard.getRowTiles(row));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 3, 5})
  void getColumnTiles_tilesAreConsistent(int column) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(2);
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
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    Assertions.assertThrows(InvalidParameterException.class, () -> gameBoard.getColumnTiles(column));
  }

  @ParameterizedTest
  @CsvSource({"0,9", "-1,6", "10,22", "11,2"})
  void getTile_coordinatesOutsideGameBoardThrowException(int row, int column) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position position = new Position(row, column);
    Assertions.assertThrows(InvalidParameterException.class, () -> gameBoard.getTile(position));
  }


  @ParameterizedTest
  @CsvSource({"0,0,4", "1,8,4", "2,4,0", "3,4,8"})
  void getStartingAndDestinationTiles_startingTilesAreCorrect(int pawnIndex, int row, int column) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(4);
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position position = new Position(row, column);
    Assertions.assertEquals(gameBoard.getTile(position),
            gameBoard.getStartingAndDestinationTiles().get(pawnIndex).startingTile());
  }

  @ParameterizedTest
  @CsvSource({"0,8,1", "0,8,6", "1,0,4", "1,0,7", "2,4,8", "2,2,8", "3,3,0", "3,8,0"})
  void getStartingAndDestinationTiles_destinationTilesContainsCorrectTiles(int pawnIndex, int row, int column) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(4);
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position position = new Position(row, column);
    Assertions.assertTrue(gameBoard.getStartingAndDestinationTiles().get(pawnIndex).destinationTiles().contains(gameBoard.getTile(position)));
  }

  @ParameterizedTest
  @CsvSource({"1,8,1", "2,8,6", "0,0,4", "3,0,7", "1,4,8", "3,2,8", "0,3,0", "2,8,0"})
  void getStartingAndDestinationTiles_destinationTilesDoNotContainWrongTiles(int pawnIndex, int row, int column) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(4);
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position position = new Position(row, column);
    Assertions.assertFalse(gameBoard.getStartingAndDestinationTiles().get(pawnIndex).destinationTiles().contains(gameBoard.getTile(position)));
  }

  @ParameterizedTest
  @CsvSource({"6, 4, 6, 2, LEFT", "2, 4, 2, 6, RIGHT", "5, 7, 3, 7, UP", "5, 8, 7, 8, DOWN"})
  void getLandingTileTest_landingTileIsCorrect(int testRow, int testColumn, int expectedRow, int expectedColumn, StraightDirection direction) throws OutOfGameBoardException, BuilderException, InvalidParameterException {
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile testTile = gameBoard.getTile(new Position(testRow, testColumn));
    AbstractTile upperTile = gameBoard.getTile(new Position(expectedRow, expectedColumn));
    Assertions.assertEquals(upperTile, gameBoard.getLandingTile(testTile, direction));
  }

  @ParameterizedTest
  @CsvSource({"1, 4, 1, 3, LEFT", "3, 7, 3, 8, RIGHT", "8, 2, 7, 2, UP", "3, 8, 4, 8, DOWN"})
  void getAdjacentTile_adjacentTileIsCorrect(int testRow, int testColumn, int expectedRow, int expectedColumn, StraightDirection direction) throws OutOfGameBoardException, InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile testTile = gameBoard.getTile(new Position(testRow, testColumn));
    AbstractTile leftTile = gameBoard.getTile(new Position(expectedRow, expectedColumn));
    Assertions.assertEquals(leftTile, gameBoard.getAdjacentTile(testTile, direction));
  }

  @ParameterizedTest
  @CsvSource({"3,4", "7,6", "1,2"})
  void isThereAWall_horizontalWallIsPresentBetweenTwoAdjacentTiles(int testTileRow, int testTileColumn) throws InvalidParameterException, NotAdjacentTilesException, BuilderException, OutOfGameBoardException, InvalidActionException {
    AbstractGame game = buildGame(2);
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
    AbstractGame game = buildGame(2);
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
    AbstractGame game = buildGame(2);
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
    AbstractGame game = buildGame(2);
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
  void isThereAWallOrEdgeTest_horizontalWallIsPresent(int testTileRow, int testTileColumn) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position testTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile testTile = gameBoard.getTile(testTilePosition);
    game.placeWall(testTilePosition, HORIZONTAL);
    Assertions.assertTrue(testTile.isThereAWallOrEdge(DOWN));
  }

  @ParameterizedTest
  @CsvSource({"2,5", "1,4", "7,7"})
  void isThereAWallOrEdgeTest_verticalWallIsPresent(int testTileRow, int testTileColumn) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position testTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile testTile = gameBoard.getTile(testTilePosition);
    game.placeWall(testTilePosition, VERTICAL);
    Assertions.assertTrue(testTile.isThereAWallOrEdge(LEFT));
  }

  @ParameterizedTest
  @CsvSource({"1,2", "5,2", "4,3"})
  void isThereAWallOrEdgeTest_wallOrEdgeIsAbsent(int testTileRow, int testTileColumn) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    Position testTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile testTile = gameBoard.getTile(testTilePosition);
    game.placeWall(testTilePosition, VERTICAL);
    game.placeWall(testTilePosition, HORIZONTAL);
    Assertions.assertFalse(testTile.isThereAWallOrEdge(RIGHT));
  }

  @ParameterizedTest
  @CsvSource({"0,2,UP", "8,0,LEFT", "8,0,DOWN"})
  void isThereAWallOrEdgeTest_edgeIsPresent(int testTileRow, int testTileColumn, StraightDirection direction) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile testTile = gameBoard.getTile(new Position(testTileRow, testTileColumn));
    Assertions.assertTrue(testTile.isThereAWallOrEdge(direction));
  }

  @ParameterizedTest
  @CsvSource({"9,0", "9,9", "0,15", "99,99"})
  void getTileTest_InvalidParameterExceptionIsThrown(int row, int column) throws BuilderException, InvalidParameterException {
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    Assertions.assertThrows(InvalidParameterException.class,
            () -> gameBoard.getTile(new Position(row, column)));
  }

  @Test
  void cloneTest_cloneEqualToNewGameBoard() throws CloneNotSupportedException, InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    Assertions.assertEquals(gameBoard, gameBoard.clone());
  }

  @Test
  void cloneTest_withWalls() throws CloneNotSupportedException, InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame(2);
    AbstractGameBoard gameBoard = game.getGameBoard();

    game.placeWall(new Position(3, 4), HORIZONTAL);
    game.placeWall(new Position(1, 6), VERTICAL);
    game.placeWall(new Position(6, 5), HORIZONTAL);
    Assertions.assertEquals(gameBoard, gameBoard.clone());
  }
}
