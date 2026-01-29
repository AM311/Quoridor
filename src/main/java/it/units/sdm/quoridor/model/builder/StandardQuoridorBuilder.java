package it.units.sdm.quoridor.model.builder;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.abstracts.AbstractGameBoard;
import it.units.sdm.quoridor.model.abstracts.AbstractPawn;
import it.units.sdm.quoridor.model.abstracts.AbstractTile;
import it.units.sdm.quoridor.model.movemanagement.ActionController;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.PathExistenceChecker;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.PawnMovementChecker;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.WallPlacementChecker;
import it.units.sdm.quoridor.model.movemanagement.actionmanagers.ActionManager;
import it.units.sdm.quoridor.model.movemanagement.actionmanagers.GameActionManager;
import it.units.sdm.quoridor.model.movemanagement.actions.PawnMover;
import it.units.sdm.quoridor.model.movemanagement.actions.WallPlacer;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.TargetTiles;
import it.units.sdm.quoridor.view.PawnAppearance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static it.units.sdm.quoridor.model.abstracts.AbstractTile.LinkState.EDGE;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class StandardQuoridorBuilder extends AbstractQuoridorBuilder {
  public final static int GAMEBOARD_SIDE_LENGTH = 9;
  public final static int DEFAULT_NUMBER_OF_WALLS = 10;
  public final static int[] ALLOWED_NUMBER_OF_PLAYERS = {2, 4};

  private AbstractGameBoard gameBoard;
  private List<AbstractPawn> pawns;
  private ActionManager actionManager;
  private ActionController<Wall> placeWallActionController;
  private ActionController<AbstractTile> movePawnActionController;

  public StandardQuoridorBuilder(int numberOfPlayers) throws InvalidParameterException {
    super(GAMEBOARD_SIDE_LENGTH, DEFAULT_NUMBER_OF_WALLS, numberOfPlayers);

    if (Arrays.stream(ALLOWED_NUMBER_OF_PLAYERS).noneMatch(i -> i == numberOfPlayers)) {
      throw new InvalidParameterException("Invalid number of players: " + numberOfPlayers);
    }
  }

  @Override
  AbstractGame buildGame() {
    int playingPawn = 0;

    for (int i = 0; i < numberOfPlayers; i++) {
      gameBoard.getStartingAndDestinationTiles().get(i).startingTile().setOccupiedBy(pawns.get(i));
    }

    return new Game(gameBoard, pawns, playingPawn, actionManager, movePawnActionController, placeWallActionController);
  }

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
    int numberOfWalls = defaultNumberOfWalls / (numberOfPlayers / 2);
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
    this.placeWallActionController = new ActionController<>(new WallPlacer(), false, new WallPlacementChecker(), new PathExistenceChecker());
    return this;
  }

  @Override
  AbstractQuoridorBuilder setMovePawnActionController() {
    this.movePawnActionController = new ActionController<>(new PawnMover(), false, new PawnMovementChecker());
    return this;
  }
}
