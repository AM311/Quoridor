import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MovePawnTest {
  @ParameterizedTest
  @CsvSource({"7, 1, 7, 2", "4, 3, 5, 3", "3, 1, 2, 1", "5, 6, 5, 5"})
  void movePawnVerticallyAndHorizontally(int startingRow, int startingColumn, int destinationRow, int destinationColumn) {
    Game game = new Game(2);
    Tile startingTile = game.getGameBoard().getGameState()[startingRow][startingColumn];
    Tile destinationTile = game.getGameBoard().getGameState()[destinationRow][destinationColumn];
    game.getPlayingPawn().move(startingTile);
    startingTile.setOccupied(true);
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalUpRight(){
    Game game = new Game(2);
    Tile startingTile = game.getGameBoard().getGameState()[1][1];
    Tile destinationTile = game.getGameBoard().getGameState()[0][2];
    game.getPlayingPawn().move(startingTile);
    startingTile.setOccupied(true);
    game.changeRound();
    game.getPlayingPawn().move(game.getGameBoard().getGameState()[0][1]);
    game.getGameBoard().getGameState()[0][1].setOccupied(true);
    game.changeRound();
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalUpLeft(){
    Game game = new Game(2);
    Tile startingTile = game.getGameBoard().getGameState()[1][1];
    Tile destinationTile = game.getGameBoard().getGameState()[0][0];
    game.getPlayingPawn().move(startingTile);
    startingTile.setOccupied(true);
    game.changeRound();
    game.getPlayingPawn().move(game.getGameBoard().getGameState()[0][1]);
    game.getGameBoard().getGameState()[0][1].setOccupied(true);
    game.changeRound();
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalDownRight(){
    Game game = new Game(2);
    Tile startingTile = game.getGameBoard().getGameState()[4][7];
    Tile destinationTile = game.getGameBoard().getGameState()[5][8];
    game.getPlayingPawn().move(startingTile);
    startingTile.setOccupied(true);
    game.changeRound();
    game.getPlayingPawn().move(game.getGameBoard().getGameState()[4][8]);
    game.getGameBoard().getGameState()[4][8].setOccupied(true);
    game.changeRound();
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalDownLeft(){
    Game game = new Game(2);
    Tile startingTile = game.getGameBoard().getGameState()[4][1];
    Tile destinationTile = game.getGameBoard().getGameState()[5][0];
    game.getPlayingPawn().move(startingTile);
    startingTile.setOccupied(true);
    game.changeRound();
    game.getPlayingPawn().move(game.getGameBoard().getGameState()[4][0]);
    game.getGameBoard().getGameState()[4][0].setOccupied(true);
    game.changeRound();
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }
}
