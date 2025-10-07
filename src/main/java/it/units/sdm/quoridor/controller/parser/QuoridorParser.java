package it.units.sdm.quoridor.controller.parser;

import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.Optional;

public abstract class QuoridorParser {
  protected String[] commandTokens;
  protected CommandType commandType;
  protected Position position;
  protected WallOrientation wallOrientation;

  public final void parse(String command) throws ParserException {
    this.commandTokens = command.toUpperCase().split("\\s+");
    initializeFieldsToNull();

    try {
      this.commandType = switch (commandTokens[0]) {
        case "Q" -> {
          verifyNumberOfParameters(0);
          yield CommandType.QUIT;
        }
        case "R" -> {
          verifyNumberOfParameters(0);
          yield CommandType.RESTART;
        }

        default -> parseSpecific();
      };
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new ParserException("No command found!");
    }
  }

  protected abstract CommandType parseSpecific() throws ParserException;

  protected void initializeFieldsToNull() {
    this.commandType = null;
    this.position = null;
    this.wallOrientation = null;
  }

  protected void verifyNumberOfParameters(int expected) throws ParserException {
    int actual = commandTokens.length - 1;
    if (commandTokens.length - 1 != expected)
      throw new ParserException("Wrong number of parameters provided for this command: " + expected + " expected, " + actual + " given.");
  }

  abstract public Optional<CommandType> getCommandType();

  abstract public Optional<Position> getActionPosition();

  abstract public Optional<WallOrientation> getWallOrientation();

  abstract public String toString();

  abstract public String generateString(CommandType commandType, Position position, WallOrientation wallOrientation) ;

  public enum CommandType {
    QUIT, MOVE, WALL, HELP, RESTART
  }
}
