import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.controller.parser.StandardQuoridorParser;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.*;
import testDoubles.StubStandardGUIQuoridorGameEngine;
import java.util.List;
import static it.units.sdm.quoridor.controller.engine.gui.GUIQuoridorGameEngine.GUIAction.*;

public class StandardGUIQuoridorGameEngineTest {
  QuoridorParser parser;
  AbstractQuoridorBuilder builder;
  StatisticsCounter statisticsCounter;
  StubStandardGUIQuoridorGameEngine engine;

  @BeforeEach
  public void setUp() throws Exception {
    parser = new StandardQuoridorParser();
    builder = new StandardQuoridorBuilder(2);
    statisticsCounter = new StatisticsCounter();
    engine = new StubStandardGUIQuoridorGameEngine(builder, statisticsCounter, parser);
    engine.runGame();
  }

  @Test
  void createdGameIsNotNull(){
    Assertions.assertNotNull(engine.getGame());
  }

  @Test
  void turnHasChanged_afterValidMove(){
    engine.setMoveAction();
    Position target = new Position(1, 4);

    engine.handleTileClick(target);
    Assertions.assertEquals(engine.getGame().getPawns().get(1), engine.getGame().getPlayingPawn());
  }

  @Test
  void gameMovesCorrectlyUpdated_afterValidMove(){
    engine.setMoveAction();
    Position target = new Position(1, 4);

    engine.handleTileClick(target);
    AbstractPawn pawn0 = engine.getPawns().getFirst();
    Assertions.assertEquals(1, statisticsCounter.getGameMoves(pawn0.toString()));
  }

  @Test
  void gameMovesNotUpdated_afterInvalidMove(){
    engine.setMoveAction();
    Position target = new Position(4, 4);

    engine.handleTileClick(target);
    AbstractPawn pawn0 = engine.getPawns().getFirst();
    Assertions.assertEquals(0, statisticsCounter.getGameMoves(pawn0.toString()));
  }

  @Test
  void turnHasChanged_afterValidHorizontalWallIsPlaced(){
    engine.setPlaceWallAction();
    engine.setCurrentAction(PLACE_HORIZONTAL_WALL);

    Position target = new Position(5, 5);

    engine.handleTileClick(target);
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().get(1));
  }

  @Test
  void turnHasNotChanged_afterInvalidHorizontalWallIsPlaced(){
    engine.setPlaceWallAction();
    engine.setCurrentAction(PLACE_HORIZONTAL_WALL);

    Position target = new Position(8, 8);

    engine.handleTileClick(target);
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().getFirst());
  }

  @Test
  void gameWallsCorrectlyUpdated_afterValidHorizontalWallIsPlaced(){
    engine.setPlaceWallAction();
    engine.setCurrentAction(PLACE_HORIZONTAL_WALL);

    Position target = new Position(3, 3);

    engine.handleTileClick(target);
    AbstractPawn pawn0 = engine.getPawns().getFirst();
    Assertions.assertEquals(1, statisticsCounter.getGameWalls(pawn0.toString()));
  }

  @Test
  void turnHasChanged_afterValidVerticalWallIsPlaced(){
    engine.setPlaceWallAction();
    engine.setCurrentAction(PLACE_VERTICAL_WALL);

    Position target = new Position(5, 5);

    engine.handleTileClick(target);
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().get(1));
  }

  @Test
  void turnHasNotChanged_afterNoAction(){
    engine.setCurrentAction(DO_NOTHING);

    Position target = new Position(8, 8);

    engine.handleTileClick(target);
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().getFirst());
  }

  @Test
  void invalidParameterExceptionCaught(){
    engine.setMoveAction();
    Position target = new Position(12, 12);
    engine.handleTileClick(target);
    Assertions.assertTrue(engine.isInvalidParameterExceptionCaught());
  }

  @Test
  void invalidActionExceptionCaught(){
    engine.setMoveAction();
    Position target = new Position(3, 7);
    engine.handleTileClick(target);
    Assertions.assertTrue(engine.isInvalidActionExceptionCaught());
  }

  @Test
  void totalGameStatsUpdated_afterGameHasFinished(){
    makeGameEnd(engine);

    AbstractPawn pawn0 = engine.getPawns().getFirst();
    AbstractPawn pawn1 = engine.getPawns().get(1);
    Assertions.assertEquals(7, statisticsCounter.getTotalMoves(pawn0.toString()));
    Assertions.assertEquals(7, statisticsCounter.getTotalMoves(pawn1.toString()));
  }

  @Test
  void gameIsQuit_afterHasFinished(){
    engine.setGameHasToQuit(true);
    makeGameEnd(engine);

    Assertions.assertTrue(engine.isGameEnded());
    Assertions.assertTrue(engine.isGameQuit());
    Assertions.assertFalse(engine.isGameRestarted());
  }

  @Test
  void gameIsRestarted_afterHasFinished(){
    engine.setGameHasToRestart(true);
    makeGameEnd(engine);

    Assertions.assertTrue(engine.isGameEnded());
    Assertions.assertFalse(engine.isGameQuit());
    Assertions.assertTrue(engine.isGameRestarted());
  }

  @Test
  void gameStatsAreReset_afterHasRestarted(){
    engine.setGameHasToRestart(true);
    makeGameEnd(engine);

    AbstractPawn pawn0 = engine.getPawns().getFirst();
    AbstractPawn pawn1 = engine.getPawns().get(1);

    Assertions.assertEquals(0, statisticsCounter.getGameMoves(pawn0.toString()));
    Assertions.assertEquals(0, statisticsCounter.getGameMoves(pawn1.toString()));
  }

  private static void makeGameEnd(StubStandardGUIQuoridorGameEngine engine) {
    List<Position> moves = List.of(
            new Position(1, 4),
            new Position(7, 4),
            new Position(2, 4),
            new Position(6, 4),
            new Position(3, 4),
            new Position(5, 4),
            new Position(4, 4),
            new Position(3, 4),
            new Position(5, 4),
            new Position(2, 4),
            new Position(6, 4),
            new Position(1, 4),
            new Position(7, 4),
            new Position(0, 4)
    );

    for (Position move : moves) {
      engine.setMoveAction();
      engine.handleTileClick(move);
    }
  }
}
