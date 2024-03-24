package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.movemanager.*;
import it.units.sdm.quoridor.utils.Pair;

import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

public class Game {
  private List<Pawn> pawns;
  private final GameBoard gameBoard;
  private final GameActionManager actionManager;
  private Pawn playingPawn;

  public Game(int numberOfPlayers) throws InvalidParameterException {
    this.gameBoard = new GameBoard();

    switch (numberOfPlayers) {
      case 2 -> initialize(2);
      case 4 -> initialize(4);
      default -> throw new InvalidParameterException("Invalid number of players");
    }

    this.actionManager = new GameActionManager(this);
    this.playingPawn = pawns.getFirst();
  }

  private void initialize(int numberOfPlayers) {
    final int stdNumberOfWalls = 10;
    int numberOfWalls = stdNumberOfWalls / (numberOfPlayers / 2);

    List<Color> pawnsColors = List.of(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
    List<Pair<Tile, List<Tile>>> startingAndDestinationTiles = gameBoard.getStartingAndDestinationTiles();

    IntStream.range(0, numberOfPlayers).forEach(i -> startingAndDestinationTiles.get(i).getKey().setOccupied(true));
    this.pawns = IntStream.range(0, numberOfPlayers).mapToObj(i -> new Pawn(startingAndDestinationTiles.get(i).getKey(), startingAndDestinationTiles.get(i).getValue(), pawnsColors.get(i), numberOfWalls)).toList();
  }

  public List<Pawn> getPawns() {
    return pawns;
  }

  public GameBoard getGameBoard() {
    return gameBoard;
  }

  public Pawn getPlayingPawn() {
    return playingPawn;
  }

  public void placeWall(Wall wall) {
    actionManager.performAction(new WallPlacer(), new WallPlacementChecker(), wall);
  }

  public void movePlayingPawn(Tile destinationTile) {
    actionManager.performAction(new PawnMover(), new PawnMovementChecker(), destinationTile);
  }

  public void changeRound() {
    try {
      playingPawn = pawns.get(pawns.indexOf(playingPawn) + 1);
    } catch (IndexOutOfBoundsException ex) {
      playingPawn = pawns.getFirst();
    }
  }
}