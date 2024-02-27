public class GameBoard {
  private final int sideLength;
  private final boolean[][] adjacencyMatrix;
  private final Box[][] boxesMatrix;

  public GameBoard(int sideLength) {
    this.sideLength = sideLength;
    boxesMatrix = new Box[sideLength][sideLength];
    fillBoxesMatrix();
    adjacencyMatrix = new boolean[(int) Math.pow(sideLength, 2)][(int) Math.pow(sideLength, 2)];
    fillAdjacencyMatrix();
  }

  private void fillAdjacencyMatrix() {
    for (int i = 0; i < Math.pow(sideLength, 2); i++) {
      for (int j = 0; j < Math.pow(sideLength, 2); j++) {
        int row1 = i / 9;
        int column1 = i % 9;
        int row2 = j / 9;
        int column2 = j % 9;
        if ((Math.abs(row1 - row2) == 1 && Math.abs(column1 - column2) == 0)
                || (Math.abs(row1 - row2) == 0 && Math.abs(column1 - column2) == 1)) {
          adjacencyMatrix[i][j] = true;
        }
      }
    }
  }

  private void fillBoxesMatrix() {
    for (int i = 0; i < sideLength; i++) {
      for (int j = 0; j < sideLength; j++) {
        boxesMatrix[i][j] = new Box((i == 0 && j == 4) || (i == 8 && j == 4));
      }
    }
  }

  public int getSideLength() {
    return sideLength;
  }

  public boolean[][] getAdjacencyMatrix() {
    return adjacencyMatrix;
  }

  public Box[][] getBoxesMatrix() {
    return boxesMatrix;
  }
}
