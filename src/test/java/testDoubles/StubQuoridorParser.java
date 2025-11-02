package testDoubles;

import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.Optional;

import static it.units.sdm.quoridor.controller.parser.QuoridorParser.CommandType.*;
import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;

public class StubQuoridorParser extends QuoridorParser {
  @Override
  protected CommandType parseSpecific() throws ParserException {
    return switch (commandTokens[0]) {
      case "0"-> {
        this.position = new Position(1, 5);
        yield MOVE;
      }
      case "1" -> {
        this.position = new Position(1, 4);
        yield MOVE;
      }
      case "2" -> {
        this.position = new Position(4, 4);
        this.wallOrientation = HORIZONTAL;
        yield WALL;
      }
      case "4" -> {
        this.position = new Position(0, 3);
        yield MOVE;
      }
      case "5" ->{
        this.position = new Position(1, 1);
        yield MOVE;
      }
      case "6" -> throw new ParserException();
      case "7" ->{
        this.position = new Position(-1, -1);
        yield MOVE;
      }
      case "8" ->{
        this.position = new Position(4, 4);
        yield MOVE;
      }
      case "H" -> HELP;
      default -> null;
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
  public String toString() {
    return "";
  }

  //todo FARE...
  @Override
  public String generateString(CommandType commandType, Position position, WallOrientation wallOrientation) {
    return "";
  }
}
