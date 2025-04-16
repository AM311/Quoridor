package it.units.sdm.quoridor.cli;

import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractPawn;
import java.util.ArrayList;
import java.util.List;

public class GameStringBuilder {

  public static String createGameString(AbstractGame game) {
    StringBuilder gameString = new StringBuilder(game.getGameBoard().toString());
    gameString.append("\n\n").append(game.getPlayingPawn()).append("'s turn").append("\n\n");
    appendRemainingWallsInfoString(game, gameString);

    gameString.append("\n".repeat(3));

    return gameString.toString();
  }

  private static void appendRemainingWallsInfoString(AbstractGame game, StringBuilder gameString) {
    gameString.append("Remaining Walls:").append("\n\n");

    List<String> pawnInfoList = new ArrayList<>();

    for (AbstractPawn pawn : game.getPawns()) {
      int remainingWalls = pawn.getNumberOfWalls();
      pawnInfoList.add(pawn + " (" + remainingWalls + ")");

    }
    gameString.append(String.join("  -  ", pawnInfoList));

  }

}
