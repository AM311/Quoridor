import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.model.Wall;
import it.units.sdm.quoridor.movemanager.ActionChecker;
import it.units.sdm.quoridor.movemanager.WallPlacementChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class PlaceWallTest {

  ActionChecker<Wall> wallPlacementChecker = new WallPlacementChecker();

  @ParameterizedTest
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnLowerLinkAfterHorizontalWallPlacement_startingTile_innerTiles(int row, int column) throws InvalidActionException {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(HORIZONTAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, FREE, WALL};
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
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnUpperLinkAfterHorizontalWallPlacement_tileBelowStartingTile_innerTiles(int row, int column) throws InvalidActionException {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(HORIZONTAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, WALL, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row+1, column).getLink(LEFT),
                    gameBoard.getTile(row+1, column).getLink(RIGHT),
                    gameBoard.getTile(row+1, column).getLink(UP),
                    gameBoard.getTile(row+1, column).getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnLowerLinkAfterHorizontalWallPlacement_tileRightToStartingTile_innerTiles(int row, int column) throws InvalidActionException {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(HORIZONTAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, FREE, WALL};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row, column+1).getLink(LEFT),
                    gameBoard.getTile(row, column+1).getLink(RIGHT),
                    gameBoard.getTile(row, column+1).getLink(UP),
                    gameBoard.getTile(row, column+1).getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnUpperLinkAfterHorizontalWallPlacement_tileLowRightDiagToStartingTile_innerTiles(int row, int column) throws InvalidActionException {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(HORIZONTAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, WALL, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row+1, column+1).getLink(LEFT),
                    gameBoard.getTile(row+1, column+1).getLink(RIGHT),
                    gameBoard.getTile(row+1, column+1).getLink(UP),
                    gameBoard.getTile(row+1, column+1).getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  void wallOnLowerLinkAfterHorizontalWallPlacement_startingTile_upperLeftCorner() throws InvalidActionException {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(0, 0);
    Wall wall = new Wall(HORIZONTAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE, EDGE, WALL};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(0, 0).getLink(LEFT),
                    gameBoard.getTile(0, 0).getLink(RIGHT),
                    gameBoard.getTile(0, 0).getLink(UP),
                    gameBoard.getTile(0, 0).getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnLeftLinkAfterVerticalWallPlacement_startingTile_innerTiles(int row, int column) throws InvalidActionException {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(VERTICAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{WALL, FREE, FREE, FREE};
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
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnLeftLinkAfterVerticalWallPlacement_tileAboveStartingTile_innerTiles(int row, int column) throws InvalidActionException {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(VERTICAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{WALL, FREE, FREE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row - 1, column).getLink(LEFT),
                    gameBoard.getTile(row - 1, column).getLink(RIGHT),
                    gameBoard.getTile(row - 1, column).getLink(UP),
                    gameBoard.getTile(row - 1, column).getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnRightLinkAfterVerticalWallPlacement_tileLeftToStartingTile_innerTiles(int row, int column) throws InvalidActionException {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(VERTICAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, WALL, FREE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row, column-1).getLink(LEFT),
                    gameBoard.getTile(row, column-1).getLink(RIGHT),
                    gameBoard.getTile(row, column-1).getLink(UP),
                    gameBoard.getTile(row, column-1).getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnRightLinkAfterVerticalWallPlacement_tileUpLeftDiagToStartingTile_innerTiles(int row, int column) throws InvalidActionException {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(VERTICAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, WALL, FREE, FREE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(row-1, column-1).getLink(LEFT),
                    gameBoard.getTile(row-1, column-1).getLink(RIGHT),
                    gameBoard.getTile(row-1, column-1).getLink(UP),
                    gameBoard.getTile(row-1, column-1).getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  void wallOnLeftLinkAfterVerticalWallPlacement_startingTile_lowerRightCorner() throws InvalidActionException {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(GameBoard.SIDE_LENGTH - 1, GameBoard.SIDE_LENGTH - 1);
    Wall wall = new Wall(VERTICAL, startingTile);
    game.placeWall(wall);

    GameBoard.LinkState[] expected = new GameBoard.LinkState[]{WALL, EDGE, FREE, EDGE};
    GameBoard.LinkState[] actual = new GameBoard.LinkState[]
            {
                    gameBoard.getTile(GameBoard.SIDE_LENGTH - 1, GameBoard.SIDE_LENGTH - 1).getLink(LEFT),
                    gameBoard.getTile(GameBoard.SIDE_LENGTH - 1, GameBoard.SIDE_LENGTH - 1).getLink(RIGHT),
                    gameBoard.getTile(GameBoard.SIDE_LENGTH - 1, GameBoard.SIDE_LENGTH - 1).getLink(UP),
                    gameBoard.getTile(GameBoard.SIDE_LENGTH - 1, GameBoard.SIDE_LENGTH - 1).getLink(DOWN)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"3, 3", "6, 2", "0, 0"})
  void horizontalWallIsAllowed(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(HORIZONTAL, startingTile);

    Assertions.assertTrue(wallPlacementChecker.checkAction(game, wall));
  }


  @ParameterizedTest
  @CsvSource({"8, 0", "4, 8", "8, 8", "0, 8"})
  void horizontalWallIsNotAllowed(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(HORIZONTAL, startingTile);

    Assertions.assertFalse(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "3, 3", "6, 4"})
  void horizontalWallCrossingVerticalWallIsNotAllowed(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Tile belowStartingTile = gameBoard.getTile(startingTile.getRow() + 1,startingTile.getColumn());
    Wall wall = new Wall(HORIZONTAL, startingTile);

    startingTile.setLink(RIGHT, WALL);
    belowStartingTile.setLink(RIGHT, WALL);


    Assertions.assertFalse(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "3, 4", "7, 2"})
  void horizontalWallAboveVerticalIsAllowed(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Tile belowStartingTile = gameBoard.getTile(startingTile.getRow() + 1,startingTile.getColumn());
    Wall wall = new Wall(HORIZONTAL, startingTile);

    belowStartingTile.setLink(RIGHT, WALL);

    Assertions.assertTrue(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"6, 1", "2, 5", "3, 2"})
  void horizontalWallsOverlappingIsNotAllowedFirstCase(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(HORIZONTAL, startingTile);
    startingTile.setLink(DOWN, WALL);

    Assertions.assertFalse(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"2, 3", "1, 4", "4, 7"})
  void horizontalWallsOverlappingIsNotAllowedSecondCase(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(HORIZONTAL, startingTile);
    Tile tileRightToStartingTile = gameBoard.getTile(startingTile.getRow(),startingTile.getColumn() + 1);

    tileRightToStartingTile.setLink(DOWN, WALL);

    Assertions.assertFalse(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"1, 6", "4, 4", "7, 7"})
  void horizontalWallsNearEachOtherIsAllowed(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(HORIZONTAL, startingTile);
    Tile tileLeftToStartingTile = gameBoard.getTile(startingTile.getRow(),startingTile.getColumn() - 1);

    tileLeftToStartingTile.setLink(DOWN, WALL);

    Assertions.assertTrue(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "4, 0", "0, 7"})
  void verticalWallIsNotAllowed(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(VERTICAL, startingTile);

    Assertions.assertFalse(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"8, 8", "4, 4", "1, 8"})
  void verticalWallIsAllowed(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(VERTICAL, startingTile);

    Assertions.assertTrue(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "5, 7", "1, 7"})
  void verticalWallCrossingHorizontalWallIsNotAllowed(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(VERTICAL, startingTile);
    Tile tileLeftToStartingTile = gameBoard.getTile(startingTile.getRow(),startingTile.getColumn() - 1);

    startingTile.setLink(UP, WALL);
    tileLeftToStartingTile.setLink(UP, WALL);

    Assertions.assertFalse(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "3, 3", "2, 4"})
  void verticalWallRightToHorizontalWallIsAllowed(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(VERTICAL, startingTile);
    Tile tileLeftToStartingTile = gameBoard.getTile(startingTile.getRow(),startingTile.getColumn() - 1);

    tileLeftToStartingTile.setLink(UP, WALL);

    Assertions.assertTrue(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "7, 2", "4, 6"})
  void verticalWallsOverlappingIsNotAllowedFirstCase(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(VERTICAL, startingTile);

    startingTile.setLink(LEFT, WALL);

    Assertions.assertFalse(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"5, 4", "5, 1", "6, 3"})
  void verticalWallsOverlappingIsNotAllowedSecondCase(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(VERTICAL, startingTile);
    Tile tileAboveStartingTile = gameBoard.getTile(startingTile.getRow() - 1,startingTile.getColumn());

    tileAboveStartingTile.setLink(LEFT, WALL);

    Assertions.assertFalse(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"6, 2", "3, 5", "5, 1"})
  void verticalWallsNearEachOtherIsAllowed(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(VERTICAL, startingTile);
    Tile tileBelowStartingTile = gameBoard.getTile(startingTile.getRow() + 1,startingTile.getColumn());

    tileBelowStartingTile.setLink(DOWN, WALL);

    Assertions.assertTrue(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"3, 3", "6, 2", "0, 0"})
  void horizontalWallNotIsAllowed_IfZeroWallsRemaining(int row, int column) {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    for(int i = 0; i < 10; i++)
      game.getPlayingPawn().decrementNumberOfWalls();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(HORIZONTAL, startingTile);

    Assertions.assertFalse(wallPlacementChecker.checkAction(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"3, 3", "6, 2", "0, 0"})
  void numberOfWallsIsConsistentAfterPlacingAWall(int row, int column) throws InvalidActionException {
    Game game = new Game(2);
    GameBoard gameBoard = game.getGameBoard();
    int numberOfWallsBeforePlacement = game.getPlayingPawn().getNumberOfWalls();
    Tile startingTile = gameBoard.getTile(row, column);
    Wall wall = new Wall(HORIZONTAL, startingTile);
    game.placeWall(wall);

    Assertions.assertEquals(numberOfWallsBeforePlacement - 1, game.getPlayingPawn().getNumberOfWalls());
  }

}