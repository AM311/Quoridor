

import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.movemanagement.actioncheckers.PathExistenceChecker;
import it.units.sdm.quoridor.movemanagement.actions.PawnMover;
import it.units.sdm.quoridor.movemanagement.actions.WallPlacer;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;
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
    BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    return builderDirector.makeGame();
  }

  private void fillRowWithWalls(AbstractGame game, AbstractGameBoard gameBoard, int row) throws InvalidParameterException, InvalidActionException {
    int[][] tileCoordinates = {
            {row, 0}, {row, 2}, {row, 4}, {row, 6}
    };
    WallOrientation[] orientations = {
            HORIZONTAL, HORIZONTAL, HORIZONTAL, HORIZONTAL
    };

    for (int i = 0; i < tileCoordinates.length; i++) {
      AbstractTile tile = gameBoard.getTile(new Position(tileCoordinates[i][0], tileCoordinates[i][1]));
      Wall wall = new Wall(orientations[i], tile);
      wallPlacer.execute(game, wall);
    }
  }

  @Test
  void checkWithFreeGameBoard() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile startingTile = gameBoard.getTile(new Position(3, 4));
    Wall wall = new Wall(HORIZONTAL, startingTile);

    Assertions.assertTrue(pathExistenceChecker.isValidAction(game, wall));
  }

  @ParameterizedTest
  @ValueSource(ints = {3, 6, 7})
  void checkBlockedPath(int row) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    fillRowWithWalls(game, gameBoard, row);
    wallPlacer.execute(game, new Wall(VERTICAL, gameBoard.getTile(new Position(row , 8))));

    AbstractTile startingTile = gameBoard.getTile(new Position(row - 2, 7));

    Wall wall = new Wall(HORIZONTAL, startingTile);

    Assertions.assertFalse(pathExistenceChecker.isValidAction(game, wall));
  }



  @ParameterizedTest
  @ValueSource(ints = {3, 4, 6})
  void checkBlockedPawnsFromCorrectSide(int row) throws InvalidParameterException, BuilderException, InvalidActionException, OutOfGameBoardException {
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
    wallPlacer.execute(game, new Wall(VERTICAL, gameBoard.getTile(new Position(row , 8))));

    AbstractTile startingTile = gameBoard.getTile(new Position(row - 2, 7));
    Wall wall = new Wall(HORIZONTAL, startingTile);

    Assertions.assertTrue(new PathExistenceChecker().isValidAction(game, wall));
  }



  @Test
  void checkBlockedPath_OnlyForPawn1() throws InvalidParameterException, BuilderException, InvalidActionException, OutOfGameBoardException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    fillRowWithWalls(game, gameBoard, 1);

    Wall wall = new Wall(VERTICAL, gameBoard.getTile(new Position(1, 8)));

    Assertions.assertFalse(new PathExistenceChecker().isValidAction(game, wall));

  }


  @Test
  void checkBlockedPath_OnlyForPawn2() throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();

    fillRowWithWalls(game, gameBoard, 6);

    Wall wall = new Wall(VERTICAL, gameBoard.getTile(new Position(8, 8)));

    Assertions.assertFalse(new PathExistenceChecker().isValidAction(game, wall));
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
    Wall wall = new Wall(VERTICAL, gameBoard.getTile(new Position(1, 8)));

    Assertions.assertFalse(new PathExistenceChecker().isValidAction(game, wall));

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
    Wall wall = new Wall(VERTICAL, gameBoard.getTile(new Position(8, 8)));

    Assertions.assertFalse(new PathExistenceChecker().isValidAction(game, wall));

  }

}


