package it.units.sdm.quoridor.controller.server;

import java.io.*;
import java.net.Socket;

public record Client(Socket clientSocket, BufferedReader reader, BufferedWriter writer) implements AutoCloseable {
  public Client(Socket clientSocket) throws IOException {
    this(
            clientSocket,
            new BufferedReader(new InputStreamReader(clientSocket.getInputStream())),
            new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
    );
  }

  public void write(String message) throws IOException {
    writer.write(message + System.lineSeparator());
    writer.flush();
  }

  @Override
  public String toString() {
    return "Client @ " + clientSocket;
  }

  @Override
  public void close() throws IOException {
    reader.close();
    writer.close();
    clientSocket.close();
  }
}
