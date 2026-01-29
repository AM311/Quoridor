package it.units.sdm.quoridor.utils;

public enum Color {
  CYAN(java.awt.Color.CYAN, "CYAN","\033[46m"),
  RED(java.awt.Color.RED, "RED","\033[41m"),
  WHITE(java.awt.Color.WHITE, "WHITE","\033[107m"),
  GREEN(java.awt.Color.GREEN, "GREEN","\033[42m"),
  MAGENTA(java.awt.Color.MAGENTA, "MAGENTA","\033[45m");

  final private java.awt.Color color;
  final private String name;
  final private String ansiEscapeCode;

  Color(java.awt.Color color, String name, String ansiEscapeCode) {
    this.color = color;
    this.name = name;
    this.ansiEscapeCode = ansiEscapeCode;
  }

  public String getAnsiEscapeCode() {
    return ansiEscapeCode;
  }

  public java.awt.Color getColor() {
    return color;
  }

  public String getName() {
    return name;
  }
}
