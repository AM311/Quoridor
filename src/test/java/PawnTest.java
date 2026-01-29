import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.NumberOfWallsBelowZeroException;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.abstracts.AbstractPawn;
import it.units.sdm.quoridor.model.abstracts.AbstractTile;
import it.units.sdm.quoridor.view.PawnAppearance;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

public class PawnTest {
  private static AbstractGame buildGame(int numberOfPlayers) throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StandardQuoridorBuilder(numberOfPlayers));
    return builderDirector.makeGame();
  }

  @ParameterizedTest
  @CsvSource({"3,4", "7,5", "6,2", "5,5"})
  void afterMoveDestinationTileIsConsistentWithCurrentTile(int destinationTileRow, int destinationTileColumn) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(2);
    AbstractPawn pawn = game.getPlayingPawn();

    Position destinationTilePosition = new Position(destinationTileRow, destinationTileColumn);
    AbstractTile destinationTile = game.getGameBoard().getTile(destinationTilePosition);
    pawn.move(destinationTile);
    Assertions.assertEquals(destinationTile, pawn.getCurrentTile());
  }

  @ParameterizedTest
  @CsvSource({"10", "5", "3"})
  void decrementNumberOfWallsTest_decrementIsCorrectlyExecuted(int numberOfWalls) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(2);
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
    AbstractGame game = buildGame(2);
    AbstractPawn pawn = game.getPlayingPawn();

    for (int i = 0; i < 10; i++) {
      pawn.decrementNumberOfWalls();
    }
    Assertions.assertThrows(NumberOfWallsBelowZeroException.class,
            pawn::decrementNumberOfWalls);
  }

  @Test
  void defaultPawnsTest_colorsAreAsExpected() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame(4);
    List<PawnAppearance> expectedColor = PawnAppearance.getDefaultPawnStyles();

    for (int i = 0; i < 4; i++) {
      AbstractPawn pawn = game.getPlayingPawn();

      Assertions.assertEquals(expectedColor.get(i).color(), pawn.getPawnAppearance().color());
      game.changeRound();
    }
  }

  @Test
  void defaultPawnsTest_colorNamesAndPathsAreCoherent() throws InvalidParameterException, BuilderException {
    List<String> colorNames = List.of("cyan", "magenta", "green", "red");
    List<String> pawnPaths = List.of("/cyan-pawn.png", "/magenta-pawn.png", "/green-pawn.png", "/red-pawn.png");

    AbstractGame game = buildGame(4);

    for (int i = 0; i < 4; i++) {
      AbstractPawn pawn = game.getPlayingPawn();

      String pawnColorName = pawn.getPawnAppearance().color().getName();
      String pawnPath = pawn.getPawnAppearance().getGuiMarkerPath();

      int index = colorNames.indexOf(pawnColorName.toLowerCase());

      Assertions.assertEquals(pawnPaths.get(index), pawnPath);
      game.changeRound();
    }
  }

  @Test
  void cloneTest_cloneEqualToNewPawn() throws InvalidParameterException, BuilderException, CloneNotSupportedException {
    AbstractGame game = buildGame(2);
    AbstractPawn pawn = game.getPlayingPawn();
    Assertions.assertEquals(pawn, pawn.clone());
  }
}
