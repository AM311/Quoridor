package it.units.sdm.quoridor.controller.engine.abstracts;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;


public abstract class CLIQuoridorGameEngine extends QuoridorGameEngine {
  public CLIQuoridorGameEngine(AbstractQuoridorBuilder builder, StatisticsCounter statisticsCounter, QuoridorParser parser) {
    super(builder, statisticsCounter, parser);
  }
}
