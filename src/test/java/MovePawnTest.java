import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.Tile;
import it.units.sdm.quoridor.model.builder.IQuoridorBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MovePawnTest {
  @ParameterizedTest
  @CsvSource({"7, 1, 7, 2", "4, 3, 5, 3", "3, 1, 2, 1", "5, 6, 5, 5"})
  void movePawnVerticallyAndHorizontally(int startingRow, int startingColumn, int destinationRow, int destinationColumn) throws InvalidActionException, InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Tile startingTile = game.getGameBoard().getTile(startingRow, startingColumn);
    Tile destinationTile = game.getGameBoard().getTile(destinationRow, destinationColumn);
    game.getPlayingPawn().move(startingTile);
    startingTile.setOccupiedBy(true);
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalUpRight() throws InvalidActionException, InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Tile startingTile = game.getGameBoard().getTile(1,1);
    Tile destinationTile = game.getGameBoard().getTile(0,2);
    game.getPlayingPawn().move(startingTile);
    startingTile.setOccupiedBy(true);
    game.changeRound();
    game.getPlayingPawn().move(game.getGameBoard().getTile(0,1));
    game.getGameBoard().getTile(0,1).setOccupiedBy(true);
    game.changeRound();
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalUpLeft() throws InvalidActionException, InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Tile startingTile = game.getGameBoard().getTile(1,1);
    Tile destinationTile = game.getGameBoard().getTile(0,0);
    game.getPlayingPawn().move(startingTile);
    startingTile.setOccupiedBy(true);
    game.changeRound();
    game.getPlayingPawn().move(game.getGameBoard().getTile(0,1));
    game.getGameBoard().getTile(0,1).setOccupiedBy(true);
    game.changeRound();
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalDownRight() throws InvalidActionException, InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Tile startingTile = game.getGameBoard().getTile(4,7);
    Tile destinationTile = game.getGameBoard().getTile(5,8);
    game.getPlayingPawn().move(startingTile);
    startingTile.setOccupiedBy(true);
    game.changeRound();
    game.getPlayingPawn().move(game.getGameBoard().getTile(4,8));
    game.getGameBoard().getTile(4,8).setOccupiedBy(true);
    game.changeRound();
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }

  @Test
  void movePawnDiagonalDownLeft() throws InvalidActionException, InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Tile startingTile = game.getGameBoard().getTile(4,1);
    Tile destinationTile = game.getGameBoard().getTile(5,0);
    game.getPlayingPawn().move(startingTile);
    startingTile.setOccupiedBy(true);
    game.changeRound();
    game.getPlayingPawn().move(game.getGameBoard().getTile(4,0));
    game.getGameBoard().getTile(4,0).setOccupiedBy(true);
    game.changeRound();
    game.movePlayingPawn(destinationTile);
    Assertions.assertTrue(!startingTile.isOccupied()
            && destinationTile.isOccupied()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }
}
