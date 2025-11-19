import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractGameBoard;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.model.Wall;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.CheckResult;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.QuoridorCheckResult;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.WallPlacementChecker;
import it.units.sdm.quoridor.model.movemanagement.actions.WallPlacer;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;

public class WallPlacementCheckerTest {


  private final ActionChecker<Wall> wallPlacementChecker = new WallPlacementChecker();
  private final WallPlacer wallPlacer = new WallPlacer();

  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StandardQuoridorBuilder(4));
    return builderDirector.makeGame();
  }

  @ParameterizedTest
  @CsvSource({"3, 3", "6, 2", "0, 0"})
  void horizontalWallIsAllowed(int row, int column) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    Wall wall = new Wall(HORIZONTAL, startingTile);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.OKAY, checkPath);


  }


  @ParameterizedTest
  @CsvSource({"8, 0", "4, 8", "8, 8", "0, 8"})
  void horizontalWallIsNotAllowed(int row, int column) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    Wall wall = new Wall(HORIZONTAL, startingTile);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.INVALID_WALL_POSITION, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "3, 3", "6, 4"})
  void horizontalWallCrossingVerticalWallIsNotAllowed(int row, int column) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTileHorizontal = gameBoard.getTile(new Position(row, column));
    AbstractTile startingTileVertical = gameBoard.getTile(new Position(row + 1, column + 1));

    Wall horizontalWall = new Wall(HORIZONTAL, startingTileHorizontal);
    Wall verticalWall = new Wall(VERTICAL, startingTileVertical);

    wallPlacer.execute(game, verticalWall);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, horizontalWall);
    Assertions.assertEquals(QuoridorCheckResult.INVALID_WALL_POSITION, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "3, 4", "6, 2"})
  void horizontalWallAboveVerticalIsAllowed(int row, int column) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    AbstractTile startingTileHorizontal = gameBoard.getTile(new Position(row, column));
    AbstractTile startingTileVertical = gameBoard.getTile(new Position(row + 2, column + 1));

    Wall horizontalWall = new Wall(HORIZONTAL, startingTileHorizontal);
    Wall verticalWall = new Wall(VERTICAL, startingTileVertical);

    wallPlacer.execute(game, verticalWall);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, horizontalWall);
    Assertions.assertEquals(QuoridorCheckResult.OKAY, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"6, 1", "2, 5", "3, 2"})
  void horizontalWallsOverlappingIsNotAllowedFirstCase(int row, int column) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));

    Wall wall = new Wall(HORIZONTAL, startingTile);
    wallPlacer.execute(game, wall);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.INVALID_WALL_POSITION, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"2, 3", "1, 4", "4, 6"})
  void horizontalWallsOverlappingIsNotAllowedSecondCase(int row, int column) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    AbstractTile startingTileFirst = gameBoard.getTile(new Position(row, column + 1));
    AbstractTile startingTileSecond = gameBoard.getTile(new Position(row, column));

    Wall firstWall = new Wall(HORIZONTAL, startingTileFirst);
    Wall secondWall = new Wall(HORIZONTAL, startingTileSecond);
    wallPlacer.execute(game, firstWall);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, secondWall);
    Assertions.assertEquals(QuoridorCheckResult.INVALID_WALL_POSITION, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"2, 3", "4, 4", "3, 5"})
  void horizontalWallsNearEachOtherIsAllowed(int row, int column) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTileFirst = gameBoard.getTile(new Position(row, column + 2));
    AbstractTile startingTileSecond = gameBoard.getTile(new Position(row, column - 2));
    AbstractTile startingTileThird = gameBoard.getTile(new Position(row, column));

    Wall firstWall = new Wall(HORIZONTAL, startingTileFirst);
    Wall secondWall = new Wall(HORIZONTAL, startingTileSecond);
    Wall thirdWall = new Wall(HORIZONTAL, startingTileThird);
    wallPlacer.execute(game, firstWall);
    wallPlacer.execute(game, secondWall);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, thirdWall);
    Assertions.assertEquals(QuoridorCheckResult.OKAY, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"0, 0", "4, 0", "0, 7"})
  void verticalWallIsNotAllowed(int row, int column) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    Wall wall = new Wall(VERTICAL, startingTile);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.INVALID_WALL_POSITION, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"8, 8", "4, 4", "1, 8"})
  void verticalWallIsAllowed(int row, int column) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    Wall wall = new Wall(VERTICAL, startingTile);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.OKAY, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "5, 7", "1, 7"})
  void verticalWallCrossingHorizontalWallIsNotAllowed(int row, int column) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTileHorizontal = gameBoard.getTile(new Position(row - 1, column - 1));
    AbstractTile startingTileVertical = gameBoard.getTile(new Position(row, column));

    Wall horizontalWall = new Wall(HORIZONTAL, startingTileHorizontal);
    Wall verticalWall = new Wall(VERTICAL, startingTileVertical);

    wallPlacer.execute(game, horizontalWall);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, verticalWall);
    Assertions.assertEquals(QuoridorCheckResult.INVALID_WALL_POSITION, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"3, 4", "3, 3", "2, 4"})
  void verticalWallNextToHorizontalWallIsAllowed(int row, int column) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTileHorizontal = gameBoard.getTile(new Position(row - 1, column - 2));
    AbstractTile startingTileVertical = gameBoard.getTile(new Position(row, column));

    Wall horizontalWall = new Wall(HORIZONTAL, startingTileHorizontal);
    Wall verticalWall = new Wall(VERTICAL, startingTileVertical);

    wallPlacer.execute(game, horizontalWall);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, verticalWall);
    Assertions.assertEquals(QuoridorCheckResult.OKAY, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"1, 4", "7, 2", "4, 6"})
  void verticalWallsOverlappingIsNotAllowedFirstCase(int row, int column) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));

    Wall wall = new Wall(VERTICAL, startingTile);
    wallPlacer.execute(game, wall);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.INVALID_WALL_POSITION, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"5, 4", "5, 1", "6, 3"})
  void verticalWallsOverlappingIsNotAllowedSecondCase(int row, int column) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTileFirst = gameBoard.getTile(new Position(row - 1, column));
    AbstractTile startingTileSecond = gameBoard.getTile(new Position(row, column));

    Wall firstWall = new Wall(VERTICAL, startingTileFirst);
    Wall secondWall = new Wall(VERTICAL, startingTileSecond);
    wallPlacer.execute(game, firstWall);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, secondWall);
    Assertions.assertEquals(QuoridorCheckResult.INVALID_WALL_POSITION, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"6, 2", "3, 5", "5, 1"})
  void verticalWallsNearEachOtherIsAllowed(int row, int column) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTileFirst = gameBoard.getTile(new Position(row + 2, column));
    AbstractTile startingTileSecond = gameBoard.getTile(new Position(row - 2, column));
    AbstractTile startingTileThird = gameBoard.getTile(new Position(row, column));

    Wall firstWall = new Wall(VERTICAL, startingTileFirst);
    Wall secondWall = new Wall(VERTICAL, startingTileSecond);
    Wall thirdWall = new Wall(VERTICAL, startingTileThird);
    wallPlacer.execute(game, firstWall);
    wallPlacer.execute(game, secondWall);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, thirdWall);
    Assertions.assertEquals(QuoridorCheckResult.OKAY, checkPath);
  }

  @ParameterizedTest
  @CsvSource({"3, 3", "6, 2", "0, 0"})
  void horizontalWallNotIsAllowed_IfZeroWallsRemaining(int row, int column) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    for (int i = 0; i < 5; i++)
      game.getPlayingPawn().decrementNumberOfWalls();

    AbstractTile startingTile = gameBoard.getTile(new Position(row, column));
    Wall wall = new Wall(HORIZONTAL, startingTile);

    CheckResult checkPath = wallPlacementChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.END_OF_AVAILABLE_WALLS, checkPath);
  }
}
