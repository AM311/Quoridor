import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.NumberOfWallsBelowZeroException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PawnTest {
  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    return builderDirector.makeGame();
  }

  @ParameterizedTest
  @CsvSource({"3,4", "7,5", "6,2", "5,5"})
  void afterMoveDestinationTileIsConsistentWithCurrentTile(int destinationTileRow, int destinationTileColumn) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractPawn pawn = game.getPlayingPawn();

    Position destinationTilePosition = new Position(destinationTileRow, destinationTileColumn);
    AbstractTile destinationTile = game.getGameBoard().getTile(destinationTilePosition);
    pawn.move(destinationTile);
    Assertions.assertEquals(destinationTile, pawn.getCurrentTile());
  }

  @ParameterizedTest
  @CsvSource({"10", "5", "3"})
  void decrementNumberOfWallsTest_decrementIsCorrectlyExecuted(int numberOfWalls) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractPawn pawn = game.getPlayingPawn();

    for (int i = 10; i >= numberOfWalls; i--) {
      pawn.decrementNumberOfWalls();
    }
    int expected = numberOfWalls - 1;
    int actual = pawn.getNumberOfWalls();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void decrementNumberOfWallsTest_exceptionIsCorrectlyThrown() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractPawn pawn = game.getPlayingPawn();

    for(int i = 0; i < 10; i++) {
      pawn.decrementNumberOfWalls();
    }
    Assertions.assertThrows(NumberOfWallsBelowZeroException.class,
            pawn::decrementNumberOfWalls);
  }
}
