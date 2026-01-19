import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.abstracts.AbstractGameBoard;
import it.units.sdm.quoridor.model.abstracts.AbstractPawn;
import it.units.sdm.quoridor.model.abstracts.AbstractTile;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.TargetTiles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static it.units.sdm.quoridor.model.abstracts.AbstractTile.LinkState.EDGE;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class BuilderTest {
  @ParameterizedTest
  @ValueSource(ints = {2, 4})
  public void builderDirectorTest_stdGameIsCorrectlyBuilt_gameBoardIsCorrect(int numberOfPlayers) throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StandardQuoridorBuilder(numberOfPlayers));
    AbstractGame game = builderDirector.makeGame();

    AbstractGameBoard gameBoard = game.getGameBoard();

    AbstractTile[][] expectedGameState = new AbstractTile[9][9];
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        expectedGameState[i][j] = new Tile(new Position(i, j));

        switch (i) {
          case 0 -> expectedGameState[i][j].setLink(UP, EDGE);
          case 8 -> expectedGameState[i][j].setLink(DOWN, EDGE);
        }

        switch (j) {
          case 0 -> expectedGameState[i][j].setLink(LEFT, EDGE);
          case 8 -> expectedGameState[i][j].setLink(RIGHT, EDGE);
        }
      }
    }

    expectedGameState[0][4].setOccupiedBy(game.getPawns().get(0));
    expectedGameState[8][4].setOccupiedBy(game.getPawns().get(1));
    if (numberOfPlayers == 4) {
      expectedGameState[4][0].setOccupiedBy(game.getPawns().get(2));
      expectedGameState[4][8].setOccupiedBy(game.getPawns().get(3));
    }

    AbstractGameBoard expectedGameBoard = new GameBoard(expectedGameState);

    Assertions.assertEquals(expectedGameBoard, gameBoard);
  }

  @ParameterizedTest
  @CsvSource({"2,10", "4,5"})
  public void builderDirectorTest_stdGameIsCorrectlyBuilt_pawnsListIsCorrect(int numberOfPlayers, int numberOfWalls) throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StandardQuoridorBuilder(numberOfPlayers));
    AbstractGame game = builderDirector.makeGame();

    List<AbstractPawn> pawns = game.getPawns();
    List<TargetTiles> targetTiles = game.getGameBoard().getStartingAndDestinationTiles();

    List<AbstractPawn> expectedPawns = new ArrayList<>();
    expectedPawns.add(new Pawn(targetTiles.get(0).startingTile(), targetTiles.get(0).destinationTiles(), PawnAppearance.getDefaultPawnStyles().get(0), numberOfWalls));
    expectedPawns.add(new Pawn(targetTiles.get(1).startingTile(), targetTiles.get(1).destinationTiles(), PawnAppearance.getDefaultPawnStyles().get(1), numberOfWalls));
    if (numberOfPlayers == 4) {
      expectedPawns.add(new Pawn(targetTiles.get(2).startingTile(), targetTiles.get(2).destinationTiles(), PawnAppearance.getDefaultPawnStyles().get(2), numberOfWalls));
      expectedPawns.add(new Pawn(targetTiles.get(3).startingTile(), targetTiles.get(3).destinationTiles(), PawnAppearance.getDefaultPawnStyles().get(3), numberOfWalls));
    }

    Assertions.assertEquals(expectedPawns, pawns);
  }

  @ParameterizedTest
  @ValueSource(ints = {-2, 0, 1, 3, 5, 11})
  public void builderTest_wrongParametersAreInhibited_wrongNumberOfPlayers(int numberOfPlayers) {
    Assertions.assertThrows(InvalidParameterException.class, () -> new StandardQuoridorBuilder(numberOfPlayers));
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 4})
  public void builderTest_correctParametersAreAccepted_correctNumberOfPlayers(int numberOfPlayers) {
    Assertions.assertDoesNotThrow(() -> new StandardQuoridorBuilder(numberOfPlayers));
  }

    @Test
  public void builderDirectorTest_setBuilderWorksCorrectly() throws InvalidParameterException, BuilderException {
    AbstractQuoridorBuilder builder2 = new StandardQuoridorBuilder(2);
    AbstractQuoridorBuilder builder4 = new StandardQuoridorBuilder(4);

    BuilderDirector builderDirector = new BuilderDirector(builder2);
    builderDirector.setBuilder(builder4);

    AbstractGame game = builderDirector.makeGame();

    Assertions.assertEquals(4, game.getPawns().size());
  }
}
