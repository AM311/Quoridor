package it.units.sdm.quoridor.model.builder;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.PawnAppearance;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.PawnMovementChecker;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.PathExistenceChecker;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.WallPlacementChecker;
import it.units.sdm.quoridor.model.movemanagement.actionmanagers.ActionManager;
import it.units.sdm.quoridor.model.movemanagement.actionmanagers.GameActionManager;
import it.units.sdm.quoridor.model.movemanagement.actions.PawnMover;
import it.units.sdm.quoridor.model.movemanagement.actions.WallPlacer;
import it.units.sdm.quoridor.utils.ActionController;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.TargetTiles;

import java.util.List;
import java.util.stream.IntStream;

import static it.units.sdm.quoridor.model.AbstractTile.LinkState.EDGE;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.RIGHT;

public class StdQuoridorBuilder extends AbstractQuoridorBuilder {
  private AbstractGameBoard gameBoard;
  private List<AbstractPawn> pawns;
  private ActionManager actionManager;
  private ActionController<Wall> placeWallActionController;
  private ActionController<AbstractTile> movePawnActionController;

  public StdQuoridorBuilder(int numberOfPlayers) throws InvalidParameterException {
    super(9, 10, numberOfPlayers);

    if (numberOfPlayers != 2 && numberOfPlayers != 4)
      throw new InvalidParameterException("Invalid number of players: " + numberOfPlayers);
  }

  @Override
  AbstractGame buildGame() {
    int playingPawn = 0;

    for (int i = 0; i < numberOfPlayers; i++) {
      gameBoard.getStartingAndDestinationTiles().get(i).startingTile().setOccupiedBy(pawns.get(i));
    }

    return new Game(gameBoard, pawns, playingPawn, actionManager, movePawnActionController, placeWallActionController);
  }

  //=====================

  @Override
  AbstractQuoridorBuilder setGameBoard() {
    AbstractTile[][] gameState = new AbstractTile[gameBoardSideLength][gameBoardSideLength];

    for (int i = 0; i < gameBoardSideLength; i++) {
      for (int j = 0; j < gameBoardSideLength; j++) {
        gameState[i][j] = new Tile(new Position(i, j));
      }
    }

    for (int i = 0; i < gameBoardSideLength; i++) {
      gameState[0][i].setLink(UP, EDGE);
      gameState[i][0].setLink(LEFT, EDGE);
      gameState[gameBoardSideLength - 1][i].setLink(DOWN, EDGE);
      gameState[i][gameBoardSideLength - 1].setLink(RIGHT, EDGE);
    }

    try {
      this.gameBoard = new GameBoard(gameState);
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }
    
    return this;
  }

  @Override
  AbstractQuoridorBuilder setPawnList() {
    int numberOfWalls = stdNumberOfWalls / (numberOfPlayers / 2);
    List<TargetTiles> startingAndDestinationTiles = gameBoard.getStartingAndDestinationTiles();

    this.pawns = IntStream.range(0, numberOfPlayers).mapToObj(
            i -> (AbstractPawn) new Pawn(
                    startingAndDestinationTiles.get(i).startingTile(),
                    startingAndDestinationTiles.get(i).destinationTiles(),
                    PawnAppearance.getDefaultPawnStyles().get(i),
                    numberOfWalls
            )
    ).toList();

    return this;
  }

  @Override
  AbstractQuoridorBuilder setActionManager() {
    this.actionManager = new GameActionManager();
    return this;
  }

  @Override
  AbstractQuoridorBuilder setPlaceWallActionController() {
    this.placeWallActionController = new ActionController<>(new WallPlacer(), new WallPlacementChecker(), new PathExistenceChecker());
    return this;
  }

  @Override
  AbstractQuoridorBuilder setMovePawnActionController() {
    this.movePawnActionController = new ActionController<>(new PawnMover(), new PawnMovementChecker());
    return this;
  }
}
