package it.units.sdm.quoridor.cli.engine;

import java.util.Scanner;

public class CLIInputProvider implements InputProvider {
  private final Scanner scanner = new Scanner(System.in);

  @Override
  public String nextLine() {
    return scanner.nextLine();
  }
}
