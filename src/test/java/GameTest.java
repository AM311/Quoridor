import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class GameTest {

  @Test
  void testConstructor_invalidNumberOfPlayersThrowsException() {
    Assertions.assertThrows(InvalidParameterException.class, () -> new Game(3));
  }

  @ParameterizedTest
  @CsvSource({"2,10", "4,5"})
  void testConstructor_wallNumberIsConsistent(int numOfPlayers, int numOfWalls) throws InvalidParameterException {
    Game game = new Game(numOfPlayers);
    Assertions.assertEquals(numOfWalls, game.getPlayingPawn().getNumberOfWalls());
  }

  @ParameterizedTest
  @ValueSource(ints = {2,4})
  void testConstructor_pawnsNumberIsConsistent(int numOfPlayers) throws InvalidParameterException {
    Game game = new Game(numOfPlayers);
    Assertions.assertEquals(numOfPlayers, game.getPawns().size());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3})
  void testConstructor_pawnsStartingTilesAreConsistent(int pawnIndex) throws InvalidParameterException {
    Game game = new Game(4);
    Assertions.assertEquals(game.getGameBoard().getStartingAndDestinationTiles().get(pawnIndex).getKey(), game.getPawns().get(pawnIndex).getCurrentTile());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3})
  void testConstructor_pawnsDestinationTilesAreConsistent(int pawnIndex) throws InvalidParameterException {
    Game game = new Game(4);
    Assertions.assertEquals(game.getGameBoard().getStartingAndDestinationTiles().get(pawnIndex).getValue(), game.getPawns().get(pawnIndex).getDestinationTiles());
  }

  @Test
  void testConstructor_startingTilesAreSetOccupied() throws InvalidParameterException {        //todo parametrizzare-separare casi
    Game game = new Game(2);

    Assertions.assertTrue(game.getGameBoard().getTile(0, 4).isOccupied() && game.getGameBoard().getTile(8, 4).isOccupied());
  }

  //------------------------

  @Test
  void playingPawnAtBeginningTest() throws InvalidParameterException {
    Game game = new Game(2);

    Assertions.assertEquals(game.getPawns().getFirst(), game.getPlayingPawn());
  }

  @Test
  void changeRoundOnceTest() throws InvalidParameterException {
    Game game = new Game(2);

    game.changeRound();

    Assertions.assertEquals(game.getPawns().getLast(), game.getPlayingPawn());
  }

  @Test
  void changeRound_backToFirstPawnTest() throws InvalidParameterException {
    Game game = new Game(2);

    game.changeRound();
    game.changeRound();

    Assertions.assertEquals(game.getPawns().getFirst(), game.getPlayingPawn());
  }


  @Test
  void pawnReachesDestinationTilesAndWins() throws InvalidParameterException {

    Game game = new Game(2);
    Pawn pawn = game.getPawns().getFirst();

    for (int i = 0; i < 8; i++) {
      Tile nextTile1 = game.getGameBoard().getTile(pawn.getCurrentTile().getRow()+1, pawn.getCurrentTile().getColumn());
      pawn.move(nextTile1);
    }

    Assertions.assertTrue(game.checkWin());
  }

  @Test
  void pawnDoesNotReachDestinationTilesAndDoesNotWin() throws InvalidParameterException {
    Game game = new Game(2);
    Pawn pawn = game.getPawns().getFirst();

    for (int i = 0; i < 5; i++) {
      Tile nextTile1 = game.getGameBoard().getTile(pawn.getCurrentTile().getRow()+1, pawn.getCurrentTile().getColumn());
      pawn.move(nextTile1);
    }

    Assertions.assertFalse(game.checkWin());
  }

}
