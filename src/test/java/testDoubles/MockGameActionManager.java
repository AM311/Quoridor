package testDoubles;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.QuoridorRuntimeException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.movemanagement.actionmanagers.ActionManager;
import it.units.sdm.quoridor.utils.ActionController;

public class MockGameActionManager implements ActionManager {
  private boolean isValidAction;

  public boolean isValidAction() {
    return isValidAction;
  }

  @Override
  public <T> void performAction(AbstractGame game, T target, ActionController<T> actionController, boolean useOrInsteadOfAnd) throws InvalidActionException {
    try {
      isValidAction = !useOrInsteadOfAnd || actionController.actionCheckers().length == 0;

      for (ActionChecker<T> actionChecker : actionController.actionCheckers()) {
        if (useOrInsteadOfAnd)
          isValidAction = isValidAction || actionChecker.isValidAction(game, target);
        else
          isValidAction = isValidAction && actionChecker.isValidAction(game, target);
      }

    } catch (QuoridorRuntimeException ex) {
      throw new InvalidActionException("Invalid action: " + ex.getMessage());
    }
  }
}
