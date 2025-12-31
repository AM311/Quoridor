package it.units.sdm.quoridor.controller.engine;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Optional;

public final class EngineContext {
  private final QuoridorParser parser;
  private final StatisticsCounter statisticsCounter;
  private final AbstractQuoridorBuilder builder;
  private final BufferedReader reader;
  private final BufferedReader socketReader;
  private final BufferedWriter socketWriter;

  private EngineContext(ContextBuilder b) {
    this.parser = b.parser;
    this.statisticsCounter = b.statistics;
    this.builder = b.builder;
    this.reader = b.reader;
    this.socketReader = b.socketReader;
    this.socketWriter = b.socketWriter;
  }

  public QuoridorParser getParser() {
    return parser;
  }

  public StatisticsCounter getStatisticsCounter() {
    return statisticsCounter;
  }

  public AbstractQuoridorBuilder getBuilder() {
    return builder;
  }

  public Optional<BufferedReader> getReader() {
    return Optional.ofNullable(reader);
  }

  public Optional<BufferedReader> getSocketReader() {
    return Optional.ofNullable(socketReader);
  }

  public Optional<BufferedWriter> getSocketWriter() {
    return Optional.ofNullable(socketWriter);
  }

  public static class ContextBuilder {
    private QuoridorParser parser;
    private StatisticsCounter statistics;
    private AbstractQuoridorBuilder builder;

    private BufferedReader reader;
    private BufferedReader socketReader;
    private BufferedWriter socketWriter;

    public ContextBuilder setCore(QuoridorParser p, StatisticsCounter s, AbstractQuoridorBuilder b) {
      this.parser = p;
      this.statistics = s;
      this.builder = b;
      return this;
    }

    public ContextBuilder setReader(BufferedReader r) {
      this.reader = r;
      return this;
    }

    public ContextBuilder setSocket(BufferedReader sr, BufferedWriter sw) {
      this.socketReader = sr;
      this.socketWriter = sw;
      return this;
    }

    public EngineContext build() {
      return new EngineContext(this);
    }
  }
}
