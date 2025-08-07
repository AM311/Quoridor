package it.units.sdm.quoridor.utils;

public record Position(int row, int column) {
  @Override
  public String toString() {
    return row + "," + column;
  }
}
