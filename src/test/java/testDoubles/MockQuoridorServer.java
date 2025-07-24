package testDoubles;

import it.units.sdm.quoridor.exceptions.QuoridorServerException;
import it.units.sdm.quoridor.server.QuoridorServer;

import java.io.IOException;

public class MockQuoridorServer extends QuoridorServer {
  public MockQuoridorServer(int port, int numOfPlayers) throws QuoridorServerException {
    super(port, numOfPlayers);
  }

  @Override
  public void shutdown() {
    try {
      executorService.shutdown();
      serverSocket.close();
    } catch (IOException ignored) {
    }
  }
}
