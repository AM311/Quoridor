import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameBoardTest {
  GameBoard gameBoard = new GameBoard(9);

  @Test
  void boxMatrixTest04() {
    Assertions.assertTrue(gameBoard.getBoxesMatrix()[0][4].isOccupied());
  }

  @Test
  void boxMatrixTest84() {
    Assertions.assertTrue(gameBoard.getBoxesMatrix()[8][4].isOccupied());
  }

  @Test
  void boxMatrixTest67() {
    Assertions.assertFalse(gameBoard.getBoxesMatrix()[6][7].isOccupied());
  }

  @Test
  void boxMatrixTest14() {
    Assertions.assertFalse(gameBoard.getBoxesMatrix()[1][4].isOccupied());
  }

  @Test
  void adjacencyMatrixTest04And05() {
    Assertions.assertTrue(gameBoard.getAdjacencyMatrix()[4][5]);
  }

  @Test
  void adjacencyMatrixTest23And33() {
    Assertions.assertTrue(gameBoard.getAdjacencyMatrix()[(9 * 2) + 3][(9 * 3) + 3]);
  }

  @Test
  void adjacencyMatrixTest85And84() {
    Assertions.assertTrue(gameBoard.getAdjacencyMatrix()[(9 * 8) + 5][(9 * 8) + 4]);
  }

  @Test
  void adjacencyMatrixTest88And78() {
    Assertions.assertTrue(gameBoard.getAdjacencyMatrix()[(9 * 8) + 8][(9 * 7) + 8]);
  }

  @Test
  void adjacencyMatrixTest45And56() {
    Assertions.assertFalse(gameBoard.getAdjacencyMatrix()[(9 * 4) + 5][(9 * 5) + 6]);
  }

  @Test
  void adjacencyMatrixTest77And80() {
    Assertions.assertFalse(gameBoard.getAdjacencyMatrix()[(9 * 7) + 7][9 * 8]);
  }
}
