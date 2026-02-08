package it.units.sdm.quoridor.view.cli;


import it.units.sdm.quoridor.utils.Color;
import it.units.sdm.quoridor.view.PawnAppearance;

public class InformationStringBuilder {
  private final StringBuilder stringBuilder = new StringBuilder();

  public static InformationStringBuilder start() {
    return new InformationStringBuilder();
  }

  public InformationStringBuilder appendTitle() {
    stringBuilder.append("""
            ===============================================================
              ____    _    _    ___    ____    ___   ____     ___    ____
             / __ \\  | |  | |  / _ \\  |  _ \\  |_ _| |  _ \\   / _ \\  |  _ \\
            | |  | | | |  | | | | | | | |_) |  | |  | | | | | | | | | |_) |
            | |  | | | |  | | | | | | |  _ <   | |  | | | | | | | | |  _ <
            | |__| | | |__| | | |_| | | | | |  | |  | |_| | | |_| | | | | |
             \\___\\_\\  \\____/   \\___/  |_| |_| |___| |____/   \\___/  |_| |_|
            
            ===============================================================
            """).append(System.lineSeparator());

    return this;
  }

  public InformationStringBuilder appendGameRules() {
    stringBuilder.append("HOW TO PLAY QUORIDOR:").append(System.lineSeparator());

    stringBuilder.append("\t- On your round, you can move your pawn or place a wall;").append(System.lineSeparator());
    stringBuilder.append("\t- You win by reaching the opposite side of the board;").append(System.lineSeparator());
    stringBuilder.append("\t- You cannot completely block your opponent's path to destination;").append(System.lineSeparator().repeat(2));

    return this;
  }

  public InformationStringBuilder appendWallsConvention() {
    final PawnAppearance demoPawn = new PawnAppearance(Color.WHITE);

    String figure = "WALL PLACEMENT CONVENTION:" + System.lineSeparator() +
            "     +     +     +        " + System.lineSeparator() +
            "     |                    " + System.lineSeparator() +
            "  v  +     +     +        " + System.lineSeparator() +
            "     |" + demoPawn + System.lineSeparator() +
            "     +-----+-----+        " + System.lineSeparator() +
            "           h              " + System.lineSeparator() +
            "                          " + System.lineSeparator();

    stringBuilder.append(figure);
    stringBuilder.append("\tThe chosen tile is in the bottom-left.").append(System.lineSeparator());
    stringBuilder.append("\tWalls occupy two tiles:").append(System.lineSeparator());
    stringBuilder.append("\t\t- Vertical walls appear on the left side of tiles;").append(System.lineSeparator());
    stringBuilder.append("\t\t- Horizontal walls appear on the bottom side of tiles;").append(System.lineSeparator());
    stringBuilder.append("\tYou cannot place walls on the margins.").append(System.lineSeparator().repeat(2));

    return this;
  }

  public String build() {
    return stringBuilder.toString();
  }
}
