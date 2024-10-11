package it.units.sdm.quoridor.movemanagement.actioncheckers;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.directions.StraightDirection;

import java.util.*;

import static it.units.sdm.quoridor.model.AbstractTile.LinkState.FREE;
import static it.units.sdm.quoridor.model.AbstractTile.LinkState.WALL;

public class PathExistenceChecker implements ActionChecker<Wall> {
  @Override
  public boolean isValidAction(AbstractGame game, Wall wall) {
    AbstractGame dummyGame = buildDummyGame(game);      //todo VERIFICARE CON DEBUGGER CHE CLONI CORRETTAMENTE

    try {
      dummyGame.placeWall(new Position(wall.startingTile().getRow(), wall.startingTile().getColumn()), wall.orientation());
    } catch (InvalidActionException | InvalidParameterException e) {
      return false;
    }

    return executeDijkstraAlgorithm(dummyGame);
  }

  private AbstractGame buildDummyGame(AbstractGame game) {
    AbstractGame dummyGame;

    try {
      dummyGame = game.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }

    return dummyGame;
  }

  private boolean executeDijkstraAlgorithm(AbstractGame dummyGame) {
    for (AbstractPawn pawn : dummyGame.getPawns()) {
      Map<AbstractTile, Integer> potentials = new HashMap<>();
      Set<AbstractTile> toVisitTiles = new HashSet<>();

      for (AbstractTile tile : dummyGame.getGameBoard().getTiles()) {
        toVisitTiles.add(tile);
        potentials.put(tile, Integer.MAX_VALUE);
      }

      AbstractTile startingTile = pawn.getCurrentTile();
      potentials.put(startingTile, 0);

      visitTiles(dummyGame.getGameBoard(), startingTile, toVisitTiles, potentials);

      if (!checkPathExistence(potentials, pawn.getDestinationTiles()))
        return false;
    }

    return true;
  }

  private void visitTiles(AbstractGameBoard gameBoard, AbstractTile startingTile, Set<AbstractTile> toVisitTiles, Map<AbstractTile, Integer> potentials) {
    AbstractTile visitedTile = startingTile;

    while (!toVisitTiles.isEmpty()) {
      toVisitTiles.remove(visitedTile);

      for (StraightDirection direction : StraightDirection.values()) {
        visitAdjacentTile(gameBoard, visitedTile, direction, potentials);
      }
      visitedTile = makeAndGetTileDefinitive(toVisitTiles, potentials);
    }
  }

  private boolean checkPathExistence(Map<AbstractTile, Integer> potentials, Collection<AbstractTile> destinationTiles) {
    boolean existsPath = false;

    for (AbstractTile tile : potentials.keySet()) {
      if (destinationTiles.contains(tile)) {
        if (potentials.get(tile) == 0)
          existsPath = true;
      }
    }

    return existsPath;
  }

  private void visitAdjacentTile(AbstractGameBoard gameBoard, AbstractTile visitedTile, StraightDirection direction, Map<AbstractTile, Integer> potentials) {
    try {
      AbstractTile adjacentTile = gameBoard.getAdjacentTile(visitedTile, direction);
      if (visitedTile.getLink(direction) == FREE) {
        if (potentials.get(adjacentTile) > potentials.get(visitedTile))
          potentials.put(adjacentTile, potentials.get(visitedTile));
      } else if (visitedTile.getLink(direction) == WALL) {
        if (potentials.get(adjacentTile) > potentials.get(visitedTile) + 1)
          potentials.put(adjacentTile, potentials.get(visitedTile) + 1);
      }
    } catch (OutOfGameBoardException ignored) {
    }
  }

  private AbstractTile makeAndGetTileDefinitive(Set<AbstractTile> toVisitTiles, Map<AbstractTile, Integer> potentials) {
    AbstractTile minTile = null;

    for (AbstractTile tile : toVisitTiles) {
      if (minTile == null || potentials.get(minTile) > potentials.get(tile))
        minTile = tile;
    }

    return minTile;
  }
}
