package it.units.sdm.quoridor.utils;

public enum Color {
  BLUE(java.awt.Color.BLUE, "\\u001B[44m"),
  CYAN(java.awt.Color.CYAN, "\\u001B[46m"),
  BLACK(java.awt.Color.BLACK, "\\u001B[40m"),
  RED(java.awt.Color.RED, "\\u001B[41m"),
  WHITE(java.awt.Color.WHITE, "\\u001B[41m"),
  GREEN(java.awt.Color.GREEN, "\\u001B[42m"),
  MAGENTA(java.awt.Color.MAGENTA, "\\u001B[45m"),
  YELLOW(java.awt.Color.YELLOW, "\\u001B[33m");

  final java.awt.Color color;
  final String ansiEscapeCode;

  Color(java.awt.Color color, String ansiEscapeCode) {
    this.color = color;
    this.ansiEscapeCode = ansiEscapeCode;
  }

  public java.awt.Color getColor() {
    return color;
  }

  public String getAnsiEscapeCode() {
    return ansiEscapeCode;
  }
}
