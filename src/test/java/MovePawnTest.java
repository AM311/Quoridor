import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.movemanager.ActionChecker;
import it.units.sdm.quoridor.movemanager.PawnMovementChecker;
import it.units.sdm.quoridor.utils.Direction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;

public class MovePawnTest {
  GameBoard gameBoard = new GameBoard();
  GameBoard.Tile startingTile = gameBoard.getGameState()[0][4];
  Pawn cecilia = new Pawn(startingTile, Color.red, 10);

  ActionChecker<GameBoard.Tile> pawnMovementChecker = new PawnMovementChecker();

  @ParameterizedTest
  @CsvSource({"1, 4, 1, 6", "1, 4, 1, 2", "1, 4, 2, 5", "1, 4, 3, 7", "1, 4, 2, 3", "1, 4, 0, 3", "1, 4, 0, 5"})
  void checkNotAdjacencyMoveNotAllowed(int startingRow, int startingColumn, int destinationRow, int destinationColumn) {
    cecilia.setCurrentTile(gameBoard.getGameState()[startingRow][startingColumn]);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[destinationRow][destinationColumn]);
    Assertions.assertFalse(checkMove);
  }

  @ParameterizedTest
  @CsvSource({"1, 4, 0, 4", "1, 4, 1, 3", "1, 4, 2, 4", "1, 4, 1, 5"})
  void checkAdjacencyMoveAllowed(int startingRow, int startingColumn, int destinationRow, int destinationColumn) {
    cecilia.setCurrentTile(gameBoard.getGameState()[startingRow][startingColumn]);
    gameBoard.getGameState()[destinationRow][destinationColumn].setOccupied(false);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[destinationRow][destinationColumn]);
    Assertions.assertTrue(checkMove);
  }

  @ParameterizedTest
  @CsvSource({"3, 6, 2, 6", "4, 3, 3, 3", "8, 3, 8, 4"})
  void goingToAnOccupiedTileNotAllowed(int startingRow, int startingColumn, int occupiedRow, int occupiedColumn) {
    cecilia.setCurrentTile(gameBoard.getGameState()[startingRow][startingColumn]);
    gameBoard.getGameState()[occupiedRow][occupiedColumn].setOccupied(true);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[occupiedRow][occupiedColumn]);
    Assertions.assertFalse(checkMove);
  }

  @Test
  void jumpingOverNeighborPawnAllowedFrom36To56() {
    cecilia.setCurrentTile(gameBoard.getGameState()[3][6]);
    gameBoard.getGameState()[4][6].setOccupied(true);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[5][6]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void jumpingOverNeighborPawnAllowedFrom46To48() {
    cecilia.setCurrentTile(gameBoard.getGameState()[4][6]);
    gameBoard.getGameState()[4][7].setOccupied(true);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[4][8]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void jumpingOverNeighborPawnAllowedFrom76To74() {
    cecilia.setCurrentTile(gameBoard.getGameState()[7][6]);
    gameBoard.getGameState()[7][5].setOccupied(true);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[7][4]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void jumpingOverNeighborPawnNotAllowedFrom36To06() {
    cecilia.setCurrentTile(gameBoard.getGameState()[3][6]);
    gameBoard.getGameState()[2][6].setOccupied(true);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[0][6]);
    Assertions.assertFalse(checkMove);
  }

  @Test
  void jumpingOverNeighborPawnNotAllowedFrom33To63() {
    cecilia.setCurrentTile(gameBoard.getGameState()[3][3]);
    gameBoard.getGameState()[4][3].setOccupied(true);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[6][3]);
    Assertions.assertFalse(checkMove);
  }

  @Test
  void jumpingOverNotNeighborPawnNotAllowedFrom36To06() {
    cecilia.setCurrentTile(gameBoard.getGameState()[3][6]);
    gameBoard.getGameState()[1][6].setOccupied(true);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[0][6]);
    Assertions.assertFalse(checkMove);
  }

  @Test
  void jumpingOverNotNeighborPawnNotAllowedFrom62To65() {
    cecilia.setCurrentTile(gameBoard.getGameState()[6][2]);
    gameBoard.getGameState()[6][4].setOccupied(true);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[6][5]);
    Assertions.assertFalse(checkMove);
  }

  @Test
  void jumpingOverWallFrom53To43NotAllowed() {
    cecilia.setCurrentTile(gameBoard.getGameState()[5][3]);
    gameBoard.getGameState()[5][3].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[5][4].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[4][3].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[4][4].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[4][3]);
    Assertions.assertFalse(checkMove);
  }

  @Test
  void jumpingOverWallFrom32To33NotAllowed() {
    cecilia.setCurrentTile(gameBoard.getGameState()[3][2]);
    gameBoard.getGameState()[3][2].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[2][2].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[3][3].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[2][3].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[3][3]);
    Assertions.assertFalse(checkMove);
  }

  @Test
  void jumpingOverPawnHavingAWallBehindFrom31to33NotAllowed() {
    cecilia.setCurrentTile(gameBoard.getGameState()[3][1]);
    gameBoard.getGameState()[3][2].setOccupied(true);
    gameBoard.getGameState()[3][2].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[2][2].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[3][3].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[2][3].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[3][3]);
    Assertions.assertFalse(checkMove);
  }

  @Test
  void jumpingOverPawnHavingAWallBehindFrom71to51NotAllowed() {
    cecilia.setCurrentTile(gameBoard.getGameState()[7][1]);
    gameBoard.getGameState()[6][1].setOccupied(true);
    gameBoard.getGameState()[6][1].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][2].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[5][1].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[5][2].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[5][1]);
    Assertions.assertFalse(checkMove);
  }

  @Test
  void goingDiagonalIfThereIsAPawnAndWallIsAllowedFrom36To27() {
    cecilia.setCurrentTile(gameBoard.getGameState()[3][6]);
    gameBoard.getGameState()[2][6].setOccupied(true);
    gameBoard.getGameState()[2][6].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[2][7].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[1][6].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[1][7].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[2][7]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void goingDiagonalIfThereIsAPawnAndWallIsAllowedFrom31To22() {
    cecilia.setCurrentTile(gameBoard.getGameState()[3][1]);
    gameBoard.getGameState()[3][2].setOccupied(true);
    gameBoard.getGameState()[3][2].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[2][2].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[2][3].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[3][3].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[2][2]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void goingDiagonalIfThereAreWallsPlacedAsTNotAllowedFrom77To66() {
    cecilia.setCurrentTile(gameBoard.getGameState()[7][7]);
    gameBoard.getGameState()[7][6].setOccupied(true);
    gameBoard.getGameState()[7][5].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[7][6].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][5].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][6].setLink(Direction.DOWN, GameBoard.LinkState.WALL);

    gameBoard.getGameState()[7][5].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[8][5].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[7][6].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[8][6].setLink(Direction.LEFT, GameBoard.LinkState.WALL);

    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[6][6]);
    Assertions.assertFalse(checkMove);
  }

  @Test
  void goingDiagonalIfThereAreWallsPlacedAsTNotAllowedFrom74To65() {
    cecilia.setCurrentTile(gameBoard.getGameState()[7][4]);
    gameBoard.getGameState()[7][5].setOccupied(true);
    gameBoard.getGameState()[7][5].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[7][6].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][5].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][6].setLink(Direction.DOWN, GameBoard.LinkState.WALL);

    gameBoard.getGameState()[7][5].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[8][5].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[7][6].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[8][6].setLink(Direction.LEFT, GameBoard.LinkState.WALL);

    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[6][5]);
    Assertions.assertFalse(checkMove);
  }

  @Test
  void IsThereNotAdjacentWallIn43From63() {
    cecilia.setCurrentTile(gameBoard.getGameState()[4][3]);
    gameBoard.getGameState()[4][3].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[4][4].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[3][3].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[3][4].setLink(Direction.DOWN, GameBoard.LinkState.WALL);

    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[5][3]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void IsThereAPawnAdjacentAndAWallBehindFrom52To41() {
    cecilia.setCurrentTile(gameBoard.getGameState()[5][2]);
    gameBoard.getGameState()[4][2].setOccupied(true);
    gameBoard.getGameState()[4][2].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[4][3].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[3][2].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[3][3].setLink(Direction.DOWN, GameBoard.LinkState.WALL);

    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[4][1]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void IsThereAWallOnLeftAndAPawnBehindItFrom56To45(){
    cecilia.setCurrentTile(gameBoard.getGameState()[5][6]);
    gameBoard.getGameState()[5][5].setOccupied(true);
    gameBoard.getGameState()[5][5].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[5][4].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][4].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][5].setLink(Direction.LEFT, GameBoard.LinkState.WALL);

    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[4][5]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void IsThereAPawnOnTheLeftAndBehindAWallFrom42To51(){
    cecilia.setCurrentTile(gameBoard.getGameState()[4][2]);
    gameBoard.getGameState()[4][1].setOccupied(true);
    gameBoard.getGameState()[4][1].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[4][0].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[5][0].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[5][1].setLink(Direction.LEFT, GameBoard.LinkState.WALL);

    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[5][1]);
    Assertions.assertTrue(checkMove);
  }
  @Test
  void IsThereAPawnBelowAndAWallBehindFrom45To53(){
    cecilia.setCurrentTile(gameBoard.getGameState()[4][5]);
    gameBoard.getGameState()[5][5].setOccupied(true);
    gameBoard.getGameState()[5][5].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[5][6].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][5].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][6].setLink(Direction.LEFT, GameBoard.LinkState.WALL);

    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[5][4]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void IsThereAPawnInFrontOfMeAndAWallBehindFrom23To34(){
    cecilia.setCurrentTile(gameBoard.getGameState()[2][3]);
    gameBoard.getGameState()[2][4].setOccupied(true);
    gameBoard.getGameState()[2][4].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[1][4].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[2][5].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[1][5].setLink(Direction.LEFT, GameBoard.LinkState.WALL);

    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[3][4]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void IsThereAPawnBelowAndAWallBehindFrom66To75(){
    cecilia.setCurrentTile(gameBoard.getGameState()[6][6]);
    gameBoard.getGameState()[7][6].setOccupied(true);
    gameBoard.getGameState()[7][6].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[7][7].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[8][5].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[8][7].setLink(Direction.UP, GameBoard.LinkState.WALL);

    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[7][5]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void IsThereAPawnBelowAndAWallBehindFrom52To63(){
    cecilia.setCurrentTile(gameBoard.getGameState()[5][2]);
    gameBoard.getGameState()[6][2].setOccupied(true);
    gameBoard.getGameState()[6][1].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][2].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[7][2].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[7][1].setLink(Direction.UP, GameBoard.LinkState.WALL);

    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[6][1]);
    Assertions.assertTrue(checkMove);
  }

  @Test
  void IsThereAPawnBelowAndAWallBehindAndAWallOnRightFrom52To63IsNotAllowed(){
    cecilia.setCurrentTile(gameBoard.getGameState()[5][2]);
    gameBoard.getGameState()[6][2].setOccupied(true);
    gameBoard.getGameState()[6][1].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][2].setLink(Direction.DOWN, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[7][2].setLink(Direction.UP, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[7][1].setLink(Direction.UP, GameBoard.LinkState.WALL);

    gameBoard.getGameState()[6][3].setLink(Direction.LEFT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[6][2].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[7][2].setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
    gameBoard.getGameState()[7][3].setLink(Direction.LEFT, GameBoard.LinkState.WALL);

    boolean checkMove = pawnMovementChecker.checkAction(gameBoard, cecilia, gameBoard.getGameState()[6][3]);
    Assertions.assertFalse(checkMove);
  }

}