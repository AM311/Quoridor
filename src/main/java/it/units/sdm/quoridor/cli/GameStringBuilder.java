package it.units.sdm.quoridor.cli;

import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractPawn;
import java.util.ArrayList;
import java.util.List;

public class GameStringBuilder {

  public static String createGameString(AbstractGame game) {
    String gameString = "";

    gameString += game.getGameBoard().toString();
    gameString += "\n\n";

    gameString += game.getPlayingPawn() + "'s turn\n\n";

    gameString += "Remaining Walls:\n\n";

    List<String> pawnInfoList = new ArrayList<>();
    for (AbstractPawn pawn : game.getPawns()) {
      int remainingWalls = pawn.getNumberOfWalls();
      pawnInfoList.add(pawn + " (" + remainingWalls + ")");
    }

    gameString += String.join("  -  ", pawnInfoList);

    return gameString;
  }

}
