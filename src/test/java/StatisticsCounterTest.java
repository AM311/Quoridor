import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsCounterTest {

  StatisticsCounter statisticsCounter;
  AbstractGame game;

  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    return builderDirector.makeGame();
  }

  @BeforeEach
  void setUp() throws InvalidParameterException, BuilderException {
    statisticsCounter = new StatisticsCounter();
    game = buildGame();
    statisticsCounter.setGame(game);
  }

  @Test
  void updateGameMoves_isCoherent() {
    String pawn1 = String.valueOf(game.getPawns().get(0));
    String pawn2 = String.valueOf(game.getPawns().get(1));

    assertEquals(0, statisticsCounter.getGameMoves(pawn1));
    assertEquals(0, statisticsCounter.getGameMoves(pawn2));

    statisticsCounter.updateGameMoves(pawn1);
    statisticsCounter.updateGameMoves(pawn2);
    statisticsCounter.updateGameMoves(pawn1);

    assertEquals(2, statisticsCounter.getGameMoves(pawn1));
    assertEquals(1, statisticsCounter.getGameMoves(pawn2));
  }

  @Test
  void updateGameWalls_isCoherent() {
    String pawn1 = String.valueOf(game.getPawns().get(0));
    String pawn2 = String.valueOf(game.getPawns().get(1));

    assertEquals(0, statisticsCounter.getGameWalls(pawn1));
    assertEquals(0, statisticsCounter.getGameWalls(pawn2));

    statisticsCounter.updateGameWalls(pawn1);
    statisticsCounter.updateGameWalls(pawn2);
    statisticsCounter.updateGameWalls(pawn2);
    statisticsCounter.updateGameWalls(pawn1);

    assertEquals(2, statisticsCounter.getGameWalls(pawn1));
    assertEquals(2, statisticsCounter.getGameWalls(pawn2));
  }

  @Test
  void resetGamesStats_isCoherent() {
    String pawn1 = String.valueOf(game.getPawns().get(0));
    String pawn2 = String.valueOf(game.getPawns().get(1));

    statisticsCounter.updateGameMoves(pawn1);
    statisticsCounter.updateGameWalls(pawn2);

    assertEquals(1, statisticsCounter.getGameMoves(pawn1));
    assertEquals(1, statisticsCounter.getGameWalls(pawn2));

    statisticsCounter.resetGameStats();

    assertEquals(0, statisticsCounter.getGameMoves(pawn1));
    assertEquals(0, statisticsCounter.getGameWalls(pawn2));
  }

  @Test
  void updateAllTotalStats_isCoherent() {
    String pawn1 = String.valueOf(game.getPawns().get(0));
    String pawn2 = String.valueOf(game.getPawns().get(1));

    statisticsCounter.updateGameMoves(pawn1);
    statisticsCounter.updateGameMoves(pawn1);
    statisticsCounter.updateGameWalls(pawn1);

    statisticsCounter.updateGameMoves(pawn2);
    statisticsCounter.updateGameWalls(pawn2);
    statisticsCounter.updateGameWalls(pawn2);

    statisticsCounter.updateAllTotalStats();

    assertEquals(2, statisticsCounter.getTotalMoves(pawn1));
    assertEquals(1, statisticsCounter.getTotalMoves(pawn2));
    assertEquals(1, statisticsCounter.getTotalWalls(pawn1));
    assertEquals(2, statisticsCounter.getTotalWalls(pawn2));
  }


  @Test
  void totalStats_afterMultipleGames_areCoherent() throws InvalidParameterException, BuilderException {
    String pawn1 = String.valueOf(game.getPawns().get(0));
    String pawn2 = String.valueOf(game.getPawns().get(1));

    statisticsCounter.updateGameMoves(pawn1);
    statisticsCounter.updateGameMoves(pawn1);
    statisticsCounter.updateGameMoves(pawn2);
    statisticsCounter.updateGameMoves(pawn2);
    statisticsCounter.updateGameWalls(pawn1);

    statisticsCounter.updateAllTotalStats();
    statisticsCounter.resetGameStats();

    game = buildGame();
    statisticsCounter.setGame(game);

    statisticsCounter.updateGameMoves(pawn1);
    statisticsCounter.updateGameMoves(pawn2);
    statisticsCounter.updateGameMoves(pawn2);
    statisticsCounter.updateGameWalls(pawn1);
    statisticsCounter.updateGameWalls(pawn2);

    statisticsCounter.updateAllTotalStats();
    statisticsCounter.resetGameStats();

    game = buildGame();
    statisticsCounter.setGame(game);

    statisticsCounter.updateAllTotalStats();
    statisticsCounter.resetGameStats();

    assertEquals(3, statisticsCounter.getTotalMoves(pawn1));
    assertEquals(4, statisticsCounter.getTotalMoves(pawn2));
    assertEquals(2, statisticsCounter.getTotalWalls(pawn1));
    assertEquals(1, statisticsCounter.getTotalWalls(pawn2));
    assertEquals(3, statisticsCounter.getTotalGamesPlayed());
  }

  @Test
  void reportIsGenerated(){
    assertNotNull(statisticsCounter.generateStatisticsReport());
  }
}