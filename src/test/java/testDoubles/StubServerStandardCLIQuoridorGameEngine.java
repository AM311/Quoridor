package testDoubles;

import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

public class StubServerStandardCLIQuoridorGameEngine extends StubStandardCLIQuoridorGameEngine {

  private final BufferedReader socketReader;
  private final BufferedWriter socketWriter;
  private boolean isIOExceptionCaught;

  public StubServerStandardCLIQuoridorGameEngine(BufferedReader reader, QuoridorParser parser, AbstractQuoridorBuilder builder, BufferedWriter socketWriter, BufferedReader socketReader) {
    super(reader, parser, builder);
    this.socketWriter = socketWriter;
    this.socketReader = socketReader;
  }

  @Override
  protected void executeRound() throws BuilderException {
    isRoundCompleted = false;
    boolean commandExecuted = false;
    String serverMessage = null;

    try {
      serverMessage = socketReader.readLine();
    } catch (IOException e) {
      isIOExceptionCaught = true;
    }

    do {
      try {
        if (Objects.equals(serverMessage, "PLAY")) {
          String command = askCommand();
          commandExecuted = performCommand(command);
          if (commandExecuted) {
            socketWriter.write(command + System.lineSeparator());
            socketWriter.flush();
            socketReader.readLine();
          }
        } else {
          commandExecuted = performCommand(serverMessage);
        }
      } catch (IOException e) {
        quitGame();
      } catch (InvalidActionException e) {
        isInvalidActionExceptionCaught = true;
        return;
      } catch (InvalidParameterException e) {
        isInvalidParameterExceptionCaught = true;
        return;
      } catch (ParserException e) {
        isParserExceptionCaught = true;
        return;
      }
    } while (!commandExecuted);

    isRoundCompleted = true;
  }

  @Override
  protected void handleEndGame() {
    isGameEnded = true;

    switch (parser.getCommandType().orElseThrow()) {
      case QUIT -> isGameQuit = true;
      case RESTART -> isGameRestarted = true;
    }
  }
}


