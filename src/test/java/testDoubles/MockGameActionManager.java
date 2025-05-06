package testDoubles;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.QuoridorRuntimeException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.movemanagement.actioncheckers.CheckResult;
import it.units.sdm.quoridor.movemanagement.actionmanagers.ActionManager;
import it.units.sdm.quoridor.utils.ActionController;

import static testDoubles.StubActionChecker.StubCheckResult.OKAY;

public class MockGameActionManager implements ActionManager {
  private boolean isValidAction;

  public boolean isValidAction() {
    return isValidAction;
  }

  @Override
  public <T> void performAction(AbstractGame game, T target, ActionController<T> actionController, boolean useOrInsteadOfAnd) throws InvalidActionException {
    try {
      for (ActionChecker<T> actionChecker : actionController.actionCheckers()) {
        CheckResult result = actionChecker.isValidAction(game, target);

        if (useOrInsteadOfAnd) {
          if (result == OKAY) {
            isValidAction = true;
            return;
          }

        } else {
          if (result != OKAY) {
            isValidAction = false;
            return;
          }
        }
      }

      if (!useOrInsteadOfAnd || actionController.actionCheckers().length == 0) {
        isValidAction = true;
      } else {
        isValidAction = false;
      }
    } catch (QuoridorRuntimeException ex) {
      throw new InvalidActionException("Invalid action: " + ex.getMessage());
    }
  }
}
