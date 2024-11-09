import it.units.sdm.quoridor.cli.engine.CLIGameEngine;
import it.units.sdm.quoridor.cli.engine.QuoridorGameEngine;
import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.model.AbstractGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class CLIGameEngineTest {


  QuoridorParser parser;

  @ParameterizedTest
  @ValueSource(ints = {2, 4})
  void gameCreatedIsNotNull_withValidInput(int numberOfPlayers) {
    String simulatedUserInput = numberOfPlayers + "\n";
    System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

    Scanner scanner = new Scanner(System.in);
    QuoridorGameEngine cliGameEngine = new CLIGameEngine(scanner, parser);
    AbstractGame game = cliGameEngine.createGame();

    Assertions.assertNotNull(game);
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 4})
  void numberOfPlayersIsCoherent_withValidInput(int numberOfPlayers) {
    String simulatedUserInput = numberOfPlayers + "\n";
    System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

    Scanner scanner = new Scanner(System.in);
    QuoridorGameEngine cliGameEngine = new CLIGameEngine(scanner, parser);
    AbstractGame game = cliGameEngine.createGame();

    Assertions.assertEquals(numberOfPlayers, game.getPawns().size());
  }

  @ParameterizedTest
  @CsvSource({"5, 2", "1, 2", "3, 4", "5, 4"})
  void numberOfPlayersIsAskedAgain_withInvalidNumber_thenGameCreated(int invalidNumberOfPlayers, int validNumberOfPlayers) {
    String simulatedUserInput = invalidNumberOfPlayers + "\n" + validNumberOfPlayers + "\n";
    System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

    Scanner scanner = new Scanner(System.in);
    QuoridorGameEngine cliGameEngine = new CLIGameEngine(scanner, parser);
    AbstractGame game = cliGameEngine.createGame();

    Assertions.assertNotNull(game);
  }

  @ParameterizedTest
  @CsvSource({"q, 2", "notANumber, 4", "two, 2"})
  void numberOfPlayersIsAskedAgain_withInvalidInput_thenGameCreated(String input, int validNumberOfPlayers) {
    String simulatedUserInput = input + "\n" + validNumberOfPlayers + "\n";
    System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

    Scanner scanner = new Scanner(System.in);
    QuoridorGameEngine cliGameEngine = new CLIGameEngine(scanner, parser);
    AbstractGame game = cliGameEngine.createGame();

    Assertions.assertNotNull(game);
  }

}
