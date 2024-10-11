package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.utils.Color;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record PawnAppearance(Color color, char symbol) {
  private static final List<Color> defaultColors = List.of(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);
  private static final List<Character> defaultSymbols = List.of('X', 'O', '#', '@');

  public static List<PawnAppearance> getDefaultPawnStyles() {
    return IntStream.range(0, defaultColors.size())
            .mapToObj(i -> new PawnAppearance(defaultColors.get(i), defaultSymbols.get(i)))
            .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    String RESET = "\\u001B[0m";

    return color.getAnsiEscapeCode() + symbol + RESET;
  }
}
