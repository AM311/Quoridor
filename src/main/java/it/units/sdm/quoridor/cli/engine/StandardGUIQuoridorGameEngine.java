package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.GUI.controller.GameController;
import it.units.sdm.quoridor.GUI.view.GameView;
import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;

public class StandardGUIQuoridorGameEngine extends QuoridorGameEngine {

  public StandardGUIQuoridorGameEngine(AbstractQuoridorBuilder quoridorBuilder, StatisticsCounter statisticsCounter) {
    super(quoridorBuilder, statisticsCounter);
  }

  @Override
  public void runGame() throws BuilderException {
    AbstractGame game = createGame();
    GameController gameController = new GameController(game, statisticsCounter);
    GameView gameView = new GameView(gameController);
    gameView.displayGUI();
  }
}