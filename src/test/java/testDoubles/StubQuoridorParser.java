package testDoubles;

import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.Optional;

import static it.units.sdm.quoridor.cli.parser.QuoridorParser.CommandType.*;
import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;

public class StubQuoridorParser implements QuoridorParser {
  public CommandType commandType;
  Position position;
  WallOrientation wallOrientation;

  @Override
  public void parse(String command) throws ParserException {
    switch (command) {
      case "0"-> {
        this.commandType = MOVE;
        this.position = new Position(1, 5);
      }
      case "1" -> {
        this.commandType = MOVE;
        this.position = new Position(1, 4);
      }
      case "2" -> {
        this.commandType = WALL;
        this.position = new Position(4, 4);
        this.wallOrientation = HORIZONTAL;
      }
      case "3" -> {
        this.commandType = QUIT;
      }
      case "4" -> {
        this.commandType = MOVE;
        this.position = new Position(0, 3);
      }
      case "5" ->{
        this.commandType = MOVE;
        this.position = new Position(1, 1);
      }
      case "6" ->{
        this.commandType = MOVE;
        throw new ParserException();
      }
      case "7" ->{
        this.commandType = MOVE;
        this.position = new Position(-1, -1);
      }
      case "8" ->{
        this.commandType = MOVE;
        this.position = new Position(4, 4);
      }
      case "9" -> {
        this.commandType = HELP;
      }
      case "10" -> {
        this.commandType = RESTART;
      }
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
}
