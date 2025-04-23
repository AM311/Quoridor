package it.units.sdm.quoridor.cli.parser;

import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.Optional;

public interface QuoridorParser {
  void parse(String command) throws ParserException;

  Optional<CommandType> getCommandType();

  Optional<Position> getActionPosition();

  Optional<WallOrientation> getWallOrientation();

  String toString();

  enum CommandType {
    QUIT, MOVE, WALL, HELP, RESTART
  }
}
