
package it.units.sdm.quoridor.view.cli;

import it.units.sdm.quoridor.model.abstracts.AbstractGameBoard;
import it.units.sdm.quoridor.model.abstracts.AbstractTile;

import static it.units.sdm.quoridor.utils.directions.StraightDirection.DOWN;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.RIGHT;

public class GameBoardStringBuilder {

  public static String createGameBoardString(AbstractGameBoard gameBoard) {
    StringBuilder gameBoardString = new StringBuilder();

    appendTopBorderString(gameBoard, gameBoardString);
    appendCellRowsString(gameBoard, gameBoardString);
    appendBottomBorderString(gameBoard, gameBoardString);

    return gameBoardString.toString();
  }

  private static void appendTopBorderString(AbstractGameBoard gameBoard, StringBuilder gameBoardString) {
    int sideLength = gameBoard.getSideLength();
    appendTopNumbersString(sideLength, gameBoardString);

    gameBoardString.append("   *").append("-----*".repeat(Math.max(0, sideLength))).append("\n");
  }


  private static void appendTopNumbersString(int sideLength, StringBuilder gameBoardString) {
    gameBoardString.append("      ");

    for (int i = 0; i < sideLength; i++) {
      gameBoardString.append(i).append("     ");
    }
    gameBoardString.append("\n");
  }


  private static void appendCellRowsString(AbstractGameBoard gameBoard, StringBuilder gameBoardString) {
    int sideLength = gameBoard.getSideLength();
    AbstractTile[][] gameState = gameBoard.getGameState();

    for (int row = 0; row < sideLength; row++) {
      gameBoardString.append(row).append("  |");

      for (int col = 0; col < sideLength; col++) {
        AbstractTile currentTile = gameState[row][col];

        gameBoardString.append(currentTile.isOccupiedBy().isPresent() ? currentTile.isOccupiedBy().get() : "     ")
                .append(currentTile.isThereAWallOrEdge(RIGHT) ? "|" : " ");
      }
      gameBoardString.append("\n");

      if (row < sideLength - 1) {
        gameBoardString.append("   *");
        for (int col = 0; col < sideLength - 1; col++) {
          gameBoardString.append(gameState[row][col].isThereAWallOrEdge(DOWN) ? "-----" : "     ").append("+");
        }
        gameBoardString.append(gameState[row][sideLength - 1].isThereAWallOrEdge(DOWN) ? "-----" : "     ").append("*").append("\n");
      }
    }
  }


  private static void appendBottomBorderString(AbstractGameBoard gameBoard, StringBuilder gameBoardString) {
    int sideLength = gameBoard.getSideLength();
    gameBoardString.append("   *").append("-----*".repeat(Math.max(0, sideLength - 1))).append("-----*").append("\n");
  }

}
