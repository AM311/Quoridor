import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.engine.gui.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.engine.gui.StandardGUIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.controller.parser.StandardQuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.*;
import testDoubles.StubStandardGUIQuoridorGameEngine;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static it.units.sdm.quoridor.controller.engine.gui.GUIQuoridorGameEngine.GUIAction.*;

public class StandardGUIQuoridorGameEngineTest {
  QuoridorParser parser;
  AbstractQuoridorBuilder builder;
  StatisticsCounter statisticsCounter;
  GUIQuoridorGameEngine engine;

  private void initStandardEngine() throws Exception {
    parser = new StandardQuoridorParser();
    builder = new StandardQuoridorBuilder(2);
    statisticsCounter = new StatisticsCounter();
    engine = new StandardGUIQuoridorGameEngine(builder, statisticsCounter, parser);

    SwingUtilities.invokeLater(() -> {
      try {
        engine.runGame();
      } catch (BuilderException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @AfterEach
  void closeWindows() throws InterruptedException, InvocationTargetException {
    SwingUtilities.invokeAndWait(() -> {
      for (Window w : Window.getWindows()) {
        if (w instanceof JDialog) {
          w.dispose();
        }
      }
    });
  }

  @Test
  void createdGameIsNotNull() throws Exception {
    initStandardEngine();
    long deadline = System.currentTimeMillis() + 500;
    while (engine.getGame() == null && System.currentTimeMillis() < deadline) Thread.sleep(25);
    Assertions.assertNotNull(engine.getGame());
  }

  @Test
  void turnHasChanged_afterValidMove() throws Exception {
    initStandardEngine();
    SwingUtilities.invokeAndWait(engine::setMoveAction);
    Position target = new Position(1, 4);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    Assertions.assertEquals(engine.getGame().getPawns().get(1), engine.getGame().getPlayingPawn());
  }

  @Test
  void gameMovesCorrectlyUpdated_afterValidMove() throws Exception {
    initStandardEngine();
    SwingUtilities.invokeAndWait(engine::setMoveAction);
    Position target = new Position(1, 4);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    AbstractPawn pawn0 = engine.getPawns().getFirst();
    Assertions.assertEquals(1, statisticsCounter.getGameMoves(pawn0.toString()));
  }

  @Test
  void gameMovesNotUpdated_afterInvalidMove() throws Exception {
    initStandardEngine();
    SwingUtilities.invokeAndWait(engine::setMoveAction);
    Position target = new Position(4, 4);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    AbstractPawn pawn0 = engine.getPawns().getFirst();
    Assertions.assertEquals(0, statisticsCounter.getGameMoves(pawn0.toString()));
  }

  @Test
  void turnHasChanged_afterValidHorizontalWallIsPlaced() throws Exception {
    initStandardEngine();
    SwingUtilities.invokeAndWait(() -> engine.setPlaceWallAction());
    SwingUtilities.invokeAndWait(() -> engine.setCurrentAction(PLACE_HORIZONTAL_WALL));

    Position target = new Position(5, 5);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().get(1));
  }

  @Test
  void turnHasNotChanged_afterInvalidHorizontalWallIsPlaced() throws Exception {
    initStandardEngine();
    SwingUtilities.invokeAndWait(() -> engine.setPlaceWallAction());
    SwingUtilities.invokeAndWait(() -> engine.setCurrentAction(PLACE_HORIZONTAL_WALL));

    Position target = new Position(8, 8);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().getFirst());
  }

  @Test
  void gameWallsCorrectlyUpdated_afterValidHorizontalWallIsPlaced() throws Exception {
    initStandardEngine();
    SwingUtilities.invokeAndWait(() -> engine.setPlaceWallAction());
    SwingUtilities.invokeAndWait(() -> engine.setCurrentAction(PLACE_HORIZONTAL_WALL));

    Position target = new Position(3, 3);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    AbstractPawn pawn0 = engine.getPawns().getFirst();
    Assertions.assertEquals(1, statisticsCounter.getGameWalls(pawn0.toString()));
  }

  @Test
  void turnHasChanged_afterValidVerticalWallIsPlaced() throws Exception {
    initStandardEngine();
    SwingUtilities.invokeAndWait(() -> engine.setPlaceWallAction());
    SwingUtilities.invokeAndWait(() -> engine.setCurrentAction(PLACE_VERTICAL_WALL));

    Position target = new Position(5, 5);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().get(1));
  }

  @Test
  void turnHasNotChanged_afterNoAction() throws Exception {
    initStandardEngine();
    SwingUtilities.invokeAndWait(() -> engine.setCurrentAction(DO_NOTHING));

    Position target = new Position(8, 8);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().getFirst());
  }

  @Test
  void totalGameStatsUpdated_afterGameHasFinished() throws Exception {
    initStandardEngine();
    makeGameEnd(engine);

    SwingUtilities.invokeAndWait(StandardGUIQuoridorGameEngineTest::closeVisibleDialog);

    AbstractPawn pawn0 = engine.getPawns().getFirst();
    AbstractPawn pawn1 = engine.getPawns().get(1);
    Assertions.assertEquals(7, statisticsCounter.getTotalMoves(pawn0.toString()));
    Assertions.assertEquals(7, statisticsCounter.getTotalMoves(pawn1.toString()));
  }

