import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsCounterTest {

  StatisticsCounter statisticsCounter;
  AbstractGame game;

  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StandardQuoridorBuilder(2));
    return builderDirector.makeGame();
  }

  @BeforeEach
  void setUp() throws InvalidParameterException, BuilderException {
    statisticsCounter = new StatisticsCounter();
    game = buildGame();
    statisticsCounter.setGame(game);
  }

  @Test
  void updateGameMovesIsCoherent() {
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
  void updateGameWallsIsCoherent() {
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
  void resetGamesStatsIsCoherent() {
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
  void updateAllTotalStatsIsCoherent() {
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
  void totalStatsAfterMultipleGamesAreCoherent() throws InvalidParameterException, BuilderException {
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

  @Test
  void getWinRate_isCoherent() {
    String pawn1 = String.valueOf(game.getPawns().get(0));
    String pawn2 = String.valueOf(game.getPawns().get(1));

    statisticsCounter.updateAllTotalStats();
    statisticsCounter.updateAllTotalStats();
    game.changeRound();
    statisticsCounter.updateAllTotalStats();
    game.changeRound();
    statisticsCounter.updateAllTotalStats();

    double actualWinRatePawn1 = statisticsCounter.getWinRate(pawn1);
    double actualWinRatePawn2 = statisticsCounter.getWinRate(pawn2);

    Assertions.assertEquals(75.0, actualWinRatePawn1);
    Assertions.assertEquals(25.0, actualWinRatePawn2);
  }

  @Test
  void getGame_isNotNull(){
    assertNotNull(statisticsCounter.getGame());
  }
}