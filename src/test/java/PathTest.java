import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.Tile;
import it.units.sdm.quoridor.model.builder.IQuoridorBuilder;
import it.units.sdm.quoridor.movemanagement.actioncheckers.PathExistenceChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class PathTest {
  //todo REPLACE TEST CODE FOR PLACING WALLS WITH A METHOD CALL TO "PLACE WALL"?

  //-------------------------

  @Test
  void checkStartPawn() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Assertions.assertTrue(new PathExistenceChecker().isValidAction(game, game.getGameBoard()));
  }

  @Test
  void checkBlockedPath() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();

    for (Tile tile : game.getGameBoard().getRowTiles(4)) {
      tile.setLink(DOWN, WALL);
    }
    for (Tile tile : game.getGameBoard().getRowTiles(5)) {
      tile.setLink(UP, WALL);
    }
    Assertions.assertFalse(new PathExistenceChecker().isValidAction(game, game.getGameBoard()));
  }

  @Test
  void checkBlockedPawnsFromCorrectSide() throws InvalidActionException, OutOfGameBoardException, InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();

    //todo CHIAMATA PER MOSSA TROPPO VERBOSA... VALUTARE!!!
    for (int j = 0; j < 6; j++) {
      game.movePlayingPawn(game.getGameBoard().getAdjacentTile(game.getPlayingPawn().getCurrentTile(), DOWN));
    }

    game.changeRound();
    game.movePlayingPawn(game.getGameBoard().getAdjacentTile(game.getPlayingPawn().getCurrentTile(), LEFT));

    for (int j = 0; j < 6; j++) {
      game.movePlayingPawn(game.getGameBoard().getAdjacentTile(game.getPlayingPawn().getCurrentTile(), UP));
    }

    //-------------------

    for (Tile tile : game.getGameBoard().getRowTiles(4)) {
      tile.setLink(DOWN, WALL);
    }
    for (Tile tile : game.getGameBoard().getRowTiles(5)) {
      tile.setLink(UP, WALL);
    }
    Assertions.assertTrue(new PathExistenceChecker().isValidAction(game, game.getGameBoard()));
  }

  @Test
  void checkBlockedPath_OnlyForPawn1() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();

    for (int i = 0; i < 7; i++) {
      game.getGameBoard().getTile(4,i).setLink(DOWN, WALL);
      game.getGameBoard().getTile(5,i).setLink(UP, WALL);
    }

    for (int i = 0; i < 5; i++) {
      game.getGameBoard().getTile(i,6).setLink(RIGHT, WALL);
      game.getGameBoard().getTile(i,7).setLink(LEFT, WALL);
    }

    Assertions.assertFalse(new PathExistenceChecker().isValidAction(game, game.getGameBoard()));
  }

  @Test
  void checkBlockedPath_OnlyForPawn2() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();

    for (int i = 0; i < 7; i++) {
      game.getGameBoard().getTile(4,i).setLink(DOWN, WALL);
      game.getGameBoard().getTile(5,i).setLink(UP, WALL);
    }

    for (int i = 5; i < 9; i++) {
      game.getGameBoard().getTile(i,6).setLink(RIGHT, WALL);
      game.getGameBoard().getTile(i,7).setLink(LEFT, WALL);
    }

    Assertions.assertFalse(new PathExistenceChecker().isValidAction(game, game.getGameBoard()));
  }

}
