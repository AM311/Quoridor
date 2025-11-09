import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testDoubles.StubQuoridorParser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testDoubles.StubStandardCLIQuoridorGameEngine;

import java.io.*;

public class StandardCLIQuoridorGameEngineTest {

  QuoridorParser parser;
  AbstractQuoridorBuilder builder;

  @BeforeEach
  void setUp() throws InvalidParameterException {
    parser = new StubQuoridorParser();
    builder = new StandardQuoridorBuilder(2);
  }

  private StubStandardCLIQuoridorGameEngine createEngineWithInput(String input) {
    BufferedReader reader = new BufferedReader(new StringReader(input));
    return new StubStandardCLIQuoridorGameEngine(reader, parser, builder);
  }

  @Test
  void createdGameIsNotNull() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("0");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

   Assertions.assertNotNull(engine.getGame());
  }

  @Test
  void movementCommandIsExecuted() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("1");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isPawnMoved());
  }

  @Test
  void wallCommandIsExecuted() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("2");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isWallPlaced());
  }

  @Test
  void turnHasChanged_afterValidCommand() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("4");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isRoundCompleted());
  }


  @Test
  void turnHasNotChanged_afterInvalidCommand() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("5");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertFalse(engine.isRoundCompleted());
  }

  @Test
  void parserExceptionIsCaught() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("6");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isParserExceptionCaught());
  }

  @Test
  void invalidParameterExceptionIsCaught() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("7");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isInvalidParameterExceptionCaught());
  }

  @Test
  void invalidActionExceptionIsCaught() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("8");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isInvalidActionExceptionCaught());
  }

  @Test
  void pawn0CorrectlyWins() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("1");

    engine.setPawn0HasToWin(true);
    engine.runGame();

    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().getFirst());
  }

  @Test
  void pawn1CorrectlyWins() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("1" + System.lineSeparator() + "1");

    engine.setPawn1HasToWin(true);
    engine.runGame();

    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().get(1));
  }

  @Test
  void gameIsQuit_onQuitCommand() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("Q");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameQuit());
  }

  @Test
  void gameIsActuallyRestarted_onRestartCommand() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("R");

    engine.setLoopStoppedAfterOneRound(true);
    engine.setGameHasToActuallyRestart(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameActuallyRestarted());
  }

  @Test
  void gameIsCreatedAfterRestart() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("R");

    engine.setLoopStoppedAfterOneRound(true);
    engine.setGameHasToActuallyRestart(true);
    engine.runGame();

    Assertions.assertNotNull(engine.getGame());
  }

  @Test
  void gameEndedAfterWin() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("1\nQ");

    engine.setPawn0HasToWin(true);
    engine.setEndGameHasToBeHandled(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameEnded());
  }

  @Test
  void gameIsQuit_onQuitCommand_afterEndGame() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("1\nQ");

    engine.setLoopStoppedAfterOneRound(true);
    engine.setEndGameHasToBeHandled(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameQuit());
  }

  @Test
  void gameIsActuallyRestarted_onRestartCommand_afterEndGame() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("1\nR");

    engine.setLoopStoppedAfterOneRound(true);
    engine.setEndGameHasToBeHandled(true);
    engine.setGameHasToActuallyRestart(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameActuallyRestarted());
  }

  @Test
  void helpCommandIsExecuted() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("H");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isHelpAsked());
  }

  @Test
  void commandExecutedIsFalse_afterHelpCommand() throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput("H");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertFalse(engine.isCommandExecuted());
  }

  @ParameterizedTest
  @ValueSource(strings = {"1", "2"})
  void commandExecutedIsTrue_afterActualCommand(String input) throws InvalidParameterException, BuilderException {
    StubStandardCLIQuoridorGameEngine engine = createEngineWithInput(input);

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isCommandExecuted());
  }
}