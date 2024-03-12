package it.units.sdm.quoridor.utils;

import java.util.EnumSet;
import java.util.Set;

import static it.units.sdm.quoridor.utils.Directions.Direction.*;

public class Directions {
  public enum Direction {
    UP, DOWN, RIGHT, LEFT, UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT
  }

  public static Set<Direction> getStraightDirections() {
    return EnumSet.of(UP, DOWN, RIGHT, LEFT);
  }
  public static Set<Direction> getDiagonalDirections() {
    return EnumSet.of(UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT);
  }
}
