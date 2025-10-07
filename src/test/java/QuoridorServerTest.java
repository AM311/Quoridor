import it.units.sdm.quoridor.exceptions.QuoridorServerException;
import it.units.sdm.quoridor.controller.server.QuoridorServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import testDoubles.MockQuoridorServer;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Execution(ExecutionMode.SAME_THREAD)
class QuoridorServerTest {
  private static final int testPort = 12345;
  private QuoridorServer server;
  private ExecutorService exec;

  @AfterEach
  void tearDown() {
    if (server != null) {
      server.shutdown();
    }
    if (exec != null) {
      exec.shutdownNow();
    }
  }

  @BeforeEach
  void prepareExecutor() {
    exec = Executors.newSingleThreadExecutor();
  }

  @Test
  void constructorTest_parametersAreSetCorrectly() throws QuoridorServerException {
    setupServer(2);

    assertEquals(testPort, server.getPort());
    assertEquals(2, server.getNumOfPlayers());
  }

  private void setupServer(int numberOfPlayers) throws QuoridorServerException {
    server = new MockQuoridorServer(testPort, numberOfPlayers);
  }

  @Test
  void constructorTest_initialPlayerIsSetCorrectly() throws Exception {
    setupServer(2);

    assertEquals(1, server.getCurrentPlayer());
  }

  @Test
  void testClientListAddMockClient() throws Exception {
    setupServer(2);

    exec.submit(() -> server.start());

    Thread.sleep(600);

    try (Socket ignored = new Socket("localhost", testPort)) {
      Thread.sleep(600);

      assertEquals(1, server.getClientList().size());
    }
  }

  @Test
  void testStartAcceptsExactClientsAndBlocksExtra() throws Exception {
    setupServer(2);

    exec.submit(() -> server.start());

    Thread.sleep(600);

    try (Socket ignored1 = new Socket("localhost", testPort)) {
      Thread.sleep(600);

      try (Socket ignored2 = new Socket("localhost", testPort)) {
        Thread.sleep(600);

        try (Socket ignored3 = new Socket("localhost", testPort)) {
          Thread.sleep(600);
          assertEquals(2, server.getClientList().size());
        }
      }
    }
  }


  @Test
  void testMessageExchangeBetweenServerAndClient() throws Exception {
    setupServer(1);

    exec.submit(() -> server.start());

    try (Socket clientSocket = new Socket("localhost", testPort)) {

      BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      String line1 = clientReader.readLine();
      String line2 = clientReader.readLine();
      String line3 = clientReader.readLine();

      assertEquals("READY", line1);
      assertTrue(line2.contains("You are player"));
      assertEquals("1", line3);

      String play = clientReader.readLine();
      assertEquals("PLAY", play);
    }
  }

  @Test
  void testMessageExchangeBetweenServerAndClient2() throws Exception {
    setupServer(2);

    exec.submit(() -> server.start());

    try (Socket clientSocket1 = new Socket("localhost", testPort)) {
      try (Socket clientSocket2 = new Socket("localhost", testPort)) {

        BufferedWriter clientWriter1 = new BufferedWriter(new OutputStreamWriter(clientSocket1.getOutputStream()));
        BufferedReader clientReader1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));

        BufferedReader clientReader2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));

        clientReader1.readLine();
        clientReader1.readLine();
        clientReader1.readLine();

        clientReader2.readLine();
        clientReader2.readLine();
        clientReader2.readLine();

        clientReader1.readLine();

        clientWriter1.write("MOCK_MOVE" + System.lineSeparator());
        clientWriter1.flush();

        String cmd = clientReader2.readLine();

        Assertions.assertEquals("MOCK_MOVE", cmd);
      }
    }
  }

  @Test
  void testServerReadsOnlyFromCurrentPlayer() throws Exception {
    setupServer(3);

    exec.submit(() -> server.start());

    try (Socket clientSocket1 = new Socket("localhost", testPort)) {
      try (Socket clientSocket2 = new Socket("localhost", testPort)) {
        try (Socket clientSocket3 = new Socket("localhost", testPort)) {

          BufferedWriter clientWriter1 = new BufferedWriter(new OutputStreamWriter(clientSocket1.getOutputStream()));
          BufferedReader clientReader1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));

          BufferedWriter clientWriter2 = new BufferedWriter(new OutputStreamWriter(clientSocket2.getOutputStream()));
          BufferedReader clientReader2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));

          BufferedReader clientReader3 = new BufferedReader(new InputStreamReader(clientSocket3.getInputStream()));

          clientReader1.readLine();
          clientReader1.readLine();
          clientReader1.readLine();

          clientReader2.readLine();
          clientReader2.readLine();
          clientReader2.readLine();

          clientReader3.readLine();
          clientReader3.readLine();
          clientReader3.readLine();

          clientReader1.readLine();

          clientWriter2.write("MOCK_MOVE_2" + System.lineSeparator());
          clientWriter2.flush();

          clientWriter1.write("MOCK_MOVE_1" + System.lineSeparator());
          clientWriter1.flush();

          String cmd = clientReader3.readLine();

          Assertions.assertEquals("MOCK_MOVE_1", cmd);
        }
      }
    }
  }

  @Test
  void testClientDisconnects() throws Exception {
    setupServer(2);

    exec.submit(() -> server.start());

    try (Socket clientSocket1 = new Socket("localhost", testPort)) {
      try (Socket clientSocket2 = new Socket("localhost", testPort)) {

        BufferedWriter clientWriter1 = new BufferedWriter(new OutputStreamWriter(clientSocket1.getOutputStream()));
        BufferedReader clientReader1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));

        BufferedReader clientReader2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));

        clientReader1.readLine();
        clientReader1.readLine();
        clientReader1.readLine();

        clientReader2.readLine();
        clientReader2.readLine();
        clientReader2.readLine();

        clientReader1.readLine();

        clientSocket2.close();

        clientWriter1.write("MOCK_MOVE" + System.lineSeparator());
        clientWriter1.flush();

        String cmd = clientReader1.readLine();

        Assertions.assertEquals("Q", cmd);
      }
    }
  }
}
