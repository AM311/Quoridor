import it.units.sdm.quoridor.cli.engine.CLIGameEngine;
import it.units.sdm.quoridor.cli.engine.CLIInputProvider;
import it.units.sdm.quoridor.cli.engine.InputProvider;
import it.units.sdm.quoridor.cli.engine.QuoridorGameEngine;
import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.model.Tile;
import it.units.sdm.quoridor.movemanagement.actionmanagers.ActionManager;
import it.units.sdm.quoridor.movemanagement.actions.PawnMover;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

public class CLIGameEngineTest {


  QuoridorParser parser;

  @ParameterizedTest
  @ValueSource(ints = {2, 4})
  void gameCreatedIsNotNull_withValidInput(int numberOfPlayers) {
    String simulatedUserInput = numberOfPlayers + "\n";
    System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

    InputProvider inputProvider = new CLIInputProvider();
    QuoridorGameEngine cliGameEngine = new CLIGameEngine(inputProvider, parser);
    AbstractGame game = cliGameEngine.createGame();

    Assertions.assertNotNull(game);
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 4})
  void numberOfPlayersIsCoherent_withValidInput(int numberOfPlayers) {
    String simulatedUserInput = numberOfPlayers + "\n";
    System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

    InputProvider inputProvider = new CLIInputProvider();
    QuoridorGameEngine cliGameEngine = new CLIGameEngine(inputProvider, parser);
    AbstractGame game = cliGameEngine.createGame();

    Assertions.assertEquals(numberOfPlayers, game.getPawns().size());
  }

  @ParameterizedTest
  @CsvSource({"5, 2", "1, 2", "3, 4", "5, 4"})
  void numberOfPlayersIsAskedAgain_withInvalidNumber_thenGameCreated(int invalidNumberOfPlayers, int validNumberOfPlayers) {
    String simulatedUserInput = invalidNumberOfPlayers + "\n" + validNumberOfPlayers + "\n";
    System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

    InputProvider inputProvider = new CLIInputProvider();
    QuoridorGameEngine cliGameEngine = new CLIGameEngine(inputProvider, parser);
    AbstractGame game = cliGameEngine.createGame();

    Assertions.assertNotNull(game);
  }

  @ParameterizedTest
  @CsvSource({"q, 2", "notANumber, 4", "two, 2"})
  void numberOfPlayersIsAskedAgain_withInvalidInput_thenGameCreated(String input, int validNumberOfPlayers) {
    String simulatedUserInput = input + "\n" + validNumberOfPlayers + "\n";
    System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

    InputProvider inputProvider = new CLIInputProvider();
    QuoridorGameEngine cliGameEngine = new CLIGameEngine(inputProvider, parser);
    AbstractGame game = cliGameEngine.createGame();

    Assertions.assertNotNull(game);
  }

  @Test
  void inputCommandIsPerformed() {
    String simulatedUserInput = "2\nmoveFirstIn1,4\n";
    System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));


    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor));

    InputProvider inputProvider = new CLIInputProvider();
    CLIGameEngine cliGameEngine = new CLIGameEngine(inputProvider, parser);

    cliGameEngine.enableTestingMode();
    AbstractGame game = cliGameEngine.createGame();

    cliGameEngine.startGame(game);

    String output = outputStreamCaptor.toString();
    Assertions.assertTrue(output.contains("Command moveFirstIn1,4 executed"));
  }


  @Test
  void roundIsProperlyChanged() {
    String simulatedUserInput = "2\nmoveFirstIn1,4\n";
    System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

    InputProvider inputProvider = new CLIInputProvider();
    CLIGameEngine cliGameEngine = new CLIGameEngine(inputProvider, parser);

    cliGameEngine.enableTestingMode();
    AbstractGame game = cliGameEngine.createGame();

    cliGameEngine.startGame(game);

    Assertions.assertEquals(game.getPlayingPawn(), game.getPawns().get(1));

  }

  @Test
  void winningPawnProperlyRecognized() throws InvalidParameterException {
    String simulatedUserInput = "2\nmakeFirstPawnWin\n";
    System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));


    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor));

    InputProvider inputProvider = new CLIInputProvider();
    CLIGameEngine cliGameEngine = new CLIGameEngine(inputProvider, parser);

    cliGameEngine.enableTestingMode();
    AbstractGame game = cliGameEngine.createGame();

    PawnMover pawnMover = new PawnMover();
    AbstractTile target = game.getGameBoard().getTile(new Position(1,5));
    pawnMover.execute(game, target);

    while(game.getPawns().getFirst().getCurrentTile().getRow() != 8){
      pawnMover.execute(game, game.getGameBoard().getTile(new Position(game.getPawns().getFirst().getCurrentTile().getRow() + 1,5)));
    }

    cliGameEngine.startGame(game);

    String output = outputStreamCaptor.toString();
    Assertions.assertTrue(output.contains(game.getPawns().getFirst() + " won!"));
  }




}
