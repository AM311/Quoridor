package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.QuoridorRuntimeException;
import it.units.sdm.quoridor.model.Game;

public class GameActionManager {
	private final Game game;

	public GameActionManager(Game game) {
		this.game = game;
	}

	public <T> void performAction(Action<T> action, T target) throws InvalidActionException {
		try {
			action.execute(game.getGameBoard(), game.getPlayingPawn(), target);
		} catch (QuoridorRuntimeException ex) {
			throw new InvalidActionException("Invalid move: " + ex.getMessage());
		}
	}

	public <T> void performAction(Action<T> action, ActionChecker<T> actionChecker, T target) throws InvalidActionException {
		try{
			if(actionChecker.checkAction(game, target))
				performAction(action, target);
			else
				throw new InvalidActionException("Invalid move");
		} catch (QuoridorRuntimeException ex) {
			throw new InvalidActionException("Invalid move: " + ex.getMessage());
		}

	}
}
