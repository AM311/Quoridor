package it.units.sdm.quoridor.model.movemanagement.actionmanagers;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.QuoridorRuntimeException;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.model.movemanagement.actioncheckers.CheckResult;
import it.units.sdm.quoridor.model.movemanagement.ActionController;

import static it.units.sdm.quoridor.model.movemanagement.actioncheckers.QuoridorCheckResult.*;

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
      case END_OF_AVAILABLE_WALLS -> "You have ended your walls!";
      case INVALID_WALL_POSITION -> "You cannot place a wall in the required position!";
      case BLOCKING_WALL -> "You cannot completely block a pawn!";
      case INVALID_MOVEMENT -> "You cannot place your pawn in the required tile!";
      case OCCUPIED_TILE -> "Your target tile is already occupied!";
      default -> "Invalid case";
    });
  }
}
