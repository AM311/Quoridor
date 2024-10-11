package it.units.sdm.quoridor.movemanagement.actionmanagers;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.QuoridorRuntimeException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.movemanagement.actioncheckers.ActionChecker;
import it.units.sdm.quoridor.utils.ActionController;

public class GameActionManager implements ActionManager {

	@Override
	public <T> void performAction(AbstractGame game, T target, ActionController<T> actionController, boolean useOrInsteadOfAnd) throws InvalidActionException {
		try {
			boolean isValidMove = !useOrInsteadOfAnd;

			for(ActionChecker<T> actionChecker : actionController.actionCheckers()) {
				if(useOrInsteadOfAnd)
          isValidMove = isValidMove || actionChecker.isValidAction(game, target);
        else
          isValidMove = isValidMove && actionChecker.isValidAction(game, target);
			}

			if(isValidMove) {
				actionController.action().execute(game, target);
			}

		} catch (QuoridorRuntimeException ex) {
			throw new InvalidActionException("Invalid move: " + ex.getMessage());
		}
	}
}
