import it.units.sdm.quoridor.exceptions.NumberOfWallsBelowZeroException;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;

public class PawnTest {
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

  @ParameterizedTest
  @CsvSource({"3,4", "7,5", "6,2", "5,5"})
  void movePawnIsConsistent(int destinationTileRow, int destinationTileColumn) {
    GameBoard gameBoard = new GameBoard();
    Pawn pawn = new Pawn(gameBoard.getStartingAndDestinationTiles().getFirst().getKey(), gameBoard.getStartingAndDestinationTiles().getFirst().getValue(), Color.black, 10);

    Tile destinationTile = gameBoard.getTile(destinationTileRow, destinationTileColumn);
    pawn.move(destinationTile);

    Assertions.assertEquals(destinationTile, pawn.getCurrentTile());
  }

  @ParameterizedTest
  @CsvSource({"10", "5", "3"})
  void decrementNumberOfWallsIsConsistent(int numberOfWalls) {
    GameBoard gameBoard = new GameBoard();

    Pawn pawn = new Pawn(gameBoard.getStartingAndDestinationTiles().getFirst().getKey(), gameBoard.getStartingAndDestinationTiles().getFirst().getValue(), Color.black, numberOfWalls);
    pawn.decrementNumberOfWalls();
    int expected = numberOfWalls - 1;
    int actual = pawn.getNumberOfWalls();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void decrementNumberOfWallsNotAllowedBelowZero() {
    GameBoard gameBoard = new GameBoard();

    Pawn pawn = new Pawn(gameBoard.getStartingAndDestinationTiles().getFirst().getKey(), gameBoard.getStartingAndDestinationTiles().getFirst().getValue(), Color.black, 0);

    Assertions.assertThrows(NumberOfWallsBelowZeroException.class, pawn::decrementNumberOfWalls);
  }
}
