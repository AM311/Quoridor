import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Tile;
import it.units.sdm.quoridor.model.builder.IQuoridorBuilder;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.movemanagement.actioncheckers.PawnMovementChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class PawnMovementCheckerTest {
  ActionChecker<Tile> pawnMovementChecker = new PawnMovementChecker();

  //TODO metodi che chiedano caselle FUORI SCACCHIERA -- GESTIRE ECCEZIONI

  @ParameterizedTest
  @CsvSource({"1, 4, 1, 6", "1, 4, 1, 2", "1, 4, 2, 5", "1, 4, 3, 7", "1, 4, 2, 3", "1,7,0,6"})
  void checkNotAdjacencyMoveNotAllowed(int startingRow, int startingColumn, int destinationRow, int destinationColumn) throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(startingRow, startingColumn));
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(destinationRow, destinationColumn));
    Assertions.assertFalse(checkMove);
  }

  @ParameterizedTest
  @CsvSource({"1, 4, 0, 4", "1, 4, 1, 3", "1, 4, 2, 4", "1, 4, 1, 5"})
  void checkAdjacencyMoveAllowed(int startingRow, int startingColumn, int destinationRow, int destinationColumn) throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(startingRow, startingColumn));
    gameBoard.getTile(destinationRow, destinationColumn).setOccupiedBy(false);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(destinationRow, destinationColumn));
    Assertions.assertTrue(checkMove);
  }

  @ParameterizedTest
  @CsvSource({"3, 6, 2, 6", "4, 3, 3, 3", "8, 3, 8, 4"})
  void goingToAnOccupiedTileNotAllowed(int startingRow, int startingColumn, int occupiedRow, int occupiedColumn) throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(startingRow, startingColumn));
    gameBoard.getTile(occupiedRow, occupiedColumn).setOccupiedBy(true);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(occupiedRow, occupiedColumn));
    Assertions.assertFalse(checkMove);
  }

  //===========================

  @ParameterizedTest
  @CsvSource({"4, 3, 4, 4, 4, 5", "0, 4, 0, 3, 0, 2", "8, 6, 8, 7, 8, 8", "5, 6, 4, 6, 3, 6", "4, 8, 3, 8, 2, 8", "0, 2, 1, 2, 2, 2"})
  void jumpingOverNeighborPawnAllowed(int startingRow, int startingColumn, int occupiedRow, int occupiedColumn, int targetRow, int targetColumn) throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(startingRow, startingColumn));
    gameBoard.getTile(occupiedRow, occupiedColumn).setOccupiedBy(true);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(targetRow, targetColumn));
    Assertions.assertTrue(checkMove);
  }

  @ParameterizedTest
  @CsvSource({"3, 6, 2, 6, 0, 6", "3, 5, 4, 5, 6, 5", "8, 8, 8, 7, 8, 5", "1, 3, 1, 2, 1, 0"})
  void jumpingTwoTilesOverNeighborPawnNotAllowed(int startingRow, int startingColumn, int occupiedRow, int occupiedColumn, int targetRow, int targetColumn) throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(startingRow, startingColumn));
    gameBoard.getTile(occupiedRow, occupiedColumn).setOccupiedBy(true);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(targetRow, targetColumn));
    Assertions.assertFalse(checkMove);
  }

  @ParameterizedTest
  @CsvSource({"5, 5, 5, 3, 5, 2", "7, 3, 7, 1, 7, 0", "1, 2, 1, 4, 1, 5", "8, 5, 8, 7, 8, 8"})
  void jumpingTwoTilesOverNotNeighborPawnNotAllowed(int startingRow, int startingColumn, int occupiedRow, int occupiedColumn, int targetRow, int targetColumn) throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(startingRow, startingColumn));
    gameBoard.getTile(occupiedRow, occupiedColumn).setOccupiedBy(true);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(targetRow, targetColumn));
    Assertions.assertFalse(checkMove);
  }

  //===========================

  @Test
  void jumpingOverWallFrom53To43NotAllowed() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(5, 3));
    gameBoard.getTile(5, 3).setLink(UP, WALL);
    gameBoard.getTile(5, 4).setLink(UP, WALL);
    gameBoard.getTile(4, 3).setLink(DOWN, WALL);
    gameBoard.getTile(4, 4).setLink(DOWN, WALL);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(4, 3));
    Assertions.assertFalse(checkMove);
  }

  @Test
  void jumpingOverWallFrom32To33NotAllowed() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(3, 2));
    gameBoard.getTile(3, 2).setLink(RIGHT, WALL);
    gameBoard.getTile(2, 2).setLink(RIGHT, WALL);
    gameBoard.getTile(3, 3).setLink(LEFT, WALL);
    gameBoard.getTile(2, 3).setLink(LEFT, WALL);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(3, 3));
    Assertions.assertFalse(checkMove);
  }

  @Test
  void jumpingOverPawnHavingAWallBehindFrom31to33NotAllowed() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(3, 1));
    gameBoard.getTile(3, 2).setOccupiedBy(true);
    gameBoard.getTile(3, 2).setLink(RIGHT, WALL);
    gameBoard.getTile(2, 2).setLink(RIGHT, WALL);
    gameBoard.getTile(3, 3).setLink(LEFT, WALL);
    gameBoard.getTile(2, 3).setLink(LEFT, WALL);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(3, 3));
    Assertions.assertFalse(checkMove);
  }

  @Test
  void jumpingOverPawnHavingAWallBehindFrom71to51NotAllowed() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(7, 1));
    gameBoard.getTile(6, 1).setOccupiedBy(true);
    gameBoard.getTile(6, 1).setLink(UP, WALL);
    gameBoard.getTile(6, 2).setLink(UP, WALL);
    gameBoard.getTile(5, 1).setLink(DOWN, WALL);
    gameBoard.getTile(5, 2).setLink(DOWN, WALL);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(5, 1));
    Assertions.assertFalse(checkMove);
  }

  @Test
  void goingDiagonalIfThereIsAPawnAndWallIsAllowedFrom36To27() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(3, 6));
    gameBoard.getTile(2, 6).setOccupiedBy(true);
    gameBoard.getTile(2, 6).setLink(UP, WALL);
    gameBoard.getTile(2, 7).setLink(UP, WALL);
    gameBoard.getTile(1, 6).setLink(DOWN, WALL);
    gameBoard.getTile(1, 7).setLink(DOWN, WALL);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(2, 7));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void goingDiagonalIfThereIsAPawnAndWallIsAllowedFrom31To22() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(3, 1));
    gameBoard.getTile(3, 2).setOccupiedBy(true);
    gameBoard.getTile(3, 2).setLink(RIGHT, WALL);
    gameBoard.getTile(2, 2).setLink(RIGHT, WALL);
    gameBoard.getTile(2, 3).setLink(LEFT, WALL);
    gameBoard.getTile(3, 3).setLink(LEFT, WALL);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(2, 2));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void goingDiagonalIfThereAreWallsPlacedAsTNotAllowedFrom77To66() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(7, 7));
    gameBoard.getTile(7, 6).setOccupiedBy(true);
    gameBoard.getTile(7, 5).setLink(UP, WALL);
    gameBoard.getTile(7, 6).setLink(UP, WALL);
    gameBoard.getTile(6, 5).setLink(DOWN, WALL);
    gameBoard.getTile(6, 6).setLink(DOWN, WALL);

    gameBoard.getTile(7, 5).setLink(RIGHT, WALL);
    gameBoard.getTile(8, 5).setLink(RIGHT, WALL);
    gameBoard.getTile(7, 6).setLink(LEFT, WALL);
    gameBoard.getTile(8, 6).setLink(LEFT, WALL);

    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(6, 6));
    Assertions.assertFalse(checkMove);
  }

  @Test
  void goingDiagonalIfThereAreWallsPlacedAsTNotAllowedFrom74To65() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(7, 4));
    gameBoard.getTile(7, 5).setOccupiedBy(true);
    gameBoard.getTile(7, 5).setLink(UP, WALL);
    gameBoard.getTile(7, 6).setLink(UP, WALL);
    gameBoard.getTile(6, 5).setLink(DOWN, WALL);
    gameBoard.getTile(6, 6).setLink(DOWN, WALL);

    gameBoard.getTile(7, 5).setLink(RIGHT, WALL);
    gameBoard.getTile(8, 5).setLink(RIGHT, WALL);
    gameBoard.getTile(7, 6).setLink(LEFT, WALL);
    gameBoard.getTile(8, 6).setLink(LEFT, WALL);

    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(6, 5));
    Assertions.assertFalse(checkMove);
  }

  @Test
  void isThereNotAdjacentWallIn43From63() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(4, 3));
    gameBoard.getTile(4, 3).setLink(UP, WALL);
    gameBoard.getTile(4, 4).setLink(UP, WALL);
    gameBoard.getTile(3, 3).setLink(DOWN, WALL);
    gameBoard.getTile(3, 4).setLink(DOWN, WALL);

    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(5, 3));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void isThereAPawnAdjacentAndAWallBehindFrom52To41() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(5, 2));
    gameBoard.getTile(4, 2).setOccupiedBy(true);
    gameBoard.getTile(4, 2).setLink(UP, WALL);
    gameBoard.getTile(4, 3).setLink(UP, WALL);
    gameBoard.getTile(3, 2).setLink(DOWN, WALL);
    gameBoard.getTile(3, 3).setLink(DOWN, WALL);

    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(4, 1));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void isThereAWallOnLeftAndAPawnBehindItFrom56To45() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(5, 6));
    gameBoard.getTile(5, 5).setOccupiedBy(true);
    gameBoard.getTile(5, 5).setLink(LEFT, WALL);
    gameBoard.getTile(5, 4).setLink(RIGHT, WALL);
    gameBoard.getTile(6, 4).setLink(RIGHT, WALL);
    gameBoard.getTile(6, 5).setLink(LEFT, WALL);

    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(4, 5));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void isThereAPawnOnTheLeftAndBehindAWallFrom42To51() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(4, 2));
    gameBoard.getTile(4, 1).setOccupiedBy(true);
    gameBoard.getTile(4, 1).setLink(LEFT, WALL);
    gameBoard.getTile(4, 0).setLink(RIGHT, WALL);
    gameBoard.getTile(5, 0).setLink(RIGHT, WALL);
    gameBoard.getTile(5, 1).setLink(LEFT, WALL);

    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(5, 1));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void isThereAPawnBelowAndAWallBehindFrom45To53() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(4, 5));
    gameBoard.getTile(5, 5).setOccupiedBy(true);
    gameBoard.getTile(5, 5).setLink(DOWN, WALL);
    gameBoard.getTile(5, 6).setLink(DOWN, WALL);
    gameBoard.getTile(6, 5).setLink(UP, WALL);
    gameBoard.getTile(6, 6).setLink(LEFT, WALL);

    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(5, 4));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void isThereAPawnInFrontOfMeAndAWallBehindFrom23To34() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(2, 3));
    gameBoard.getTile(2, 4).setOccupiedBy(true);
    gameBoard.getTile(2, 4).setLink(RIGHT, WALL);
    gameBoard.getTile(1, 4).setLink(RIGHT, WALL);
    gameBoard.getTile(2, 5).setLink(LEFT, WALL);
    gameBoard.getTile(1, 5).setLink(LEFT, WALL);

    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(3, 4));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void isThereAPawnBelowAndAWallBehindFrom66To75() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(6, 6));
    gameBoard.getTile(7, 6).setOccupiedBy(true);
    gameBoard.getTile(7, 6).setLink(DOWN, WALL);
    gameBoard.getTile(7, 7).setLink(DOWN, WALL);
    gameBoard.getTile(8, 5).setLink(UP, WALL);
    gameBoard.getTile(8, 7).setLink(UP, WALL);

    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(7, 5));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void isThereAPawnBelowAndAWallBehindFrom52To61() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(5, 2));
    gameBoard.getTile(6, 2).setOccupiedBy(true);
    gameBoard.getTile(6, 1).setLink(DOWN, WALL);
    gameBoard.getTile(6, 2).setLink(DOWN, WALL);
    gameBoard.getTile(7, 2).setLink(UP, WALL);
    gameBoard.getTile(7, 1).setLink(UP, WALL);

    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(6, 1));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void isThereAPawnBelowAndAWallBehindAndAWallOnRightFrom52To63IsNotAllowed() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(5, 2));
    gameBoard.getTile(6, 2).setOccupiedBy(true);
    gameBoard.getTile(6, 1).setLink(DOWN, WALL);
    gameBoard.getTile(6, 2).setLink(DOWN, WALL);
    gameBoard.getTile(7, 2).setLink(UP, WALL);
    gameBoard.getTile(7, 1).setLink(UP, WALL);

    gameBoard.getTile(6, 3).setLink(LEFT, WALL);
    gameBoard.getTile(6, 2).setLink(RIGHT, WALL);
    gameBoard.getTile(7, 2).setLink(RIGHT, WALL);
    gameBoard.getTile(7, 3).setLink(LEFT, WALL);

    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(6, 3));
    Assertions.assertFalse(checkMove);
  }

  @Test
  void diagonalMoveOnBorderWithAPawnInFrontFrom11to02() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(1, 1));
    gameBoard.getTile(1, 1).setOccupiedBy(true);
    gameBoard.getTile(0, 1).setOccupiedBy(true);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(0, 2));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void diagonalMoveOnBorderWithAPawnInFrontFrom61to50() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(6, 1));
    gameBoard.getTile(6, 1).setOccupiedBy(true);
    gameBoard.getTile(6, 0).setOccupiedBy(true);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(5, 0));
    Assertions.assertTrue(checkMove);
  }

  @Test
  void diagonalMoveOnBorderWithAPawnInFrontFrom21to02IsNotAllowed() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(2, 1));
    gameBoard.getTile(2, 1).setOccupiedBy(true);
    gameBoard.getTile(0, 1).setOccupiedBy(true);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(0, 2));
    Assertions.assertFalse(checkMove);
  }

  @Test
  void diagonalMoveOnBorderWithAPawnInFrontFrom58to47IsNotAllowed() throws InvalidParameterException {
    Game game = new IQuoridorBuilder().setNumberOfPlayers(2).createGame();
    Pawn pawn = game.getPlayingPawn();
    GameBoard gameBoard = game.getGameBoard();

    pawn.move(gameBoard.getTile(5, 8));
    gameBoard.getTile(5, 8).setOccupiedBy(true);
    gameBoard.getTile(4, 8).setOccupiedBy(true);
    boolean checkMove = pawnMovementChecker.isValidAction(game, gameBoard.getTile(4, 7));
    Assertions.assertFalse(checkMove);
  }

}