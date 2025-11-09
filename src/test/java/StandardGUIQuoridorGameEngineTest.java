import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.engine.gui.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.engine.gui.StandardGUIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.controller.parser.StandardQuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
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


  @BeforeEach
  void setUp() throws InvalidParameterException, InterruptedException {
    parser = new StandardQuoridorParser();
    builder = new StandardQuoridorBuilder(2);
    statisticsCounter = new StatisticsCounter();
    engine = new StandardGUIQuoridorGameEngine(builder, statisticsCounter, parser);

    SwingUtilities.invokeLater(() -> {
      try { engine.runGame(); } catch (BuilderException e) { throw new RuntimeException(e); }
    });
    long deadline = System.currentTimeMillis() + 3000;
    while (engine.getGame() == null && System.currentTimeMillis() < deadline) Thread.sleep(25);
  }

  @AfterEach
  void closeWindows() throws InterruptedException, InvocationTargetException {
    SwingUtilities.invokeAndWait(() -> {
      JFrame frame = engine.getGameView().getMainFrame();
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.dispose();
      for (Window w : Window.getWindows()) if (w.isDisplayable()) w.dispose();
    });

    SwingUtilities.invokeAndWait(() -> {
      for (Window w : Window.getWindows()) {
        if (w instanceof JDialog && w.isDisplayable()) {
          w.dispose();
        }
      }
    });
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


  @Test
  void createdGameIsNotNull(){
    Assertions.assertNotNull(engine.getGame());
  }

  @Test
  void turnHasChanged_afterValidMove() throws Exception {
    SwingUtilities.invokeAndWait(engine::setMoveAction);
    Position target = new Position(1, 4);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    Assertions.assertEquals(engine.getGame().getPawns().get(1), engine.getGame().getPlayingPawn());
  }

  @Test
  void gameMovesCorrectlyUpdated_afterValidMove() throws Exception {

    SwingUtilities.invokeAndWait(engine::setMoveAction);
    Position target = new Position(1, 4);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    AbstractPawn pawn0 = engine.getPawns().getFirst();
    Assertions.assertEquals(1, statisticsCounter.getGameMoves(pawn0.toString()));
  }

  @Test
  void gameMovesNotUpdated_afterInvalidMove() throws Exception {
    SwingUtilities.invokeAndWait(engine::setMoveAction);
    Position target = new Position(4, 4);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    AbstractPawn pawn0 = engine.getPawns().getFirst();
    Assertions.assertEquals(0, statisticsCounter.getGameMoves(pawn0.toString()));
  }

  @Test
  void turnHasChanged_afterValidHorizontalWallIsPlaced() throws Exception {
    SwingUtilities.invokeAndWait(() -> engine.setPlaceWallAction());
    SwingUtilities.invokeAndWait(() -> engine.setCurrentAction(PLACE_HORIZONTAL_WALL));

    Position target = new Position(5, 5);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().get(1));
  }

  @Test
  void turnHasNotChanged_afterInvalidHorizontalWallIsPlaced() throws Exception {
    SwingUtilities.invokeAndWait(() -> engine.setPlaceWallAction());
    SwingUtilities.invokeAndWait(() -> engine.setCurrentAction(PLACE_HORIZONTAL_WALL));

    Position target = new Position(8, 8);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().getFirst());
  }

  @Test
  void gameWallsCorrectlyUpdated_afterValidHorizontalWallIsPlaced() throws Exception {
    SwingUtilities.invokeAndWait(() -> engine.setPlaceWallAction());
    SwingUtilities.invokeAndWait(() -> engine.setCurrentAction(PLACE_HORIZONTAL_WALL));

    Position target = new Position(3,3);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    AbstractPawn pawn0 = engine.getPawns().getFirst();
    Assertions.assertEquals(1, statisticsCounter.getGameWalls(pawn0.toString()));
  }

  @Test
  void turnHasChanged_afterValidVerticalWallIsPlaced() throws Exception {
    SwingUtilities.invokeAndWait(() -> engine.setPlaceWallAction());
    SwingUtilities.invokeAndWait(() -> engine.setCurrentAction(PLACE_VERTICAL_WALL));

    Position target = new Position(5, 5);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().get(1));
  }

  @Test
  void turnHasNotChanged_afterNoAction() throws Exception {
    SwingUtilities.invokeAndWait(() -> engine.setCurrentAction(DO_NOTHING));

    Position target = new Position(8, 8);

    SwingUtilities.invokeAndWait(() -> engine.handleTileClick(target));
    Assertions.assertEquals(engine.getGame().getPlayingPawn(), engine.getGame().getPawns().getFirst());
  }
}
