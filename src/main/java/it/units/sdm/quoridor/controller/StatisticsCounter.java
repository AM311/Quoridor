package it.units.sdm.quoridor.controller;

import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.abstracts.AbstractPawn;

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
  private AbstractGame game;


  public void updateGameMoves(String key) {
    gameMoves.put(key, gameMoves.getOrDefault(key, 0) + 1);
  }

  public void updateGameWalls(String key) {
    gameWalls.put(key, gameWalls.getOrDefault(key, 0) + 1);
  }

  public void updateAllTotalStats() {
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

  public String generateStatisticsReport() {
    StringBuilder report = new StringBuilder();
    report.append("\n=========== LAST GAME STATISTICS ===========\n");
    report.append("-".repeat(44)).append("\n");
    report.append(String.format("%-14s | %-10s | %-12s |\n", "Pawn", "Moves Made", "Walls Placed"));
    report.append("-".repeat(44)).append("\n");

    for (int i = 0; i < game.getPawns().size(); i++) {
      String pawn = String.valueOf(game.getPawns().get(i));
      String playerLabel = "Pawn " + (i + 1) + " (" + pawn + ")";
      report.append(String.format("%-15s | %-10d | %-12d |\n",
              playerLabel, getGameMoves(pawn), getGameWalls(pawn)));
    }
    report.append("-".repeat(44)).append("\n");

    report.append("\nTotal Games Played: ").append(totalGamesPlayed).append("\n");

    report.append("\n======================= TOTAL STATISTICS ========================\n");
    report.append("-".repeat(65)).append("\n");
    report.append(String.format("%-14s | %-5s | %-8s | %-12s | %-12s |\n",
            "Pawn", "Wins", "Win Rate", "Total Moves", "Total Walls"));
    report.append("-".repeat(65)).append("\n");

    for (int i = 0; i < game.getPawns().size(); i++) {
      String pawn = String.valueOf(game.getPawns().get(i));
      String playerLabel = "Pawn " + (i + 1) + " (" + pawn + ")";
      report.append(String.format("%-15s | %-5d | %-8s | %-12d | %-12d |\n",
              playerLabel,
              getTotalWins(pawn),
              String.format("%.1f%%", getWinRate(pawn)),
              getTotalMoves(pawn),
              getTotalWalls(pawn)));
    }
    report.append("-".repeat(65)).append("\n");

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

  public double getWinRate(String key) {
    if (totalGamesPlayed == 0) return 0;
    return (getTotalWins(key) * 100.0) / totalGamesPlayed;
  }

  public void setGame(AbstractGame game) {
    this.game = game;
  }

  public AbstractGame getGame() {
    return game;
  }
}