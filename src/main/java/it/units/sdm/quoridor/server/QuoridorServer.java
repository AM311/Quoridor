package it.units.sdm.quoridor.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class QuoridorServer {
  private final int port;
  private final String quitCommand;
  private final ExecutorService executorService;
  private final int numOfPlayers;
  private final Lock lock = new ReentrantLock();
  private final Condition turnChanged = lock.newCondition();
  private AtomicInteger currentPlayer = new AtomicInteger(1);
  private Queue<Socket> clientQueue = new ConcurrentLinkedQueue<>();
  //todo implementare la clientQueue per inviare un messaggio a ogni client quando si deve eseguire una mossa, usare la sua lunghezza invecere di currentClients
  public QuoridorServer(int port, String quitCommand, ExecutorService executorService, int numOfPlayers) {
    this.port = port;
    this.quitCommand = quitCommand;
    this.executorService = executorService;
    this.numOfPlayers = numOfPlayers;
  }

  public int getPort() {
    return port;
  }

  public String getQuitCommand() {
    return quitCommand;
  }

  public ExecutorService getExecutorService() {
    return executorService;
  }

  public int getNumOfPlayers() {
    return numOfPlayers;
  }

  public void start() throws IOException {
    AtomicInteger currentClients = new AtomicInteger();

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      Logger.printLog(System.out, "Welcome to Quoridor! The server started!");
      Logger.printLog(System.out, "Waiting for " + numOfPlayers + " players");
      CountDownLatch latch = new CountDownLatch(1);

      while (true) {
        if (currentClients.get() == numOfPlayers) {
          latch.countDown();
        }
        try {
          Socket socket = serverSocket.accept();
          if (currentClients.get() >= numOfPlayers) {
            socket.close();
            throw new Exception("The game has already started!");
          }

          executorService.submit(() -> {
            try (socket) {
              int playerNumber = currentClients.incrementAndGet();
              BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
              BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

              Logger.printLog(System.out, "New connection: " + socket.getInetAddress());
              Logger.printLog(System.out, "Waiting for " + (numOfPlayers - currentClients.get()) + " more players");
              writer.write("You are player " + playerNumber + System.lineSeparator());
              latch.await();

              while (true) {
                waitForTurn(playerNumber);
                String request = reader.readLine();

                if (request.equals(quitCommand)) {
                  break;
                }

                //writer.write(requestsProcessor.process(request) + System.lineSeparator());
                writer.flush();
              }
            } catch (InterruptedException ex) {
              Logger.printLog(System.err, "Thread managing " + socket.getInetAddress() + " has been interrupted");
            } catch (NullPointerException ex) {
              Logger.printLog(System.err, "Client " + socket.getInetAddress() + " abruptly closed connection.");
            } catch (IOException ex) {
              Logger.printLog(System.err, "Unhandled IO exception: " + ex.getMessage());
            } catch (RuntimeException ex) {
              Logger.printLog(System.err, "Unhandled RuntimeException while managing " + socket.getInetAddress() + ": " + ex.getMessage());
            } catch (Error er) {
              Logger.printLog(System.err, "ERROR while managing " + socket.getInetAddress() + ": " + er.getMessage());
            }

            currentClients.getAndDecrement();
            Logger.printLog(System.out, "Closed connection: " + socket.getInetAddress());
          });
        } catch (RuntimeException ex) {
          Logger.printLog(System.err, "RuntimeException trying managing new client connection: " + ex.getMessage());
        } catch (Exception ex) {
          Logger.printLog(System.err, "Unable to accept connection: " + ex.getMessage());
        }
      }
    } catch (IOException | RuntimeException ex) {
      throw ex;
    } catch (Exception ex) {
      Logger.printLog(System.err, "Exception trying closing resources: " + ex);
    } finally {
      executorService.shutdown();
    }
  }

  public void waitForTurn(int playerNumber) throws InterruptedException {
    lock.lock();
    try {
      while (currentPlayer.get() != playerNumber) {
        turnChanged.await();
      }
    } finally {
      lock.unlock();
    }
  }

  public void nextTurn() {
    lock.lock();
    try {
      currentPlayer.set((currentPlayer.get() % numOfPlayers) + 1);
      turnChanged.signalAll();
    } finally {
      lock.unlock();
    }
  }
}
