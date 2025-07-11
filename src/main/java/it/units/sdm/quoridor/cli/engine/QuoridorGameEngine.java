package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.BuilderDirector;

public abstract class QuoridorGameEngine {

  protected AbstractQuoridorBuilder builder;
  protected StatisticsCounter statisticsCounter;

  public QuoridorGameEngine(AbstractQuoridorBuilder builder,StatisticsCounter statisticsCounter) {
    this.builder = builder;
    this.statisticsCounter = statisticsCounter;
  }

  public abstract void runGame() throws BuilderException;

  protected AbstractGame createGame() throws BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(builder);
    return builderDirector.makeGame();
  }
}