  @Test
  void gameIsQuit_afterHasFinished() throws Exception {
    parser = new StandardQuoridorParser();
    builder = new StandardQuoridorBuilder(2);
    statisticsCounter = new StatisticsCounter();

    StubStandardGUIQuoridorGameEngine stubEngine =
            new StubStandardGUIQuoridorGameEngine(builder, statisticsCounter, parser);
    engine = stubEngine;

    stubEngine.setShowQuitRestartDialog(true);

    SwingUtilities.invokeLater(() -> {
      try {
        stubEngine.runGame();
      } catch (BuilderException e) {
        throw new RuntimeException(e);
      }
    });

    makeGameEnd(stubEngine);

    JDialog quitRestartDialog = waitForDialogWithButton();

    SwingUtilities.invokeAndWait(() -> {
      JButton exitButton = findButtonByText(quitRestartDialog.getContentPane(), "EXIT");
      if (exitButton != null) {
        exitButton.doClick();
      }
    });

    Assertions.assertTrue(stubEngine.isGameEnded());
    Assertions.assertTrue(stubEngine.isGameQuit());
    Assertions.assertFalse(stubEngine.isGameRestarted());
  }

  @Test
  void gameIsRestarted_afterHasFinished() throws Exception {
    parser = new StandardQuoridorParser();
    builder = new StandardQuoridorBuilder(2);
    statisticsCounter = new StatisticsCounter();

    StubStandardGUIQuoridorGameEngine stubEngine = new StubStandardGUIQuoridorGameEngine(builder, statisticsCounter, parser);
    engine = stubEngine;

    stubEngine.setShowQuitRestartDialog(true);

    SwingUtilities.invokeLater(() -> {
      try {
        stubEngine.runGame();
      } catch (BuilderException e) {
        throw new RuntimeException(e);
      }
    });
    makeGameEnd(stubEngine);

    JDialog quitRestartDialog = waitForDialogWithButton();

    SwingUtilities.invokeAndWait(() -> {
      JButton exitButton = findButtonByText(quitRestartDialog.getContentPane(), "RESTART");
      if (exitButton != null) {
        exitButton.doClick();
      }
    });

    Assertions.assertTrue(stubEngine.isGameEnded());
    Assertions.assertFalse(stubEngine.isGameQuit());
    Assertions.assertTrue(stubEngine.isGameRestarted());
  }

  @Test
  void gameStatsAreReset_afterHasRestarted() throws Exception {parser = new StandardQuoridorParser();
    builder = new StandardQuoridorBuilder(2);
    statisticsCounter = new StatisticsCounter();

    StubStandardGUIQuoridorGameEngine stubEngine = new StubStandardGUIQuoridorGameEngine(builder, statisticsCounter, parser);
    engine = stubEngine;

    stubEngine.setShowQuitRestartDialog(true);

    SwingUtilities.invokeLater(() -> {
      try {
        stubEngine.runGame();
      } catch (BuilderException e) {
        throw new RuntimeException(e);
      }
    });
    makeGameEnd(stubEngine);

    JDialog quitRestartDialog = waitForDialogWithButton();

    SwingUtilities.invokeAndWait(() -> {
      JButton exitButton = findButtonByText(quitRestartDialog.getContentPane(), "RESTART");
      if (exitButton != null) {
        exitButton.doClick();
      }
    });

    AbstractPawn pawn0 = engine.getPawns().getFirst();
    AbstractPawn pawn1 = engine.getPawns().get(1);

    Assertions.assertEquals(0, statisticsCounter.getGameMoves(pawn0.toString()));
    Assertions.assertEquals(0, statisticsCounter.getGameMoves(pawn1.toString()));
  }

  private static JButton findButtonByText(Container root, String text) {
    for (Component c : root.getComponents()) {
      if (c instanceof JButton b && text.equals(b.getText())) {
        return b;
      }
      if (c instanceof Container nested) {
        JButton res = findButtonByText(nested, text);
        if (res != null) return res;
      }
    }
    return null;
  }

  private static JDialog waitForDialogWithButton() throws Exception {
    long deadline = System.currentTimeMillis() + (long) 500;
    JDialog dialog = null;

    while (System.currentTimeMillis() < deadline && dialog == null) {
      final JDialog[] holder = new JDialog[1];
      SwingUtilities.invokeAndWait(() -> holder[0] = findDialogWithExitButton());
      dialog = holder[0];
    }
    return dialog;
  }

  private static JDialog findDialogWithExitButton() {
    for (Window w : Window.getWindows()) {
      if (w instanceof JDialog d && d.isShowing()) {
        JButton btn = findButtonByText(d.getContentPane(), "EXIT");
        if (btn != null) {
          return d;
        }
      }
    }
    return null;
  }

  private static void makeGameEnd(GUIQuoridorGameEngine engine) {
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
      SwingUtilities.invokeLater(engine::setMoveAction);
      SwingUtilities.invokeLater(() -> engine.handleTileClick(move));
    }
  }

  private static void closeVisibleDialog() {
    for (Window w : Window.getWindows()) {
      if (w instanceof JDialog d && d.isShowing()) {
        d.dispose();
      }
    }
  }
}
