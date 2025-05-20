package it.units.sdm.quoridor;

import it.units.sdm.quoridor.server.Logger;
import it.units.sdm.quoridor.server.QuoridorServer;

public class ServerStarter {
  public static void main(String[] args) {
    int port = 4444;      //todo passare come parametro di avvio

    try {
      int numOfPlayers = Integer.parseInt(args[0]);         //todo VALIDARE DA QUALCHE PARTE, POSSIBILMENTE TRAMITE GAME?
      String quitCommand = "q";

      QuoridorServer server = new QuoridorServer(
              port,
              quitCommand,
              numOfPlayers
      );

      server.start();
    } catch (ArrayIndexOutOfBoundsException e) {
      Logger.printLog(System.err, "Missing parameter: " + e.getMessage());
    } catch (Exception e) {
      Logger.printLog(System.err, "Server failed to start: " + e.getMessage());
    }
  }
}
