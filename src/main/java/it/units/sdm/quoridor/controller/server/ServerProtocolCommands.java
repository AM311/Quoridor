package it.units.sdm.quoridor.controller.server;

public enum ServerProtocolCommands {
  READY("READY"), PLAY("PLAY");

  private final String commandString;

  ServerProtocolCommands(String commandString) {
    this.commandString = commandString;
  }

  public String getCommandString() {
    return commandString;
  }
}
