package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.model.Game;

public class GameActionManager {
	private final Game game;

	public GameActionManager(Game game) {
		this.game = game;
	}

	public <T> void performAction(Action<T> action, T target) {
		action.execute(game.getGameBoard(), game.getPlayingPawn(), target);
	}

	public <T> void performAction(Action<T> action, ActionChecker<T> actionChecker, T target) {
		if(actionChecker.checkAction(game.getGameBoard(), game.getPlayingPawn(), target))
			performAction(action, target);
	}
}
