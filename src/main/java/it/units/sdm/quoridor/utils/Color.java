package it.units.sdm.quoridor.utils;

public enum Color {
  
  BLUE(java.awt.Color.BLUE, "\033[44m"),
  CYAN(java.awt.Color.CYAN, "\033[46m"),
  BLACK(java.awt.Color.BLACK, "\033[40m"),
  RED(java.awt.Color.RED, "\033[41m"),
  WHITE(java.awt.Color.WHITE, "\033[107m"),
  GREEN(java.awt.Color.GREEN, "\033[42m"),
  MAGENTA(java.awt.Color.MAGENTA, "\033[45m"),
  YELLOW(java.awt.Color.YELLOW, "\033[33m");

  final java.awt.Color color;
  final String ansiEscapeCode;

  Color(java.awt.Color color, String ansiEscapeCode) {
    this.color = color;
    this.ansiEscapeCode = ansiEscapeCode;
  }

  public String getAnsiEscapeCode() {
    return ansiEscapeCode;
  }
}
