import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.cli.parser.QuoridorParser.CommandType;
import it.units.sdm.quoridor.cli.parser.StandardQuoridorParser;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

public class StandardQuoridorParserTest {
  @ParameterizedTest
  @ValueSource(strings = {"q", "Q"})
  public void parseTest_quitCommand_commandTypeIsCorrectlyRecognized(String command) throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse(command);
    Assertions.assertEquals(CommandType.QUIT, parser.getCommandType().orElseThrow());
  }

  @ParameterizedTest
  @ValueSource(strings = {"m 2,3", "M 3,4", "m -2,7", "m   6,6"})
  public void parseTest_moveCommand_commandTypeIsCorrectlyRecognized(String command) throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse(command);
    Assertions.assertEquals(CommandType.MOVE, parser.getCommandType().orElseThrow());
  }

  @ParameterizedTest
  @ValueSource(strings = {"w 2,3 h", "W 3,4 v", "w 6,99 H", "W   7,5  V"})
  public void parseTest_wallCommand_commandTypeIsCorrectlyRecognized(String command) throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse(command);
    Assertions.assertEquals(CommandType.WALL, parser.getCommandType().orElseThrow());
  }

  @ParameterizedTest
  @CsvSource({"'m 2,3', 2, 3", "'M 3,4', 3, 4"})
  public void parseTest_moveCommand_positionIsCorrectlyRecognized(String command, int row, int column) throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse(command);
    Assertions.assertEquals(new Position(row, column), parser.getActionPosition().orElseThrow());
  }

  @ParameterizedTest
  @CsvSource({"'w 2,3 v', 2, 3", "'W 3,4 h', 3, 4"})
  public void parseTest_wallCommand_positionIsCorrectlyRecognized(String command, int row, int column) throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse(command);
    Assertions.assertEquals(new Position(row, column), parser.getActionPosition().orElseThrow());
  }

  @ParameterizedTest
  @CsvSource({"'w 2,3 v', VERTICAL", "'W 3,4 h', HORIZONTAL"})
  public void parseTest_wallCommand_wallOrientationIsCorrectlyRecognized(String command, WallOrientation wallOrientation) throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse(command);
    Assertions.assertEquals(wallOrientation, parser.getWallOrientation().orElseThrow());
  }

  @Test
  public void parseTest_quitCommand_positionIsAbsent() throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse("q");
    Assertions.assertEquals(Optional.empty(), parser.getActionPosition());
  }

  @Test
  public void parseTest_quitCommand_wallOrientationIsAbsent() throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse("q");
    Assertions.assertEquals(Optional.empty(), parser.getWallOrientation());
  }

  @ParameterizedTest
  @ValueSource(strings = {"m 5,2", "M 6,1"})
  public void parseTest_moveCommand_wallOrientationIsAbsent(String command) throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse(command);
    Assertions.assertEquals(Optional.empty(), parser.getWallOrientation());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "  ", "aBCde", "MaySeemAMoveCommand", "wOW"})
  public void parseTest_wrongCommandIsCorrectlyRejected(String command) {
    QuoridorParser parser = new StandardQuoridorParser();
    Assertions.assertThrows(ParserException.class, () -> parser.parse(command));
  }

  @ParameterizedTest
  @ValueSource(strings = {" 1,2", "z 5,4", "p 4,1 v"})
  public void parseTest_wrongCommandWithParametersIsCorrectlyRejected(String command) {
    QuoridorParser parser = new StandardQuoridorParser();
    Assertions.assertThrows(ParserException.class, () -> parser.parse(command));
  }

  @ParameterizedTest
  @ValueSource(strings = {"m 1,p", "W g,2 h", "M k,j", "w 1.4 v", "m 8 9"})
  public void parseTest_wrongPositionIsCorrectlyRejected(String command) {
    QuoridorParser parser = new StandardQuoridorParser();
    Assertions.assertThrows(ParserException.class, () -> parser.parse(command));
  }

  @ParameterizedTest
  @ValueSource(strings = {"W 5,2 G", "w 3,7 r"})
  public void parseTest_wrongWallOrientationIsCorrectlyRejected(String command) {
    QuoridorParser parser = new StandardQuoridorParser();
    Assertions.assertThrows(ParserException.class, () -> parser.parse(command));
  }

  @ParameterizedTest
  @ValueSource(strings = {"q 2,3", "Q 9,5 h"})
  public void parseTest_quitCommand_excessiveParametersAreCorrectlyRejected(String command) {
    QuoridorParser parser = new StandardQuoridorParser();
    Assertions.assertThrows(ParserException.class, () -> parser.parse(command));
  }

  @ParameterizedTest
  @ValueSource(strings = {"m 4,3 V", "m 7,8 h"})
  public void parseTest_moveCommand_excessiveParametersAreCorrectlyRejected(String command) {
    QuoridorParser parser = new StandardQuoridorParser();
    Assertions.assertThrows(ParserException.class, () -> parser.parse(command));
  }

  @ParameterizedTest
  @ValueSource(strings = {"w 3,1 V f", "W 2,8 h 5"})
  public void parseTest_wallCommand_excessiveParametersAreCorrectlyRejected(String command) {
    QuoridorParser parser = new StandardQuoridorParser();
    Assertions.assertThrows(ParserException.class, () -> parser.parse(command));
  }

  @Test
  public void parseTest_multipleStringsParsedCorrectlyFirstParameterIsCorrect() throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse("w 2,3 v");
    parser.parse("Q");
    Assertions.assertEquals(CommandType.QUIT, parser.getCommandType().orElseThrow());
  }

  @Test
  public void parseTest_multipleStringsParsedCorrectlySecondParameterIsCorrect() throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse("w 2,3 v");
    parser.parse("Q");
    Assertions.assertEquals(parser.getActionPosition(), Optional.empty());
  }

  @Test
  public void parseTest_multipleStringsParsedCorrectlyThirdParameterIsCorrect() throws ParserException {
    QuoridorParser parser = new StandardQuoridorParser();
    parser.parse("w 2,3 v");
    parser.parse("Q");
    Assertions.assertEquals(parser.getWallOrientation(), Optional.empty());
  }
}
