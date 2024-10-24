import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class GameTest {
  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    return builderDirector.makeGame();
  }
  @Test
  void changeRoundOnceTest() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();

    game.changeRound();
    Assertions.assertEquals(game.getPawns().getLast(), game.getPlayingPawn());
  }

  @Test
  void changeRoundTwiceTest() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();

    game.changeRound();
    game.changeRound();
    Assertions.assertEquals(game.getPawns().getFirst(), game.getPlayingPawn());
  }
  @ParameterizedTest
  @CsvSource({"8,4", "8,5", "8,1", "8,5"})
  void hasReachedDestinationTest_destinationHasBeenReached(int testTileRow, int testTileColumn) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractPawn pawn = game.getPlayingPawn();

    Position currentTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile currentTile = game.getGameBoard().getTile(currentTilePosition);
    pawn.move(currentTile);
    Assertions.assertTrue(game.isGameFinished());
  }

  @ParameterizedTest
  @CsvSource({"2,4", "3,5", "1,1", "7,5"})
  void hasReachedDestinationTest_destinationHasNotBeenReached(int testTileRow, int testTileColumn) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractPawn pawn = game.getPlayingPawn();

    Position currentTilePosition = new Position(testTileRow, testTileColumn);
    AbstractTile currentTile = game.getGameBoard().getTile(currentTilePosition);
    pawn.move(currentTile);
    Assertions.assertFalse(game.isGameFinished());
  }

  @Test
  void cloneTest_cloneEqualToNewGame() throws CloneNotSupportedException, InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    Assertions.assertEquals(game, game.clone());
  }
}
