package testDoubles;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractGameBoard;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.util.List;

public class StubGame extends AbstractGame {

  public StubGame(AbstractGameBoard gameBoard, List<AbstractPawn> pawns) {
    super(gameBoard, pawns, 0); // setta il primo pawn come giocante
  }

  @Override
  public void placeWall(Position startingPosition, WallOrientation wallOrientation) {
    // no-op per test
  }

  @Override
  public void movePlayingPawn(Position destinationPosition) {
    // no-op per test
  }

  @Override
  public void changeRound() {
    // no-op per test
  }

  @Override
  public boolean isGameFinished() {
    return false; // il test non necessita di game finito
  }
}
