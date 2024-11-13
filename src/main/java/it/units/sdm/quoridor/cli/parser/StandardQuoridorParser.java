package it.units.sdm.quoridor.cli.parser;

import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.Optional;

public class StandardQuoridorParser implements QuoridorParser {
  private String[] commandTokens;

  private CommandType commandType;
  private Position position;
  private WallOrientation wallOrientation;

  public void acceptAndParse(String command) throws ParserException {
    this.commandTokens = command.toUpperCase().split("\\s+");

    try {
      this.commandType = switch (commandTokens[0]) {
        case "Q" -> {
          verifyNumberOfParameters(0);
          yield CommandType.QUIT;
        }
        case "M" -> {
          verifyNumberOfParameters(1);
          parseActionPosition(commandTokens[1].split(","));
          yield CommandType.MOVE;
        }
        case "W" -> {
          verifyNumberOfParameters(2);
          parseActionPosition(commandTokens[1].split(","));
          parseWallOrientation(commandTokens[2]);
          yield CommandType.WALL;
        }

        default -> throw new ParserException("Unexpected value for Action Type: " + commandTokens[0]);
      };
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new ParserException("No command found!");
    }
  }

  private void verifyNumberOfParameters(int num) throws ParserException {
    if (commandTokens.length - 1 != num)
      throw new ParserException("Wrong number of parameters provided for this command!");
  }

  @Override
  public Optional<CommandType> getCommandType() {
    return Optional.ofNullable(commandType);
  }

  @Override
  public Optional<Position> getActionPosition() {
    return Optional.ofNullable(position);
  }

  @Override
  public Optional<WallOrientation> getWallOrientation() {
    return Optional.ofNullable(wallOrientation);
  }

  private void parseActionPosition(String[] actionPosition) throws ParserException {
    if (actionPosition.length != 2) {
      throw new ParserException("Invalid position: you must provide two arguments!");
    }

    try {
      int row = Integer.parseInt(actionPosition[0]);
      int col = Integer.parseInt(actionPosition[1]);

      this.position = new Position(row, col);
    } catch (NumberFormatException exception) {
      throw new ParserException("Invalid position: you must provide two integers!");
    }
  }

  private void parseWallOrientation(String wallOrientation) throws ParserException {
    this.wallOrientation = switch (commandTokens[2]) {
      case "V" -> WallOrientation.VERTICAL;
      case "H" -> WallOrientation.HORIZONTAL;
      default -> throw new ParserException("Unrecognized wall orientation: " + wallOrientation);
    };
  }
}
