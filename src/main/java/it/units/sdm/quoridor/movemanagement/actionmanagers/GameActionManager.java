package it.units.sdm.quoridor.movemanagement.actionmanagers;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.QuoridorRuntimeException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.movemanagement.actioncheckers.CheckResult;
import it.units.sdm.quoridor.utils.ActionController;

import static it.units.sdm.quoridor.movemanagement.actioncheckers.QuoridorCheckResult.*;

public class GameActionManager implements ActionManager {

  @Override
  public <T> void performAction(AbstractGame game, T target, ActionController<T> actionController, boolean useOrInsteadOfAnd) throws InvalidActionException {
    CheckResult lastWrongResult = null;

    try {
      for (ActionChecker<T> actionChecker : actionController.actionCheckers()) {
        CheckResult result = actionChecker.isValidAction(game, target);

        if (useOrInsteadOfAnd) {
          if (result == OKAY) {
            actionController.action().execute(game, target);
            return;
          }

          lastWrongResult = result;
        } else {
          if (result != OKAY) {
            explainInvalidMove(result);
          }
        }
      }

      if (!useOrInsteadOfAnd || actionController.actionCheckers().length == 0) {
        actionController.action().execute(game, target);
      } else {
        explainInvalidMove(lastWrongResult);
      }
    } catch (QuoridorRuntimeException ex) {
      throw new InvalidActionException("Invalid action: " + ex.getMessage());
    }
  }

  private void explainInvalidMove(CheckResult result) throws InvalidActionException {
    throw new InvalidActionException(switch (result) {
      case END_OF_AVAILABLE_WALLS -> "You have end the walls that you can place";
      case INVALID_WALL_POSITION -> "The required wall position is invalid";
      case BLOCKING_WALL -> "The required wall position is invalid because it blocks the pawn";
      case INVALID_MOVEMENT -> "You are requiring an invalid movement";
      case OCCUPIED_TILE -> "Your target tile is already occupied so you cannot place there your pawn";
      default -> "Invalid case";
    });
  }
}
