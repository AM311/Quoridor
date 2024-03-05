import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.model.Player;
import it.units.sdm.quoridor.utils.Direction;

import static it.units.sdm.quoridor.utils.Direction.*;

import it.units.sdm.quoridor.model.GameBoard.LinkState;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.util.List;

public class PathTest {
  GameBoard gameBoard = new GameBoard();
  Pawn pawn1 = new Pawn(gameBoard.getGameState()[0][4], Color.black);
  Player player1 = new Player("Bob", 10, pawn1);
  Pawn pawn2 = new Pawn(gameBoard.getGameState()[8][4], Color.red);
  Player player2 = new Player("Alice", 10, pawn2);
  java.util.List<Player> players = List.of(player1, player2);
  Game game = new Game(players, gameBoard);

  @Test
  void checkStartPawn1() {
    Assertions.assertTrue(game.pathExists(pawn1));
  }

  @Test
  void checkStartPawn2() {
    Assertions.assertTrue(game.pathExists(pawn2));
  }

  @Test
  void checkBlockedPathPawn1() {
    for (GameBoard.Tile tile : gameBoard.getGameState()[4]) {
      tile.setLink(DOWN, WALL);
    }
    for (GameBoard.Tile tile : gameBoard.getGameState()[5]) {
      tile.setLink(UP, WALL);
    }
    Assertions.assertFalse(game.pathExists(pawn1));
  }

  @Test
  void checkBlockedPathPawn2() {
    for (GameBoard.Tile tile : gameBoard.getGameState()[4]) {
      tile.setLink(DOWN, WALL);
    }
    for (GameBoard.Tile tile : gameBoard.getGameState()[5]) {
      tile.setLink(UP, WALL);
    }
    Assertions.assertFalse(game.pathExists(pawn2));
  }
}
