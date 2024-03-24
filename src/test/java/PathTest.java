import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.movemanager.PathExistenceChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.Directions.Direction.*;

public class PathTest {
  //todo REPLACE TEST CODE FOR PLACING WALLS WITH A METHOD CALL TO "PLACE WALL"?

  //-------------------------

  @Test
  void checkStartPawn() {
    Game game = new Game(2);
    Assertions.assertTrue(new PathExistenceChecker().checkAction(game, game.getGameBoard()));
  }

  @Test
  void checkBlockedPath() {
    Game game = new Game(2);

    for (GameBoard.Tile tile : game.getGameBoard().getGameState()[4]) {
      tile.setLink(DOWN, WALL);
    }
    for (GameBoard.Tile tile : game.getGameBoard().getGameState()[5]) {
      tile.setLink(UP, WALL);
    }
    Assertions.assertFalse(new PathExistenceChecker().checkAction(game, game.getGameBoard()));
  }

  @Test
  void checkBlockedPawnsFromCorrectSide() {
    Game game = new Game(2);

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

    for (GameBoard.Tile tile : game.getGameBoard().getGameState()[4]) {
      tile.setLink(DOWN, WALL);
    }
    for (GameBoard.Tile tile : game.getGameBoard().getGameState()[5]) {
      tile.setLink(UP, WALL);
    }
    Assertions.assertTrue(new PathExistenceChecker().checkAction(game, game.getGameBoard()));
  }

  @Test
  void checkBlockedPath_OnlyForPawn1() {
    Game game = new Game(2);

    for (int i = 0; i < 7; i++) {
      game.getGameBoard().getGameState()[4][i].setLink(DOWN, WALL);
      game.getGameBoard().getGameState()[5][i].setLink(UP, WALL);
    }

    for (int i = 0; i < 5; i++) {
      game.getGameBoard().getGameState()[i][6].setLink(RIGHT, WALL);
      game.getGameBoard().getGameState()[i][7].setLink(LEFT, WALL);
    }

    Assertions.assertFalse(new PathExistenceChecker().checkAction(game, game.getGameBoard()));
  }

  @Test
  void checkBlockedPath_OnlyForPawn2() {
    Game game = new Game(2);

    for (int i = 0; i < 7; i++) {
      game.getGameBoard().getGameState()[4][i].setLink(DOWN, WALL);
      game.getGameBoard().getGameState()[5][i].setLink(UP, WALL);
    }

    for (int i = 5; i < 9; i++) {
      game.getGameBoard().getGameState()[i][6].setLink(RIGHT, WALL);
      game.getGameBoard().getGameState()[i][7].setLink(LEFT, WALL);
    }

    Assertions.assertFalse(new PathExistenceChecker().checkAction(game, game.getGameBoard()));
  }

}
