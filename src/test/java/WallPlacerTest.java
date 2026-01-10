import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.NumberOfWallsBelowZeroException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractGameBoard;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.model.Wall;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.movemanagement.actions.WallPlacer;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static it.units.sdm.quoridor.model.AbstractTile.LinkState.*;
import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.LEFT;

public class WallPlacerTest {

  private final WallPlacer wallPlacer = new WallPlacer();

  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(4));
    return builderDirector.makeGame();
  }


  @ParameterizedTest
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnLowerLinkAfterHorizontalWallPlacement_startingTile_innerTiles(int row, int column) throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    Wall wall = new Wall(HORIZONTAL, startingTile);

    wallPlacer.execute(game, wall);

    AbstractTile.LinkState[] expected = new AbstractTile.LinkState[]{FREE, FREE, WALL, FREE};

    AbstractTile.LinkState[] actual = new AbstractTile.LinkState[]
            {
                    startingTile.getLink(UP),
                    startingTile.getLink(RIGHT),
                    startingTile.getLink(DOWN),
                    startingTile.getLink(LEFT)
            };

    Assertions.assertArrayEquals(expected, actual);
  }


  @ParameterizedTest
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnUpperLinkAfterHorizontalWallPlacement_tileBelowStartingTile_innerTiles(int row, int column) throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    AbstractTile checkedTile = gameBoard.getTile(new Position(row + 1, column));

    Wall wall = new Wall(HORIZONTAL, startingTile);

    wallPlacer.execute(game, wall);

    AbstractTile.LinkState[] expected = new AbstractTile.LinkState[]{WALL, FREE, FREE, FREE};

    AbstractTile.LinkState[] actual = new AbstractTile.LinkState[]
            {
                    checkedTile.getLink(UP),
                    checkedTile.getLink(RIGHT),
                    checkedTile.getLink(DOWN),
                    checkedTile.getLink(LEFT)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnLowerLinkAfterHorizontalWallPlacement_tileRightToStartingTile_innerTiles(int row, int column) throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    AbstractTile checkedTile = gameBoard.getTile(new Position(row, column + 1));

    Wall wall = new Wall(HORIZONTAL, startingTile);

    wallPlacer.execute(game, wall);

    AbstractTile.LinkState[] expected = new AbstractTile.LinkState[]{FREE, FREE, WALL, FREE};

    AbstractTile.LinkState[] actual = new AbstractTile.LinkState[]
            {
                    checkedTile.getLink(UP),
                    checkedTile.getLink(RIGHT),
                    checkedTile.getLink(DOWN),
                    checkedTile.getLink(LEFT)
            };

    Assertions.assertArrayEquals(expected, actual);
  }


  @ParameterizedTest
  @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
  void wallOnUpperLinkAfterHorizontalWallPlacement_tileDownRightToStartingTile_innerTiles(int row, int column) throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    AbstractTile checkedTile = gameBoard.getTile(new Position(row + 1, column + 1));

    Wall wall = new Wall(HORIZONTAL, startingTile);

    wallPlacer.execute(game, wall);

    AbstractTile.LinkState[] expected = new AbstractTile.LinkState[]{WALL, FREE, FREE, FREE};
    AbstractTile.LinkState[] actual = new AbstractTile.LinkState[]
            {
                    checkedTile.getLink(UP),
                    checkedTile.getLink(RIGHT),
                    checkedTile.getLink(DOWN),
                    checkedTile.getLink(LEFT)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  void wallOnLowerLinkAfterHorizontalWallPlacement_startingTile_upperLeftCorner() throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(0, 0));
    AbstractTile checkedTile = gameBoard.getTile(new Position(0, 0));
    Wall wall = new Wall(HORIZONTAL, startingTile);

    wallPlacer.execute(game, wall);

    AbstractTile.LinkState[] expected = new AbstractTile.LinkState[]{EDGE, FREE, WALL, EDGE};
    AbstractTile.LinkState[] actual = new AbstractTile.LinkState[]
            {
                    checkedTile.getLink(UP),
                    checkedTile.getLink(RIGHT),
                    checkedTile.getLink(DOWN),
                    checkedTile.getLink(LEFT)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnLeftLinkAfterVerticalWallPlacement_startingTile_innerTiles(int row, int column) throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    AbstractTile checkedTile = gameBoard.getTile(new Position(row, column));
    Wall wall = new Wall(VERTICAL, startingTile);

    wallPlacer.execute(game, wall);

    AbstractTile.LinkState[] expected = new AbstractTile.LinkState[]{FREE, FREE, FREE, WALL};
    AbstractTile.LinkState[] actual = new AbstractTile.LinkState[]
            {
                    checkedTile.getLink(UP),
                    checkedTile.getLink(RIGHT),
                    checkedTile.getLink(DOWN),
                    checkedTile.getLink(LEFT)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnLeftLinkAfterVerticalWallPlacement_tileAboveStartingTile_innerTiles(int row, int column) throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    AbstractTile checkedTile = gameBoard.getTile(new Position(row - 1, column));
    Wall wall = new Wall(VERTICAL, startingTile);

    wallPlacer.execute(game, wall);

    AbstractTile.LinkState[] expected = new AbstractTile.LinkState[]{FREE, FREE, FREE, WALL};
    AbstractTile.LinkState[] actual = new AbstractTile.LinkState[]
            {
                    checkedTile.getLink(UP),
                    checkedTile.getLink(RIGHT),
                    checkedTile.getLink(DOWN),
                    checkedTile.getLink(LEFT)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnRightLinkAfterVerticalWallPlacement_tileLeftToStartingTile_innerTiles(int row, int column) throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    AbstractTile checkedTile = gameBoard.getTile(new Position(row, column - 1));

    Wall wall = new Wall(VERTICAL, startingTile);
    wallPlacer.execute(game, wall);

    AbstractTile.LinkState[] expected = new AbstractTile.LinkState[]{FREE, WALL, FREE, FREE};
    AbstractTile.LinkState[] actual = new AbstractTile.LinkState[]
            {
                    checkedTile.getLink(UP),
                    checkedTile.getLink(RIGHT),
                    checkedTile.getLink(DOWN),
                    checkedTile.getLink(LEFT)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
  void wallOnRightLinkAfterVerticalWallPlacement_upLeftTileToStartingTile_innerTiles(int row, int column) throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    AbstractTile checkedTile = gameBoard.getTile(new Position(row - 1, column - 1));

    Wall wall = new Wall(VERTICAL, startingTile);
    wallPlacer.execute(game, wall);

    AbstractTile.LinkState[] expected = new AbstractTile.LinkState[]{FREE, WALL, FREE, FREE};
    AbstractTile.LinkState[] actual = new AbstractTile.LinkState[]
            {
                    checkedTile.getLink(UP),
                    checkedTile.getLink(RIGHT),
                    checkedTile.getLink(DOWN),
                    checkedTile.getLink(LEFT)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  void wallOnLeftLinkAfterVerticalWallPlacement_startingTile_lowerRightCorner() throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(gameBoard.getSideLength() - 1, gameBoard.getSideLength() - 1));

    Wall wall = new Wall(VERTICAL, startingTile);
    wallPlacer.execute(game, wall);

    AbstractTile.LinkState[] expected = new AbstractTile.LinkState[]{FREE, EDGE, EDGE, WALL};
    AbstractTile.LinkState[] actual = new AbstractTile.LinkState[]
            {
                    startingTile.getLink(UP),
                    startingTile.getLink(RIGHT),
                    startingTile.getLink(DOWN),
                    startingTile.getLink(LEFT)
            };

    Assertions.assertArrayEquals(expected, actual);
  }

  @Test
  void verticalWallOutOfEdges_throwsException() throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(0, 0));
    Wall wall = new Wall(VERTICAL, startingTile);

    Assertions.assertThrows(InvalidActionException.class, () -> wallPlacer.execute(game, wall));

  }

  @Test
  void horizontalWallOutOfEdges_throwsException() throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(8, 8));
    Wall wall = new Wall(HORIZONTAL, startingTile);

    Assertions.assertThrows(InvalidActionException.class, () -> wallPlacer.execute(game, wall));
  }

  @ParameterizedTest
  @CsvSource({"3, 3", "6, 2", "0, 0"})
  void numberOfWallsIsConsistentAfterPlacingAWall(int row, int column) throws InvalidParameterException, InvalidActionException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    int numberOfWallsBeforePlacement = game.getPlayingPawn().getNumberOfWalls();
    AbstractTile startingTile = gameBoard.getTile(new Position(row,column));

    Wall wall = new Wall(HORIZONTAL, startingTile);
    wallPlacer.execute(game, wall);

    Assertions.assertEquals(numberOfWallsBeforePlacement - 1, game.getPlayingPawn().getNumberOfWalls());

  }

  @ParameterizedTest
  @CsvSource({"3, 3", "6, 2", "0, 0"})
  void throwsExceptionIfZeroWallsRemaining(int row, int column) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    for (int i = 0; i < 5; i++)
      game.getPlayingPawn().decrementNumberOfWalls();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    Wall wall = new Wall(HORIZONTAL, startingTile);

    Assertions.assertThrows(NumberOfWallsBelowZeroException.class, () -> wallPlacer.execute(game, wall));
  }
}

