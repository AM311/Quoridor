package it.units.sdm.quoridor.view.cli;

import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.abstracts.AbstractPawn;

import java.util.ArrayList;
import java.util.List;

public class GameStringBuilder {
  public static String buildGameString(AbstractGame game) {
    StringBuilder gameString = new StringBuilder(System.lineSeparator().repeat(20));

    gameString.append(game.getGameBoard().toString());
    gameString.append(System.lineSeparator().repeat(2));
    gameString.append(game.getPlayingPawn()).append("'S ROUND");
    gameString.append(System.lineSeparator().repeat(2));

    appendRemainingWallsInfoString(game, gameString);
    gameString.append(System.lineSeparator());

    return gameString.toString();
  }

  private static void appendRemainingWallsInfoString(AbstractGame game, StringBuilder gameString) {
    gameString.append("Remaining Walls:").append(System.lineSeparator());

    List<String> pawnInfoList = new ArrayList<>();
    List<AbstractPawn> pawns = game.getPawns();

    for (AbstractPawn pawn : pawns) {
      int remainingWalls = pawn.getNumberOfWalls();
      pawnInfoList.add("\t" + pawn + ": " + remainingWalls + " walls");
    }

    gameString.append(String.join(System.lineSeparator(), pawnInfoList));
  }
}