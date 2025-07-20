package it.units.sdm.quoridor.server;

import it.units.sdm.quoridor.exceptions.QuoridorServerException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class QuoridorServer {
  private final int port;
  private final ExecutorService executorService = Executors.newCachedThreadPool();
  private final int numOfPlayers;
  private final Lock lock = new ReentrantLock();
  private final Condition turnChanged = lock.newCondition();
  private final AtomicInteger currentPlayer = new AtomicInteger(1);
  private final List<Socket> clientList = Collections.synchronizedList(new ArrayList<>());
  private final ServerSocket serverSocket;

  public QuoridorServer(int port, int numOfPlayers) throws QuoridorServerException {
    this.port = port;
    this.numOfPlayers = numOfPlayers;

    try {
      this.serverSocket = new ServerSocket(port);
    } catch (IOException ex) {
      throw new QuoridorServerException("Exception while opening ServerSocket: " + ex.getMessage());
    }
  }

  public int getPort() {
    return port;
  }

  public int getNumOfPlayers() {
    return numOfPlayers;
  }

  public List<Socket> getClientList() {
    return clientList;
  }

  public int getCurrentPlayer() {
    return currentPlayer.intValue();
  }

  public void start() {
    try {
      Logger.printLog(System.out, "Welcome to Quoridor! The server started!");
      Logger.printLog(System.out, "Waiting for " + numOfPlayers + " players");
      CountDownLatch latch = new CountDownLatch(1);

      while (true) {
        Thread.sleep(1000);

        if (clientList.size() == numOfPlayers) {
          latch.countDown();
        }

        try {
          Socket client = serverSocket.accept();
          if (clientList.size() >= numOfPlayers) {
            client.close();
            Logger.printLog(System.err, "Unable to accept connection: the game has already started!");
          } else {
            executorService.submit(() -> {
              try (client) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                clientList.add(client);
                Logger.printLog(System.out, "New connection: " + client.getInetAddress());
                Logger.printLog(System.out, "Waiting for " + (numOfPlayers - clientList.size()) + " more players");
                latch.await();
                int playerNumber = (clientList.indexOf(client) + 1);
                writer.write("READY" + System.lineSeparator());
                writer.write("You are player " + playerNumber + System.lineSeparator());
                writer.write(numOfPlayers + System.lineSeparator());
                writer.flush();

                while (true) {
                  waitForRound(playerNumber);
                  writer.write("PLAY" + System.lineSeparator());
                  writer.flush();
                  String request = reader.readLine();

                  notifyClients(request);

                  if (request.equals("Q")) {
                    Thread.sleep(1000);
                    shutdown();
                  }
                  if (request.equals("R")) {
                    currentPlayer.set(1);
                  }

                  nextRound();
                }
              } catch (InterruptedException ex) {
                Logger.printLog(System.err, "Thread managing " + client.getInetAddress() + " has been interrupted");
              } catch (IOException ex) {
                Logger.printLog(System.err, "Client " + client.getInetAddress() + " abruptly closed connection.");
              } catch (RuntimeException ex) {
                Logger.printLog(System.err, "Unhandled RuntimeException while managing " + client.getInetAddress() + ": " + ex.getMessage());
              } catch (Error er) {
                Logger.printLog(System.err, "ERROR while managing " + client.getInetAddress() + ": " + er.getMessage());
              } finally {
                clientList.remove(client);
                Logger.printLog(System.out, "Closed connection: " + client.getInetAddress());

                if (latch.getCount() == 0) {
                  try {
                    notifyClients("Q" + System.lineSeparator());
                    Thread.sleep(1000);
                  } catch (IOException ex) {
                    Logger.printLog(System.err, "IOException: " + ex.getMessage());
                  } catch (InterruptedException ex) {
                    Logger.printLog(System.err, "Thread managing " + client.getInetAddress() + " has been interrupted");
                  }

                  shutdown();
                }
              }
            });
          }
        } catch (IOException ex) {
          Logger.printLog(System.err, "Exception while accepting a new client: " + ex.getMessage());
        }
      }
    } catch (InterruptedException ex) {
      Logger.printLog(System.err, "Exception while handling Sleep: " + ex.getMessage());
    } finally {
      executorService.shutdown();
    }
  }

  private void waitForRound(int playerNumber) throws InterruptedException {
    lock.lock();

    while (currentPlayer.get() != playerNumber) {
      turnChanged.await();
    }

    lock.unlock();
  }

  private void notifyClients(String message) throws IOException {
    for (Socket client : clientList) {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
      writer.write(message + System.lineSeparator());
      writer.flush();
    }
  }

  public void shutdown() {

    try {
      executorService.shutdown();
      serverSocket.close();
    } catch (IOException ignored) {
    }

    //System.exit(0);
  }

  private void nextRound() {
    lock.lock();

    currentPlayer.set((currentPlayer.get() % numOfPlayers) + 1);
    turnChanged.signalAll();

    lock.unlock();
  }
}
