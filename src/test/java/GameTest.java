import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;

public class GameTest {
  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    return builderDirector.makeGame();
  }

  private static void movePawn(AbstractGame game, Position position) throws InvalidParameterException {
    game.getPlayingPawn().move(game.getGameBoard().getTile(position));
    game.getGameBoard().getTile(position).setOccupiedBy(game.getPlayingPawn());
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
  @CsvSource({"1, 4, 1, 6", "1, 4, 1, 2", "1, 4, 2, 5", "1, 4, 3, 7", "1, 4, 2, 3", "1, 7, 0, 6"})
  void checkNotAdjacencyMoveNotAllowed(int startingRow, int startingColumn, int targetRow, int targetColumn) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();

    Position startingPosition = new Position(startingRow, startingColumn);
    Position targetPosition = new Position(targetRow, targetColumn);

    movePawn(game, startingPosition);

    Assertions.assertThrows(InvalidActionException.class, () -> game.movePlayingPawn(targetPosition));
  }


  @ParameterizedTest
  @CsvSource({"3, 6, 2, 6", "4, 3, 3, 3", "7, 3, 7, 4"})
  void goingToAnOccupiedTileNotAllowed(int startingRow, int startingColumn, int opponentRow, int opponentColumn) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();

    Position startingPosition = new Position(startingRow, startingColumn);
    Position opponentPosition = new Position(opponentRow, opponentColumn);

    movePawn(game, startingPosition);

    game.changeRound();
    movePawn(game, opponentPosition);
    game.changeRound();

    Assertions.assertThrows(InvalidActionException.class, () -> game.movePlayingPawn(opponentPosition));
  }


  @ParameterizedTest
  @CsvSource({"3, 6, 2, 6, 0, 6", "3, 5, 4, 5, 6, 5", "8, 8, 8, 7, 8, 5", "1, 3, 1, 2, 1, 0"})
  void jumpingTwoTilesOverNeighborPawnNotAllowed(int startingRow, int startingColumn, int opponentRow, int opponentColumn, int targetRow, int targetColumn) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();

    Position startingPosition = new Position(startingRow, startingColumn);
    Position targetPosition = new Position(targetRow, targetColumn);
    Position opponentPosition = new Position(opponentRow, opponentColumn);

    movePawn(game, startingPosition);

    game.changeRound();
    movePawn(game, opponentPosition);
    game.changeRound();

    Assertions.assertThrows(InvalidActionException.class, () -> game.movePlayingPawn(targetPosition));
  }


  @ParameterizedTest
  @CsvSource({"5, 3, 4, 3, 4, 3", "3, 6 , 2, 6, 2, 6", "8, 0, 7, 0, 7, 0"})
  void jumpingOverHorizontalWallNotAllowed(int startingRow, int startingColumn, int targetRow, int targetColumn, int wallRow, int wallColumn) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();

    Position startingPosition = new Position(startingRow, startingColumn);
    Position wallPosition = new Position(wallRow, wallColumn);
    Position targetPosition = new Position(targetRow, targetColumn);

    movePawn(game, startingPosition);

    game.placeWall(wallPosition, HORIZONTAL);

    Assertions.assertThrows(InvalidActionException.class, () -> game.movePlayingPawn(targetPosition));
  }


  @ParameterizedTest
  @CsvSource({"1, 7, 1, 6, 1, 7", "3, 3, 3, 2, 4, 3", "8, 1, 8, 0, 8, 1", "5, 7, 5, 8, 6 , 8"})
  void jumpingOverVerticalWallNotAllowed(int startingRow, int startingColumn, int targetRow, int targetColumn, int wallRow, int wallColumn) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();

    Position startingPosition = new Position(startingRow, startingColumn);
    Position wallPosition = new Position(wallRow, wallColumn);
    Position targetPosition = new Position(targetRow, targetColumn);

    movePawn(game, startingPosition);

    game.placeWall(wallPosition, VERTICAL);

    Assertions.assertThrows(InvalidActionException.class, () -> game.movePlayingPawn(targetPosition));
  }


  @ParameterizedTest
  @CsvSource({"3, 1, 3, 2, 3, 3, 4, 3", "6, 6, 6, 5, 6, 4, 6, 5", "8, 6, 8, 7, 8, 8, 8, 8"})
  void jumpingOverPawnHavingVerticalWallBehindNotAllowed(int startingRow, int startingColumn, int opponentRow, int opponentColumn, int targetRow, int targetColumn, int wallRow, int wallColumn) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();

    Position startingPosition = new Position(startingRow, startingColumn);
    Position opponentPosition = new Position(opponentRow, opponentColumn);
    Position wallPosition = new Position(wallRow, wallColumn);
    Position targetPosition = new Position(targetRow, targetColumn);

    movePawn(game, startingPosition);

    game.changeRound();
    movePawn(game, opponentPosition);
    game.changeRound();

    game.placeWall(wallPosition, VERTICAL);

    Assertions.assertThrows(InvalidActionException.class, () -> game.movePlayingPawn(targetPosition));
  }


  @ParameterizedTest
  @CsvSource({"7, 1, 6, 1, 5, 1, 5, 1", "2, 1, 1, 1, 0, 1, 0, 0", "2, 6, 3, 6, 4, 6, 3, 5"})
  void jumpingOverPawnHavingHorizontalWallBehindNotAllowed(int startingRow, int startingColumn, int opponentRow, int opponentColumn, int targetRow, int targetColumn, int wallRow, int wallColumn) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();

    Position startingPosition = new Position(startingRow, startingColumn);
    Position opponentPosition = new Position(opponentRow, opponentColumn);
    Position wallPosition = new Position(wallRow, wallColumn);
    Position targetPosition = new Position(targetRow, targetColumn);

    movePawn(game, startingPosition);

    game.changeRound();
    movePawn(game, opponentPosition);
    game.changeRound();

    game.placeWall(wallPosition, HORIZONTAL);

    Assertions.assertThrows(InvalidActionException.class, () -> game.movePlayingPawn(targetPosition));
  }


  @ParameterizedTest
  @CsvSource({"7, 7, 7, 6, 6, 6, 8, 6, 6, 5", "3, 2, 2, 2, 2, 1, 2, 2, 1, 2", "2, 7, 3, 7, 3, 8, 4, 8, 3, 6"})
  void goingDiagonalIfThereAreWallsPlacedAsTNotAllowed(int startingRow, int startingColumn, int opponentRow, int opponentColumn, int targetRow, int targetColumn, int verticalWallRow, int verticalWallColumn, int horizontalWallRow, int horizontalWallColumn) throws InvalidParameterException, BuilderException, InvalidActionException {
    AbstractGame game = buildGame();

    Position startingPosition = new Position(startingRow, startingColumn);
    Position opponentPosition = new Position(opponentRow, opponentColumn);
    Position horizontalwallPosition = new Position(horizontalWallRow, horizontalWallColumn);
    Position verticalwallPosition = new Position(verticalWallRow, verticalWallColumn);
    Position targetPosition = new Position(targetRow, targetColumn);

    movePawn(game, startingPosition);

    game.changeRound();
    movePawn(game, opponentPosition);
    game.changeRound();

    game.placeWall(verticalwallPosition, VERTICAL);
    game.placeWall(horizontalwallPosition, HORIZONTAL);

    Assertions.assertThrows(InvalidActionException.class, () -> game.movePlayingPawn(targetPosition));
  }


  @ParameterizedTest
  @CsvSource({"4, 3, 3, 3, 2, 3, 1, 3", "4, 3, 4, 2, 4, 1, 4, 0", "4, 5, 4, 6, 4, 7, 4, 8", "4, 5, 5, 5, 6, 5, 7, 5"})
  void jumpingOverTwoPlayersNotAllowed(int startingRow, int startingColumn, int secondPlayerRow, int secondPlayerColumn, int thirdPlayerRow, int thirdPlayerColumn, int targetRow, int targetColumn) throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(4));
    AbstractGame game = builderDirector.makeGame();

    Position startingPosition = new Position(startingRow, startingColumn);
    Position secondPlayerPosition = new Position(secondPlayerRow, secondPlayerColumn);
    Position thirdPlayerPosition = new Position(thirdPlayerRow, thirdPlayerColumn);
    Position targetPosition = new Position(targetRow, targetColumn);

    movePawn(game, startingPosition);

    game.changeRound();
    movePawn(game, secondPlayerPosition);

    game.changeRound();
    movePawn(game, thirdPlayerPosition);

    game.changeRound();
    game.changeRound();

    Assertions.assertThrows(InvalidActionException.class, () -> game.movePlayingPawn(targetPosition));
  }

  @ParameterizedTest
  @CsvSource({"0, 8, 0, 9", "0, 0, -1, 0", "8, 0, 9, 0", "8, 8, 8, 9"})
  void invalidTargetCoordinatesNotAllowed(int startingRow, int startingColumn, int targetRow, int targetColumn) throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();

    Position startingPosition = new Position(startingRow, startingColumn);
    Position targetPosition = new Position(targetRow, targetColumn);

    movePawn(game, startingPosition);

    Assertions.assertThrows(InvalidParameterException.class, () -> game.movePlayingPawn(targetPosition));
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
