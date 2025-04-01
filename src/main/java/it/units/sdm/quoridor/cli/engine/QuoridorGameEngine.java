package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;

public abstract class QuoridorGameEngine {

  AbstractQuoridorBuilder builder;

  public QuoridorGameEngine(AbstractQuoridorBuilder builder) {
    this.builder = builder;
  }

  public void startGame() {}
}
