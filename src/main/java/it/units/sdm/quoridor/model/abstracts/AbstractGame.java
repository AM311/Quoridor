package it.units.sdm.quoridor.model.abstracts;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static it.units.sdm.quoridor.view.cli.GameStringBuilder.createGameString;

public abstract class AbstractGame implements Cloneable {
  protected List<AbstractPawn> pawns;
  protected AbstractGameBoard gameBoard;
  protected int playingPawn;

  public AbstractGame(AbstractGameBoard gameBoard, List<AbstractPawn> pawns, int playingPawn) {
    this.playingPawn = playingPawn;
    this.gameBoard = gameBoard;
    this.pawns = pawns;
  }

  public List<AbstractPawn> getPawns() {
    return List.copyOf(pawns);
  }

  public int getPlayingPawnIndex() {
    return playingPawn;
  }

  public AbstractGameBoard getGameBoard() {
    return gameBoard;
  }

  public AbstractPawn getPlayingPawn() {
    return pawns.get(playingPawn);
  }

  public abstract void placeWall(Position startingPosition, WallOrientation wallOrientation) throws InvalidActionException, InvalidParameterException;

  public abstract void movePlayingPawn(Position destinationPosition) throws InvalidParameterException, InvalidActionException;

  public abstract void changeRound();

  public abstract boolean isGameFinished();

  public abstract List<Position> getValidMovePositions();

  @Override
  public int hashCode() {
    return Objects.hash(pawns, gameBoard, playingPawn);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof AbstractGame that))
      return false;
    return playingPawn == that.playingPawn && Objects.equals(pawns, that.pawns) && Objects.equals(gameBoard, that.gameBoard);
  }

  @Override
  public AbstractGame clone() throws CloneNotSupportedException {
    AbstractGame clonedGame = (AbstractGame) super.clone();
    clonedGame.gameBoard = gameBoard.clone();
    clonedGame.pawns = new ArrayList<>();

    for (AbstractPawn pawn : pawns) {
      Collection<AbstractTile> destinationTiles = new ArrayList<>();
      AbstractTile currentTile = null;

      try {
        for (AbstractTile tile : pawn.getDestinationTiles()) {
          destinationTiles.add(clonedGame.gameBoard.getTile(new Position(tile.getRow(), tile.getColumn())));
        }

        currentTile = clonedGame.gameBoard.getTile(new Position(pawn.getCurrentTile().getRow(), pawn.getCurrentTile().getColumn()));
      } catch (InvalidParameterException ignored) {
      }

      clonedGame.pawns.add(new Pawn(currentTile, destinationTiles, pawn.getPawnAppearance(), pawn.getNumberOfWalls()));
    }

    return clonedGame;
  }

  public String toString() {
    return createGameString(this);
  }


}
