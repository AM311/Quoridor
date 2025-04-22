package it.units.sdm.quoridor.cli;

import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractPawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsCounter {

  private int totalGamesPlayed = 0;
  private final Map<String, Integer> totalWins = new HashMap<>();
  private final Map<String, Integer> totalMoves = new HashMap<>();
  private final Map<String, Integer> totalWalls = new HashMap<>();
  private final Map<String, Integer> gameMoves = new HashMap<>();
  private final Map<String, Integer> gameWalls = new HashMap<>();


  public void updateGameMoves(String key) {
    gameMoves.put(key, gameMoves.getOrDefault(key, 0) + 1);
  }

  public void updateGameWalls(String key) {
    gameWalls.put(key, gameWalls.getOrDefault(key, 0) + 1);
  }

  public void updateAllTotalStats(AbstractGame game) {
    updateTotalGamesPlayed();
    updateTotalWins(game.getPlayingPawn().toString());
    ArrayList<String> keys = new ArrayList<>();
    for (AbstractPawn pawn : game.getPawns()) {
      keys.add(String.valueOf(pawn));
    }
    for (String key : keys) {
      updateTotalMoves(key);
      updateTotalWalls(key);
    }
  }

  public void resetGameStats() {
    gameMoves.clear();
    gameWalls.clear();
  }

  private void updateTotalGamesPlayed() {
    totalGamesPlayed++;
  }

  private void updateTotalWins(String key) {
    totalWins.put(key, totalWins.getOrDefault(key, 0) + 1);
  }

  private void updateTotalMoves(String key) {
    int movesThisGame = gameMoves.getOrDefault(key, 0);
    totalMoves.put(key, totalMoves.getOrDefault(key, 0) + movesThisGame);
  }

  private void updateTotalWalls(String key) {
    int wallsThisGame = gameWalls.getOrDefault(key, 0);
    totalWalls.put(key, totalWalls.getOrDefault(key, 0) + wallsThisGame);
  }

  public String generateStatisticsReport(AbstractGame game) {
    StringBuilder report = new StringBuilder();
    report.append("\n===== LAST GAME STATISTICS =====\n");
    for (int i = 0; i < game.getPawns().size(); i++) {
      String pawn = String.valueOf(game.getPawns().get(i));
      report.append("Player ").append(i + 1).append(" (").append(pawn).append("):\n");
      report.append("  Moves Made: ").append(getGameMoves(pawn)).append("\n");
      report.append("  Walls Placed: ").append(getGameWalls(pawn)).append("\n\n");
    }


    report.append("\n===== GLOBAL STATISTICS =====\n");
    report.append("Total Games Played: ").append(totalGamesPlayed).append("\n\n");

    for (int i = 0; i < game.getPawns().size(); i++) {
      String pawn = String.valueOf(game.getPawns().get(i));
      report.append("Player ").append(i + 1).append(" (").append(pawn).append("):\n");
      report.append("  Wins: ").append(getTotalWins(pawn)).append("\n");
      report.append("  Win Rate: ").append(String.format("%.1f%%", getWinRate(pawn))).append("\n");
      report.append("  Total Moves Made: ").append(getTotalMoves(pawn)).append("\n");
      report.append("  Total Walls Placed: ").append(getTotalWalls(pawn)).append("\n\n");
    }

    return report.toString();
  }

  public int getTotalGamesPlayed() {
    return totalGamesPlayed;
  }

  public int getTotalWins(String key) {
    return totalWins.getOrDefault(key, 0);
  }

  public int getTotalMoves(String key) {
    return totalMoves.getOrDefault(key, 0);
  }

  public int getTotalWalls(String key) {
    return totalWalls.getOrDefault(key, 0);
  }

  public int getGameMoves(String key) {
    return gameMoves.getOrDefault(key, 0);
  }

  public int getGameWalls(String key) {
    return gameWalls.getOrDefault(key, 0);
  }

  private double getWinRate(String key) {
    if (totalGamesPlayed == 0) return 0;
    return (getTotalWins(key) * 100.0) / totalGamesPlayed;
  }
}