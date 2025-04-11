import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import testDoubles.StubQuoridorParser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testDoubles.StubStandardCLIQuoridorGameEngine;
import java.io.*;

public class CLIGameEngineTest {

  QuoridorParser parser = new StubQuoridorParser();

  @Test
  void createdGameIsNotNull() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "0";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertNotNull(engine.getCurrentGame());
  }

  @Test
  void movementCommandIsExecuted() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "1";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isPawnMoved());
  }

  @Test
  void wallCommandIsExecuted() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "2";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isWallPlaced());
  }


  @Test
  void quitCommandIsExecuted() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "3";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameQuit());
  }

  @Test
  void turnHasChanged_afterValidCommand() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "4";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setLoopStoppedAfterTwoRounds(true);
    engine.runGame();

    AbstractGame game = engine.getCurrentGame();

    Assertions.assertEquals(game.getPlayingPawn(), game.getPawns().get(1));
  }


  @Test
  void turnHasNotChanged_afterInvalidCommand() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "5";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    AbstractGame game = engine.getCurrentGame();

    Assertions.assertEquals(game.getPlayingPawn(), game.getPawns().getFirst());
  }

  @Test
  void parserExceptionIsCaught() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "6";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isParserExceptionCaught());
  }

  @Test
  void invalidParameterExceptionIsCaught() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "7";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isInvalidParameterExceptionCaught());
  }

  @Test
  void invalidActionExceptionIsCaught() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "8";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isInvalidActionExceptionCaught());
  }

  @Test
  void gameEndedAfterWin() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "1";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setPawn0HasToWin(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameEnded());
  }

  @Test
  void pawn0CorrectlyWins() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "1";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setPawn0HasToWin(true);
    engine.runGame();

    Assertions.assertEquals(engine.getCurrentGame().getPlayingPawn(), engine.getCurrentGame().getPawns().getFirst());
  }

  @Test
  void pawn1CorrectlyWins() throws InvalidParameterException, BuilderException {
    String simulatedUserInput = "1";

    Reader reader = new StringReader(simulatedUserInput);
    AbstractQuoridorBuilder builder = new StdQuoridorBuilder(2);

    StubStandardCLIQuoridorGameEngine engine = new StubStandardCLIQuoridorGameEngine(reader, parser, builder);

    engine.setPawn1HasToWin(true);
    engine.runGame();

    Assertions.assertEquals(engine.getCurrentGame().getPlayingPawn(), engine.getCurrentGame().getPawns().get(1));
  }
}