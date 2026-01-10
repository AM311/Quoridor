import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import it.units.sdm.quoridor.model.movemanagement.actions.PawnMover;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


public class MovePawnTest {

  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StandardQuoridorBuilder(2));
    return builderDirector.makeGame();
  }


  @ParameterizedTest
  @CsvSource({"7, 4" ,"6, 4", "5, 4"})
  void destinationTilesAreConsistent(int destinationRow, int destinationColumn) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();

    Position destinationPosition = new Position(destinationRow, destinationColumn);
    AbstractTile destinationTile = game.getGameBoard().getTile(destinationPosition);

    PawnMover pawnMover = new PawnMover();
    pawnMover.execute(game, destinationTile);

    Assertions.assertEquals(destinationTile, game.getPlayingPawn().getCurrentTile());
  }

  @ParameterizedTest
  @CsvSource({"3, 4" ,"5, 4", "1, 4"})
  void startingTileIsCorrectlyEmptyAfterMovement(int destinationRow, int destinationColumn) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();

    Position destinationPosition = new Position(destinationRow, destinationColumn);
    AbstractTile startingTile = game.getPlayingPawn().getCurrentTile();
    AbstractTile destinationTile = game.getGameBoard().getTile(destinationPosition);

    PawnMover pawnMover = new PawnMover();
    pawnMover.execute(game, destinationTile);

    Assertions.assertTrue(startingTile.isOccupiedBy().isEmpty());
  }

  @ParameterizedTest
  @CsvSource({"7, 4" ,"6, 4", "5, 4"})
  void destinationTileIsCorrectlyOccupiedBy(int destinationRow, int destinationColumn) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();

    Position destinationPosition = new Position(destinationRow, destinationColumn);
    AbstractTile destinationTile = game.getGameBoard().getTile(destinationPosition);

    PawnMover pawnMover = new PawnMover();
    pawnMover.execute(game, destinationTile);

    Assertions.assertEquals(game.getPlayingPawn(), destinationTile.isOccupiedBy().orElseThrow());
  }
}