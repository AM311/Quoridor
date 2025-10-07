package it.units.sdm.quoridor;

import it.units.sdm.quoridor.exceptions.QuoridorServerException;
import it.units.sdm.quoridor.controller.server.QuoridorServer;

public class ServerStarter {
  public static void main(String[] args) {
    try {
      int port = Integer.parseInt(args[0]);

      try {
        int numOfPlayers = Integer.parseInt(args[1]);

        QuoridorServer server = new QuoridorServer(
                port,
                numOfPlayers
        );

        server.start();
      } catch (QuoridorServerException ex) {
        System.err.println("Server failure: " + ex.getMessage());
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.err.println("Missing parameter: " + ex.getMessage());
    }
  }
}
