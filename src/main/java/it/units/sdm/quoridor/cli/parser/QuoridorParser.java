package it.units.sdm.quoridor.cli.parser;

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

  protected void verifyNumberOfParameters(int num) throws ParserException {
    if (commandTokens.length - 1 != num)
      throw new ParserException("Wrong number of parameters provided for this command!");
  }

  abstract public Optional<CommandType> getCommandType();

  abstract public Optional<Position> getActionPosition();

  abstract public Optional<WallOrientation> getWallOrientation();

  abstract public String toString();

  public enum CommandType {
    QUIT, MOVE, WALL, HELP, RESTART
  }
}
