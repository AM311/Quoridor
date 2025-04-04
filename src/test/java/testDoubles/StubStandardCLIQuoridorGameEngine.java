package testDoubles;

import it.units.sdm.quoridor.cli.parser.QuoridorParser;

import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;

import java.io.Reader;
public class StubStandardCLIQuoridorGameEngine {

  private final Reader reader;
  private final QuoridorParser parser;
  private final AbstractQuoridorBuilder builder;

  public StubStandardCLIQuoridorGameEngine(Reader reader, QuoridorParser parser, AbstractQuoridorBuilder builder) {
    this.reader = reader;
    this.parser = parser;
    this.builder = builder;
  }

  public void startGame() {
  }
}
