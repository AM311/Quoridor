package it.units.sdm.quoridor;

import it.units.sdm.quoridor.exceptions.QuoridorServerException;
import it.units.sdm.quoridor.server.QuoridorServer;

public class ServerStarter {
  public static void main(String[] args) {
    int port = Integer.parseInt(args[0]);

    try {
      int numOfPlayers = Integer.parseInt(args[1]);

      QuoridorServer server = new QuoridorServer(
              port,
              numOfPlayers
      );

      server.start();
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.err.println("Missing parameter: " + ex.getMessage());
    } catch (QuoridorServerException ex) {
      System.err.println("Server failure: " + ex.getMessage());
    }
  }
}
