

import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.CheckResult;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.QuoridorCheckResult;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.PathExistenceChecker;
import it.units.sdm.quoridor.model.movemanagement.actions.PawnMover;
import it.units.sdm.quoridor.model.movemanagement.actions.WallPlacer;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;

public class PathExistenceCheckerTest {

  private final ActionChecker<Wall> pathExistenceChecker = new PathExistenceChecker();
  private final WallPlacer wallPlacer = new WallPlacer();
  private final PawnMover pawnMover = new PawnMover();

  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StandardQuoridorBuilder(4));
    return builderDirector.makeGame();
  }


  private void fillRowWithWalls(AbstractGame game, AbstractGameBoard gameBoard, int row) throws InvalidParameterException, InvalidActionException {
    int[][] tileCoordinates = {
            {row, 0}, {row, 2}, {row, 4}, {row, 6}
    };

    for (int[] tileCoordinate : tileCoordinates) {
      AbstractTile tile = gameBoard.getTile(new Position(tileCoordinate[0], tileCoordinate[1]));
      Wall wall = new Wall(HORIZONTAL, tile);
      wallPlacer.execute(game, wall);
    }
  }

  private void fillColumnWithWalls(AbstractGame game, AbstractGameBoard gameBoard, int column) throws InvalidParameterException, InvalidActionException {
    int[][] tileCoordinates = {
            {1, column}, {3, column}, {5, column}, {7, column}
    };

    for (int[] tileCoordinate : tileCoordinates) {
      AbstractTile tile = gameBoard.getTile(new Position(tileCoordinate[0], tileCoordinate[1]));
      Wall wall = new Wall(VERTICAL, tile);
      wallPlacer.execute(game, wall);
    }
  }

  @Test
  void checkWithFreeGameBoard() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(3, 4));
    Wall wall = new Wall(HORIZONTAL, startingTile);

    //Assertions.assertTrue(pathExistenceChecker.isValidAction(game, wall));
    CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.OKAY, checkPath);
  }

  @ParameterizedTest
  @ValueSource(ints = {3, 6, 7})
  void checkBlockedPath_forPawns1And2(int row) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    fillRowWithWalls(game, gameBoard, row);
    game.changeRound();
    wallPlacer.execute(game, new Wall(VERTICAL, gameBoard.getTile(new Position(row, 8))));

    AbstractTile startingTile = gameBoard.getTile(new Position(row - 2, 7));

    Wall wall = new Wall(HORIZONTAL, startingTile);

     CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.BLOCKING_WALL, checkPath); //NON SONO SICURA DI BLOCKING_WALLS
  }


  @ParameterizedTest
  @ValueSource(ints = {3, 4, 6})
  void checkBlockedPawns1And2_FromCorrectSide(int row) throws InvalidParameterException, BuilderException, InvalidActionException, OutOfGameBoardException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    while (game.getPlayingPawn().getCurrentTile().getRow() < 7) {
      pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow() + 1, game.getPlayingPawn().getCurrentTile().getColumn())));
    }

    game.changeRound();

    pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow(), game.getPlayingPawn().getCurrentTile().getColumn() - 1)));

    while (game.getPlayingPawn().getCurrentTile().getRow() > 1) {
      pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow() - 1, game.getPlayingPawn().getCurrentTile().getColumn())));
    }

    fillRowWithWalls(game, gameBoard, row);
    game.changeRound();
    wallPlacer.execute(game, new Wall(VERTICAL, gameBoard.getTile(new Position(row, 8))));

    AbstractTile startingTile = gameBoard.getTile(new Position(row - 2, 7));
    Wall wall = new Wall(HORIZONTAL, startingTile);

     CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.OKAY, checkPath);
  }


  @Test
  void checkBlockedPath_OnlyForPawn1() throws InvalidParameterException, BuilderException, InvalidActionException, OutOfGameBoardException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    fillRowWithWalls(game, gameBoard, 1);
    game.changeRound();
    Wall wall = new Wall(VERTICAL, gameBoard.getTile(new Position(1, 8)));

    //Assertions.assertFalse(pathExistenceChecker.isValidAction(game, wall));
     CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.BLOCKING_WALL, checkPath);

  }


  @Test
  void checkBlockedPath_OnlyForPawn2() throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    fillRowWithWalls(game, gameBoard, 6);
    game.changeRound();

    Wall wall = new Wall(VERTICAL, gameBoard.getTile(new Position(8, 8)));

     CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.BLOCKING_WALL, checkPath);
  }

  @Test
  void checkBlockedPath_OnlyForPawn1_whenOnSameSide() throws InvalidParameterException, BuilderException, InvalidActionException, OutOfGameBoardException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    game.changeRound();

    pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow(), game.getPlayingPawn().getCurrentTile().getColumn() - 1)));

    while (game.getPlayingPawn().getCurrentTile().getRow() > 1) {
      pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow() - 1, game.getPlayingPawn().getCurrentTile().getColumn())));
    }

    fillRowWithWalls(game, gameBoard, 1);
    game.changeRound();
    Wall wall = new Wall(VERTICAL, gameBoard.getTile(new Position(1, 8)));

    CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.BLOCKING_WALL, checkPath);

  }


  @Test
  void checkBlockedPath_OnlyForPawn2_whenOnSameSide() throws InvalidParameterException, BuilderException, InvalidActionException, OutOfGameBoardException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();


    pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow(), game.getPlayingPawn().getCurrentTile().getColumn() - 1)));

    while (game.getPlayingPawn().getCurrentTile().getRow() < 7) {
      pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow() + 1, game.getPlayingPawn().getCurrentTile().getColumn())));
    }

    fillRowWithWalls(game, gameBoard, 6);
    game.changeRound();
    Wall wall = new Wall(VERTICAL, gameBoard.getTile(new Position(8, 8)));

    CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.BLOCKING_WALL, checkPath);

  }

  @ParameterizedTest
  @ValueSource(ints = {3, 6, 7})
  void checkBlockedPath_forPawns3And4(int column) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    fillColumnWithWalls(game, gameBoard, column);
    game.changeRound();
    wallPlacer.execute(game, new Wall(VERTICAL, gameBoard.getTile(new Position(8, column - 2))));

    AbstractTile startingTile = gameBoard.getTile(new Position(7, column - 2));

    Wall wall = new Wall(HORIZONTAL, startingTile);

    CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.BLOCKING_WALL, checkPath);
  }

  @ParameterizedTest
  @ValueSource(ints = {3, 4, 6})
  void checkBlockedPawns3And4_FromCorrectSide(int column) throws InvalidParameterException, BuilderException, InvalidActionException, OutOfGameBoardException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    game.changeRound();
    game.changeRound();

    while (game.getPlayingPawn().getCurrentTile().getColumn() < 7) {
      pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow(), game.getPlayingPawn().getCurrentTile().getColumn() + 1)));
    }

    game.changeRound();

    pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow() - 1, game.getPlayingPawn().getCurrentTile().getColumn())));

    while (game.getPlayingPawn().getCurrentTile().getColumn() > 1) {
      pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow(), game.getPlayingPawn().getCurrentTile().getColumn() - 1)));
    }

    fillColumnWithWalls(game, gameBoard, column);
    game.changeRound();
    wallPlacer.execute(game, new Wall(HORIZONTAL, gameBoard.getTile(new Position(7, column))));

    AbstractTile startingTile = gameBoard.getTile(new Position(8, column + 2));
    Wall wall = new Wall(VERTICAL, startingTile);

    CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.OKAY, checkPath);
  }


  @Test
  void pawn1ClosedInABox_wrongSide() throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    wallPlacer.execute(game, new Wall(VERTICAL, gameBoard.getTile(new Position(1, 3))));
    wallPlacer.execute(game, new Wall(VERTICAL, gameBoard.getTile(new Position(1, 5))));

    Wall wall = new Wall(HORIZONTAL, gameBoard.getTile(new Position(1, 3)));

    CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.BLOCKING_WALL, checkPath);
  }

  @Test
  void pawn2ClosedInABox_correctSide() throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    game.changeRound();

    pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow(), game.getPlayingPawn().getCurrentTile().getColumn() - 1)));

    while (game.getPlayingPawn().getCurrentTile().getRow() > 0) {
      pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow() - 1, game.getPlayingPawn().getCurrentTile().getColumn())));
    }

    wallPlacer.execute(game, new Wall(VERTICAL, gameBoard.getTile(new Position(1, 5))));
    wallPlacer.execute(game, new Wall(VERTICAL, gameBoard.getTile(new Position(1, 7))));

    Wall wall = new Wall(HORIZONTAL, gameBoard.getTile(new Position(1, 5)));

    CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.OKAY, checkPath);
  }

  @Test
  void pawn3And4ClosedInABox_3onCorrectSide_4onWrongSide() throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    game.changeRound();
    game.changeRound();

    pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow() - 1, game.getPlayingPawn().getCurrentTile().getColumn())));

    while (game.getPlayingPawn().getCurrentTile().getColumn() < 8) {
      pawnMover.execute(game, gameBoard.getTile(new Position(game.getPlayingPawn().getCurrentTile().getRow(), game.getPlayingPawn().getCurrentTile().getColumn() + 1)));
    }

    wallPlacer.execute(game, new Wall(HORIZONTAL, gameBoard.getTile(new Position(2, 7))));
    wallPlacer.execute(game, new Wall(HORIZONTAL, gameBoard.getTile(new Position(4, 7))));

    Wall wall = new Wall(VERTICAL, gameBoard.getTile(new Position(4, 7)));

    CheckResult checkPath = pathExistenceChecker.isValidAction(game, wall);
    Assertions.assertEquals(QuoridorCheckResult.BLOCKING_WALL, checkPath);

  }

}


