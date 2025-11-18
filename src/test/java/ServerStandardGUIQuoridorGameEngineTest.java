import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.*;
import testDoubles.StubQuoridorParser;
import testDoubles.StubServerStandardGUIQuoridorGameEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static it.units.sdm.quoridor.controller.engine.gui.GUIQuoridorGameEngine.GUIAction.PLACE_HORIZONTAL_WALL;
import static it.units.sdm.quoridor.controller.engine.gui.GUIQuoridorGameEngine.GUIAction.PLACE_VERTICAL_WALL;
import static it.units.sdm.quoridor.controller.server.ServerProtocolCommands.PLAY;

public class ServerStandardGUIQuoridorGameEngineTest {
  QuoridorParser parser;
  AbstractQuoridorBuilder builder;
  StatisticsCounter statisticsCounter;

  @BeforeEach
  void setUp() throws InvalidParameterException {
    parser = new StubQuoridorParser();
    builder = new StandardQuoridorBuilder(2);
    statisticsCounter = new StatisticsCounter();
  }

  private StubServerStandardGUIQuoridorGameEngine createEngineWithInput(String input, String socketInput) {
    BufferedReader reader = new BufferedReader(new StringReader(input));
    BufferedReader socketReader = new BufferedReader(new StringReader(socketInput));
    BufferedWriter socketWriter = new BufferedWriter(new StringWriter(10));

    return new StubServerStandardGUIQuoridorGameEngine(reader, builder, statisticsCounter, parser, socketWriter, socketReader);
  }

  @Test
  void createdGameIsNotNull() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("", "0");
    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();
    Assertions.assertNotNull(engine.getGame());
  }

  @Test
  void moveCommand_FromServerIsExecuted() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("", "1");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isPawnMoved());
  }

  @Test
  void moveCommand_FromClientIsExecuted() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("1", PLAY.getCommandString());

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();
    engine.setMoveAction();
    Position target = new Position(1, 4);

    engine.handleTileClick(target);

    Assertions.assertTrue(engine.isPawnMoved());
  }

  @Test
  void placeWallCommand_FromServerIsExecuted() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("", "2");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isWallPlaced());
  }

  @Test
  void placeWallCommand_FromClientIsExecuted() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("1", PLAY.getCommandString());

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();
    engine.setPlaceWallAction();
    engine.setCurrentAction(PLACE_HORIZONTAL_WALL);

    Position target = new Position(4, 4);

    engine.handleTileClick(target);

    Assertions.assertTrue(engine.isWallPlaced());
  }

  @Test
  void quitCommandFromServerIsExecuted() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("", "Q");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertTrue(engine.isGameQuit());
  }

  @Test
  void restartCommandFromServerIsExecuted() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("", "R");

    engine.setGameHasToActuallyRestart(true);
    engine.runGame();

    Assertions.assertTrue(engine.hasGameActuallyRestarted());
  }

  @Test
  void turnHasChanged_afterValidMoveFromClient() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("1", "PLAY");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();
    engine.setMoveAction();
    Position target = new Position(1, 4);

    engine.handleTileClick(target);

    Assertions.assertEquals(engine.getGame().getPawns().get(1), engine.getGame().getPlayingPawn());
  }

  @Test
  void turnHasChanged_afterValidMoveFromServer() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("", "1");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertEquals(engine.getGame().getPawns().get(1), engine.getGame().getPlayingPawn());
  }

  @Test
  void turnHasNotChanged_afterInValidMoveFromServer() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("", "7");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertEquals(engine.getGame().getPawns().getFirst(), engine.getGame().getPlayingPawn());
  }

  @Test
  void turnHasChanged_afterValidHorizontalWallFromClient() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("2", "PLAY");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();
    engine.setPlaceWallAction();
    engine.setCurrentAction(PLACE_HORIZONTAL_WALL);
    Position target = new Position(4, 4);

    engine.handleTileClick(target);

    Assertions.assertEquals(engine.getGame().getPawns().get(1), engine.getGame().getPlayingPawn());
  }

  @Test
  void turnHasChanged_afterValidVerticalWallFromClient() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("9", "PLAY");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();
    engine.setPlaceWallAction();
    engine.setCurrentAction(PLACE_VERTICAL_WALL);
    Position target = new Position(4, 4);

    engine.handleTileClick(target);

    Assertions.assertEquals(engine.getGame().getPawns().get(1), engine.getGame().getPlayingPawn());
  }

  @Test
  void turnHasChanged_afterValidHorizontalWallFromServer() throws BuilderException {
    StubServerStandardGUIQuoridorGameEngine engine = createEngineWithInput("", "2");

    engine.setLoopStoppedAfterOneRound(true);
    engine.runGame();

    Assertions.assertEquals(engine.getGame().getPawns().get(1), engine.getGame().getPlayingPawn());
  }

}

