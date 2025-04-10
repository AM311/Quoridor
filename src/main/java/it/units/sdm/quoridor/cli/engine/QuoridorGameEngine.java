package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;

public abstract class QuoridorGameEngine {

  protected AbstractQuoridorBuilder builder;

  public QuoridorGameEngine(AbstractQuoridorBuilder builder) {
    this.builder = builder;
  }

  public abstract void runGame() throws BuilderException;
}
