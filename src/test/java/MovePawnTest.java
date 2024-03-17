import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.model.Wall;
import it.units.sdm.quoridor.movemanager.ActionChecker;
import it.units.sdm.quoridor.movemanager.WallPlacementChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.util.List;

public class MovePawnTest {
  GameBoard gameBoard = new GameBoard();
  GameBoard.Tile tile1 = gameBoard.getGameState()[0][4];
  GameBoard.Tile tile2 = gameBoard.getGameState()[8][4];
  Pawn pawn1 = new Pawn(tile1, Color.black, 10);

  Pawn pawn2 = new Pawn(tile2, Color.black, 10);

  java.util.List<Pawn> pawns = List.of(pawn1, pawn2);
  Game game = new Game(pawns, gameBoard);

  ActionChecker<Wall> wallPlacementChecker = new WallPlacementChecker();

  @ParameterizedTest
  @CsvSource({"7, 1, 7, 2", "4, 3, 5, 3", "3, 1, 2, 1", "5, 6, 5, 5"})
  void movePawnVerticallyAndOrizzontally(int startingRow, int startingColumn, int destinationRow, int destinationColumn) {
    GameBoard.Tile startingTile = gameBoard.getGameState()[startingRow][startingColumn];
    GameBoard.Tile destinationTile = gameBoard.getGameState()[destinationRow][destinationColumn];
    pawn1.setCurrentTile(startingTile);
    startingTile.setOccupied(true);
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && pawn1.getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalUpRight(){
    GameBoard.Tile startingTile = gameBoard.getGameState()[1][1];
    GameBoard.Tile destinationTile = gameBoard.getGameState()[0][2];
    pawn1.setCurrentTile(startingTile);
    startingTile.setOccupied(true);
    pawn2.setCurrentTile(gameBoard.getGameState()[0][1]);
    gameBoard.getGameState()[0][1].setOccupied(true);
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && pawn1.getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalUpLeft(){
    GameBoard.Tile startingTile = gameBoard.getGameState()[1][1];
    GameBoard.Tile destinationTile = gameBoard.getGameState()[0][0];
    pawn1.setCurrentTile(startingTile);
    startingTile.setOccupied(true);
    pawn2.setCurrentTile(gameBoard.getGameState()[0][1]);
    gameBoard.getGameState()[0][1].setOccupied(true);
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && pawn1.getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalDownRight(){
    GameBoard.Tile startingTile = gameBoard.getGameState()[4][7];
    GameBoard.Tile destinationTile = gameBoard.getGameState()[5][8];
    pawn1.setCurrentTile(startingTile);
    startingTile.setOccupied(true);
    pawn2.setCurrentTile(gameBoard.getGameState()[4][8]);
    gameBoard.getGameState()[4][8].setOccupied(true);
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && pawn1.getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalDownLeft(){
    GameBoard.Tile startingTile = gameBoard.getGameState()[4][1];
    GameBoard.Tile destinationTile = gameBoard.getGameState()[5][0];
    pawn1.setCurrentTile(startingTile);
    startingTile.setOccupied(true);
    pawn2.setCurrentTile(gameBoard.getGameState()[4][0]);
    gameBoard.getGameState()[4][0].setOccupied(true);
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && pawn1.getCurrentTile().equals(destinationTile));
  }
}
