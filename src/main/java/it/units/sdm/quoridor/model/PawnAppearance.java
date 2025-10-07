package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.utils.Color;

import java.util.List;
import java.util.stream.Collectors;

public record PawnAppearance(Color color) {
  public static final String RESET_STRING = "\u001B[0m";
  private static final List<Color> defaultColors = List.of(Color.RED, Color.CYAN, Color.GREEN, Color.MAGENTA);

  public static List<PawnAppearance> getDefaultPawnStyles() {
    return defaultColors.stream().map(PawnAppearance::new)
            .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return " " + color.getAnsiEscapeCode() + "   " + RESET_STRING + " ";
  }
}

