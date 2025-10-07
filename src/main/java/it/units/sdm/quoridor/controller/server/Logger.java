package it.units.sdm.quoridor.controller.server;

import java.io.PrintStream;

public class Logger {
  private Logger() {
  }

  public static void printLog(PrintStream ps, String message) {
    ps.printf("[%1$tY-%1$tm-%1$td %1$tT] %2$s %n", System.currentTimeMillis(), message);
  }
}
