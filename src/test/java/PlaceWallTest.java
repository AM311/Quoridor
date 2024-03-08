import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.model.Wall;
import it.units.sdm.quoridor.movemanager.ActionChecker;
import it.units.sdm.quoridor.movemanager.WallPlacementChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.Direction.*;
import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;

public class PlaceWallTest {
  //todo check if the declaration of the objects for all the tests leads to issues or inconsistencies
  GameBoard gameBoard = new GameBoard();
  GameBoard.Tile tile1 = gameBoard.getGameState()[0][4];
  GameBoard.Tile tile2 = gameBoard.getGameState()[8][4];
  Pawn pawn1 = new Pawn(tile1, Color.black, 10);

  Pawn pawn2 = new Pawn(tile2, Color.black, 10);

  List<Pawn> pawns = List.of(pawn1, pawn2);
  Game game = new Game(pawns, gameBoard);

  ActionChecker<Wall> wallPlacementChecker = new WallPlacementChecker();


  @ParameterizedTest
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnLowerLinkAfterHorizontalWallPlacement_startingTile_innerTiles(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(HORIZONTAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, FREE, WALL};
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
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnUpperLinkAfterHorizontalWallPlacement_tileBelowStartingTile_innerTiles(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(HORIZONTAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, WALL, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row + 1][column].getLink(LEFT),
                    gameBoard.getGameState()[row + 1][column].getLink(RIGHT),
                    gameBoard.getGameState()[row + 1][column].getLink(UP),
                    gameBoard.getGameState()[row + 1][column].getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnLowerLinkAfterHorizontalWallPlacement_tileRightToStartingTile_innerTiles(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(HORIZONTAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, FREE, WALL};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row][column + 1].getLink(LEFT),
                    gameBoard.getGameState()[row][column + 1].getLink(RIGHT),
                    gameBoard.getGameState()[row][column + 1].getLink(UP),
                    gameBoard.getGameState()[row][column + 1].getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnUpperLinkAfterHorizontalWallPlacement_tileLowRightDiagToStartingTile_innerTiles(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(HORIZONTAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, WALL, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row + 1][column + 1].getLink(LEFT),
                    gameBoard.getGameState()[row + 1][column + 1].getLink(RIGHT),
                    gameBoard.getGameState()[row + 1][column + 1].getLink(UP),
                    gameBoard.getGameState()[row + 1][column + 1].getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  void wallOnLowerLinkAfterHorizontalWallPlacement_startingTile_upperLeftCorner() {
    GameBoard.Tile startingTile = gameBoard.getGameState()[0][0];
    Wall wall = new Wall(HORIZONTAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE, EDGE, WALL};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[0][0].getLink(LEFT),
                    gameBoard.getGameState()[0][0].getLink(RIGHT),
                    gameBoard.getGameState()[0][0].getLink(UP),
                    gameBoard.getGameState()[0][0].getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnLeftLinkAfterVerticalWallPlacement_startingTile_innerTiles(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(VERTICAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{WALL, FREE, FREE, FREE};
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
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnLeftLinkAfterVerticalWallPlacement_tileAboveStartingTile_innerTiles(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(VERTICAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{WALL, FREE, FREE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row - 1][column].getLink(LEFT),
                    gameBoard.getGameState()[row - 1][column].getLink(RIGHT),
                    gameBoard.getGameState()[row - 1][column].getLink(UP),
                    gameBoard.getGameState()[row - 1][column].getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnRightLinkAfterVerticalWallPlacement_tileLeftToStartingTile_innerTiles(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(VERTICAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, WALL, FREE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row][column - 1].getLink(LEFT),
                    gameBoard.getGameState()[row][column - 1].getLink(RIGHT),
                    gameBoard.getGameState()[row][column - 1].getLink(UP),
                    gameBoard.getGameState()[row][column - 1].getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnRightLinkAfterVerticalWallPlacement_tileUpLeftDiagToStartingTile_innerTiles(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(VERTICAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, WALL, FREE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[row - 1][column - 1].getLink(LEFT),
                    gameBoard.getGameState()[row - 1][column - 1].getLink(RIGHT),
                    gameBoard.getGameState()[row - 1][column - 1].getLink(UP),
                    gameBoard.getGameState()[row - 1][column - 1].getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  void wallOnLeftLinkAfterVerticalWallPlacement_startingTile_lowerRightCorner() {
    GameBoard.Tile startingTile = gameBoard.getGameState()[gameBoard.getSideLength() - 1][gameBoard.getSideLength() - 1];
    Wall wall = new Wall(VERTICAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{WALL, EDGE, FREE, EDGE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getGameState()[gameBoard.getSideLength() - 1][gameBoard.getSideLength() - 1].getLink(LEFT),
                    gameBoard.getGameState()[gameBoard.getSideLength() - 1][gameBoard.getSideLength() - 1].getLink(RIGHT),
                    gameBoard.getGameState()[gameBoard.getSideLength() - 1][gameBoard.getSideLength() - 1].getLink(UP),
                    gameBoard.getGameState()[gameBoard.getSideLength() - 1][gameBoard.getSideLength() - 1].getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"3, 3", "6, 2", "0, 0"})
  void horizontalWallIsAllowed(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(HORIZONTAL, startingTile);

    Assertions.assertTrue(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"8, 0", "4, 8", "8, 8", "0, 8"})
  void horizontalWallIsNotAllowed(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(HORIZONTAL, startingTile);

    Assertions.assertFalse(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "3, 3", "6, 4"})
  void horizontalWallCrossingVerticalWallIsNotAllowed(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    GameBoard.Tile belowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];
    Wall wall = new Wall(HORIZONTAL, startingTile);

    startingTile.setLink(RIGHT, WALL);
    belowStartingTile.setLink(RIGHT, WALL);


    Assertions.assertFalse(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "3, 4", "7, 2"})
  void horizontalWallAboveVerticalIsAllowed(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    GameBoard.Tile belowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];
    Wall wall = new Wall(HORIZONTAL, startingTile);

    belowStartingTile.setLink(RIGHT, WALL);

    Assertions.assertTrue(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"6, 1", "2, 5", "3, 2"})
  void horizontalWallsOverlappingIsNotAllowedFirstCase(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(HORIZONTAL, startingTile);
    startingTile.setLink(DOWN, WALL);

    Assertions.assertFalse(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"2, 3", "1, 4", "4, 7"})
  void horizontalWallsOverlappingIsNotAllowedSecondCase(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(HORIZONTAL, startingTile);
    GameBoard.Tile tileRightToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() + 1];

    tileRightToStartingTile.setLink(DOWN, WALL);

    Assertions.assertFalse(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"1, 6", "4, 4", "7, 7"})
  void horizontalWallsNearEachOtherIsAllowed(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(HORIZONTAL, startingTile);
    GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

    tileLeftToStartingTile.setLink(DOWN, WALL);

    Assertions.assertTrue(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "4, 0", "0, 7"})
  void verticalWallIsNotAllowed(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(VERTICAL, startingTile);

    Assertions.assertFalse(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"8, 8", "4, 4", "1, 8"})
  void verticalWallIsAllowed(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(VERTICAL, startingTile);

    Assertions.assertTrue(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "5, 7", "1, 7"})
  void verticalWallCrossingHorizontalWallIsNotAllowed(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(VERTICAL, startingTile);
    GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

    startingTile.setLink(UP, WALL);
    tileLeftToStartingTile.setLink(UP, WALL);

    Assertions.assertFalse(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "3, 3", "2, 4"})
  void verticalWallRightToHorizontalWallIsAllowed(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(VERTICAL, startingTile);
    GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

    tileLeftToStartingTile.setLink(UP, WALL);

    Assertions.assertTrue(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "7, 2", "4, 6"})
  void verticalWallsOverlappingIsNotAllowedFirstCase(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(VERTICAL, startingTile);

    startingTile.setLink(LEFT, WALL);

    Assertions.assertFalse(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"5, 4", "5, 1", "6, 3"})
  void verticalWallsOverlappingIsNotAllowedSecondCase(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(VERTICAL, startingTile);
    GameBoard.Tile tileAboveStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn()];

    tileAboveStartingTile.setLink(LEFT, WALL);

    Assertions.assertFalse(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }

  @ParameterizedTest
  @CsvSource({"6, 2", "3, 5", "5, 1"})
  void verticalWallsNearEachOtherIsAllowed(int row, int column) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
    Wall wall = new Wall(VERTICAL, startingTile);
    GameBoard.Tile tileBelowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];

    tileBelowStartingTile.setLink(DOWN, WALL);

    Assertions.assertTrue(wallPlacementChecker.checkAction(gameBoard, pawn1, wall));
  }
}