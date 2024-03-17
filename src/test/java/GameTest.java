import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

public class GameTest {
  @Test
  void playingPawnAtBeginningTest() {
    GameBoard gameBoard = new GameBoard();
    Pawn pawn1 = new Pawn(gameBoard.getGameState()[0][4], Color.black, 10);
    Pawn pawn2 = new Pawn(gameBoard.getGameState()[8][4], Color.red, 10);
    List<Pawn> pawns = List.of(pawn1, pawn2);
    Game game = new Game(pawns, gameBoard);

    Assertions.assertEquals(pawn1, game.getPlayingPawn());
  }

  @Test
  void changeRoundOnceTest() {
    GameBoard gameBoard = new GameBoard();
    Pawn pawn1 = new Pawn(gameBoard.getGameState()[0][4], Color.black, 10);
    Pawn pawn2 = new Pawn(gameBoard.getGameState()[8][4], Color.red, 10);
    List<Pawn> pawns = List.of(pawn1, pawn2);
    Game game = new Game(pawns, gameBoard);

    game.changeRound();

    Assertions.assertEquals(pawn2, game.getPlayingPawn());
  }

  @Test
  void changeRound_backToFirstPawnTest() {
    GameBoard gameBoard = new GameBoard();
    Pawn pawn1 = new Pawn(gameBoard.getGameState()[0][4], Color.black, 10);
    Pawn pawn2 = new Pawn(gameBoard.getGameState()[8][4], Color.red, 10);
    List<Pawn> pawns = List.of(pawn1, pawn2);
    Game game = new Game(pawns, gameBoard);

    game.changeRound();
    game.changeRound();

    Assertions.assertEquals(pawn1, game.getPlayingPawn());
  }
}
