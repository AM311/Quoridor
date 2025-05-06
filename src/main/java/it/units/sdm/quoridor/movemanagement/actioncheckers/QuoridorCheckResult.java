package it.units.sdm.quoridor.movemanagement.actioncheckers;

public enum QuoridorCheckResult implements CheckResult {
  OKAY, END_OF_AVAILABLE_WALLS, INVALID_WALL_POSITION, BLOCKING_WALL, INVALID_MOVEMENT, OCCUPIED_TILE
}
