package it.units.sdm.quoridor.controller.parser;

import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.Optional;

public class StandardQuoridorParser extends QuoridorParser {
  @Override
  protected CommandType parseSpecific() throws ParserException {
    return switch (commandTokens[0]) {
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
      case "H" -> {
        verifyNumberOfParameters(0);
        yield CommandType.HELP;
      }

      default -> throw new ParserException("Unexpected value for Action Type: " + commandTokens[0]);
    };
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

  @Override
  public String generateString(CommandType commandType, Position position, WallOrientation wallOrientation) {
    return switch (commandType) {
      case MOVE -> "M " + position.toString();
      case WALL -> "W " + position.toString() + " " + switch (wallOrientation) {
        case VERTICAL -> "V";
        case HORIZONTAL -> "H";
      };
      case HELP -> "H";
      case QUIT -> "Q";
      case RESTART -> "R";
    };
  }

  @Override
  public String toString() {
    return """
            
            Command format:
            1) "m r,c" => move the pawn in the cell (r,c)
            2) "w r,c h" => place an horizontal wall near the cell (r,c)
            3) "w r,c v" => place a vertical wall near the cell (r,c)
            4) "h" => obtain information about the commands' format
            5) "q" => quit the game
            6) "r" => restart the game
            """;
  }
}
