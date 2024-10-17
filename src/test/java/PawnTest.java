import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PawnTest {

  BuilderDirector builderDirector;

  /*
  @Test
  void constructorTest_startingTileIsConsistent() {
    GameBoard gameBoard = new GameBoard();

    Pawn pawn = new Pawn(gameBoard.getStartingAndDestinationTiles().getFirst().getKey(), gameBoard.getStartingAndDestinationTiles().getFirst().getValue(), Color.black, 10);

    Assertions.assertEquals(gameBoard.getStartingAndDestinationTiles().getFirst().getKey(), pawn.getCurrentTile());
  }

  @Test
  void constructorTest_destinationTilesAreConsistent() {
    GameBoard gameBoard = new GameBoard();

    Pawn pawn = new Pawn(gameBoard.getStartingAndDestinationTiles().getFirst().getKey(), gameBoard.getStartingAndDestinationTiles().getFirst().getValue(), Color.black, 10);

    Assertions.assertEquals(gameBoard.getStartingAndDestinationTiles().getFirst().getValue(), pawn.getDestinationTiles());
  }

  @ParameterizedTest
  @ValueSource(ints = {5, 10})
  void constructorTest_numberOfWallsIsConsistent(int numberOfWalls) {
    GameBoard gameBoard = new GameBoard();
    Pawn pawn = new Pawn(gameBoard.getStartingAndDestinationTiles().getFirst().getKey(), gameBoard.getStartingAndDestinationTiles().getFirst().getValue(), Color.black, numberOfWalls);
    Assertions.assertEquals(numberOfWalls, pawn.getNumberOfWalls());
  }

  @Test
  void constructorTest_colorIsConsistent() {
    GameBoard gameBoard = new GameBoard();
    Pawn pawn = new Pawn(gameBoard.getStartingAndDestinationTiles().getFirst().getKey(), gameBoard.getStartingAndDestinationTiles().getFirst().getValue(), Color.black, 10);
    Assertions.assertEquals(Color.black, pawn.getColor());
  }
*/

  @ParameterizedTest
  @CsvSource({"3,4", "7,5", "6,2", "5,5"})
  void moveTest(int destinationTileRow, int destinationTileColumn) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractPawn pawn = game.getPawns().get(0);

    Position destinationTilePosition = new Position(destinationTileRow, destinationTileColumn);
    AbstractTile destinationTile = game.getGameBoard().getTile(destinationTilePosition);
    pawn.move(destinationTile);
    Assertions.assertEquals(destinationTile, pawn.getCurrentTile());
  }

  @ParameterizedTest
  @CsvSource({"10", "5", "3"})
  void decrementNumberOfWallsTest(int numberOfWalls) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractPawn pawn = game.getPawns().get(0);

    pawn.decrementNumberOfWalls();
    int expected = numberOfWalls - 1;
    int actual = pawn.getNumberOfWalls();
    Assertions.assertEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({"9,4", "0,5", "8,1", "5,5"})
  void hasReachedDestinationTest(int currentTileRow, int currentTileColumn) throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();
    AbstractPawn pawn = game.getPawns().get(0);

    Position currentTilePosition = new Position(currentTileRow, currentTileColumn);
    AbstractTile currentTile = game.getGameBoard().getTile(currentTilePosition);
    pawn.move(currentTile);
    Assertions.assertEquals(pawn.hasReachedDestination(), pawn.getDestinationTiles().contains(currentTile));
  }
}
