package it.units.sdm.quoridor.cli;

import it.units.sdm.quoridor.model.AbstractGameBoard;
import it.units.sdm.quoridor.model.AbstractTile;


import static it.units.sdm.quoridor.utils.directions.StraightDirection.DOWN;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.RIGHT;

public class GameBoardStringBuilder {



  public static String createGameBoardString(AbstractGameBoard gameBoard) {
    String gameBoardString = "";

    gameBoardString += "\n".repeat(20);

    gameBoardString += appendTopBorderString(gameBoard);
    gameBoardString += appendCellRowsString(gameBoard);
    gameBoardString += appendBottomBorderString(gameBoard);

    return gameBoardString;
  }

  private static String appendTopBorderString(AbstractGameBoard gameBoard) {
    int sideLength = gameBoard.getSideLength();
    String result = appendTopNumbersString(sideLength);
    result += "   *" + "-----*".repeat(Math.max(0, sideLength)) + "\n";
    return result;
  }

  private static String appendTopNumbersString(int sideLength) {
    String result = "      ";
    for (int i = 0; i < sideLength; i++) {
      result += i + "     ";
    }
    result += "\n";
    return result;
  }

  private static String appendCellRowsString(AbstractGameBoard gameBoard) {
    String result = "";
    int sideLength = gameBoard.getSideLength();
    AbstractTile[][] gameState = gameBoard.getGameState();

    for (int row = 0; row < sideLength; row++) {
      result += row + "  |";

      for (int col = 0; col < sideLength; col++) {
        AbstractTile currentTile = gameState[row][col];
        String content = currentTile.isOccupiedBy().isPresent()
                ? currentTile.isOccupiedBy().get().toString()
                : "     ";
        result += content;
        result += currentTile.isThereAWallOrEdge(RIGHT) ? "|" : " ";
      }
      result += "\n";

      if (row < sideLength - 1) {
        result += "   *";
        for (int col = 0; col < sideLength - 1; col++) {
          result += gameState[row][col].isThereAWallOrEdge(DOWN) ? "-----" : "     ";
          result += "+";
        }
        result += gameState[row][sideLength - 1].isThereAWallOrEdge(DOWN) ? "-----" : "     ";
        result += "*\n";
      }
    }

    return result;
  }

  private static String appendBottomBorderString(AbstractGameBoard gameBoard) {
    int sideLength = gameBoard.getSideLength();
    return "   *" + "-----*".repeat(Math.max(0, sideLength - 1)) + "-----*\n";
  }



}
