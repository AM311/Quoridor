import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testDoubles.StubQuoridorParser;
import testDoubles.StubServerStandardCLIQuoridorGameEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;

class ServerStandardCLIQuoridorGameEngineTest {

  QuoridorParser parser;
  AbstractQuoridorBuilder builder;

  @BeforeEach
  void setUp() throws InvalidParameterException {
    parser = new StubQuoridorParser();
    builder = new StdQuoridorBuilder(2);
  }

  @Test
  void createdGameIsNotNull() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("", "0");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertNotNull(engine.getGame());
  }


  private StubServerStandardCLIQuoridorGameEngine createEngineWithInput(String input, String socketInput) {
    BufferedReader reader = new BufferedReader(new StringReader(input));
    BufferedReader socketReader = new BufferedReader(new StringReader(socketInput));
    BufferedWriter socketWriter = new BufferedWriter(new StringWriter(10));

    return new StubServerStandardCLIQuoridorGameEngine(reader, parser, builder, socketWriter, socketReader);
  }

  @Test
  void commandFromServerIsExecuted() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("", "1");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isPawnMoved());
  }

  @Test
  void commandFromClientIsExecuted() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("1", "PLAY");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isPawnMoved());
  }

  @Test
  void commandFromClientIsNotExecutedIfIsNotYourRound() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("Q", "1");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertFalse(engine.isGameQuit());
  }

  @Test
  void quitCommandFromServerIsExecuted() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("", "Q");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameQuit());
  }

  @Test
  void quitCommandFromClientIsExecuted() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("Q", "PLAY");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameQuit());
  }

  @Test
  void restartCommandFromServerIsExecuted() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("", "R");

    engine.setLoopStoppedAfterOneRound(true);
    engine.setGameHasToActuallyRestart(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameActuallyRestarted());
  }

  @Test
  void restartCommandFromClientIsExecuted() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("R", "PLAY");

    engine.setLoopStoppedAfterOneRound(true);
    engine.setGameHasToActuallyRestart(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameActuallyRestarted());
  }

  @Test
  void turnHasChanged_afterValidCommandFromClient() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("4", "PLAY");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isRoundCompleted());
  }


  @Test
  void turnHasNotChanged_afterInvalidCommandFromClient() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("5", "PLAY");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertFalse(engine.isRoundCompleted());
  }

  @Test
  void parserExceptionFromClientIsCaught() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("6", "PLAY");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isParserExceptionCaught());
  }

  @Test
  void parserExceptionFromServerIsCaught() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("", "6");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isParserExceptionCaught());
  }

  @Test
  void invalidParameterExceptionFromClientIsCaught() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("7", "PLAY");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isInvalidParameterExceptionCaught());
  }

  @Test
  void invalidParameterExceptionFromServerIsCaught() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("", "7");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isInvalidParameterExceptionCaught());
  }

  @Test
  void invalidActionExceptionFromClientIsCaught() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("8", "PLAY");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isInvalidActionExceptionCaught());
  }

  @Test
  void invalidActionExceptionFromServerIsCaught() throws InvalidParameterException, BuilderException {
    StubServerStandardCLIQuoridorGameEngine engine = createEngineWithInput("", "8");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isInvalidActionExceptionCaught());
  }
}
