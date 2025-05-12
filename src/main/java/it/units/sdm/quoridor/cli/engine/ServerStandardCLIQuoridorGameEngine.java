package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;

import java.io.BufferedReader;

public class ServerStandardCLIQuoridorGameEngine extends StandardCLIQuoridorGameEngine{
  public ServerStandardCLIQuoridorGameEngine(BufferedReader reader, QuoridorParser parser, AbstractQuoridorBuilder builder, StatisticsCounter statisticsCounter) {
    super(reader, parser, builder, statisticsCounter);
  }
}
