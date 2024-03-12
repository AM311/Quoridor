import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.Directions.Direction.*;

public class PathTest {
  private Game initialize() {                 //todo OK???
    GameBoard gameBoard = new GameBoard();
    Pawn pawn1 = new Pawn(gameBoard.getGameState()[0][4], Color.black, 10);
    Pawn pawn2 = new Pawn(gameBoard.getGameState()[8][4], Color.red, 10);
    java.util.List<Pawn> pawns = List.of(pawn1, pawn2);
    return new Game(pawns, gameBoard);
  }

  //-------------------------

  @Test
  void checkStartPawn() {
    Game game = initialize();
    Assertions.assertTrue(game.pathExists());
  }

  @Test
  void checkBlockedPath() {
    Game game = initialize();

    for (GameBoard.Tile tile : game.getGameBoard().getGameState()[4]) {
      tile.setLink(DOWN, WALL);
    }
    for (GameBoard.Tile tile : game.getGameBoard().getGameState()[5]) {
      tile.setLink(UP, WALL);
    }
    Assertions.assertFalse(game.pathExists());
  }

  @Test
  void checkBlockedPath_OnlyForPawn1() {
    Game game = initialize();

    for (int i = 0; i < 7; i++) {
      game.getGameBoard().getGameState()[4][i].setLink(DOWN, WALL);
      game.getGameBoard().getGameState()[5][i].setLink(UP, WALL);
    }

    for (int i = 0; i < 5; i++) {
      game.getGameBoard().getGameState()[i][6].setLink(RIGHT, WALL);
      game.getGameBoard().getGameState()[i][7].setLink(LEFT, WALL);
    }

    Assertions.assertFalse(game.pathExists());
  }

  @Test
  void checkBlockedPath_OnlyForPawn2() {
    Game game = initialize();

    for (int i = 0; i < 7; i++) {
      game.getGameBoard().getGameState()[4][i].setLink(DOWN, WALL);
      game.getGameBoard().getGameState()[5][i].setLink(UP, WALL);
    }

    for (int i = 5; i < 9; i++) {
      game.getGameBoard().getGameState()[i][6].setLink(RIGHT, WALL);
      game.getGameBoard().getGameState()[i][7].setLink(LEFT, WALL);
    }

    Assertions.assertFalse(game.pathExists());
  }

}
