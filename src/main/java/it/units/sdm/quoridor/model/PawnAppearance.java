package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.utils.Color;

import java.util.List;
import java.util.stream.Collectors;

public record PawnAppearance(Color color) {
  private static final List<Color> defaultColors = List.of(Color.RED, Color.BLUE, Color.GREEN, Color.WHITE);

  public static List<PawnAppearance> getDefaultPawnStyles() {
    return defaultColors.stream().map(PawnAppearance::new)
            .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    String RESET = "\u001B[0m";
    return color.getAnsiEscapeCode() + "     " + RESET;
  }
}

