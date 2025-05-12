package it.units.sdm.quoridor;

import it.units.sdm.quoridor.server.Logger;
import it.units.sdm.quoridor.server.QuoridorServer;

import java.util.concurrent.Executors;

public class ServerStarter {
  public static void main(String[] args) {
    int port = 4444;
    int numOfPlayers = Integer.parseInt(args[0]);
    String quitCommand = "q";

    QuoridorServer server = new QuoridorServer(
            port,
            quitCommand,
            Executors.newCachedThreadPool(),
            numOfPlayers
    );

    try {
      server.start();
    } catch (Exception e) {
      Logger.printLog(System.err, "Server failed to start: " + e.getMessage());
    }
  }
}
