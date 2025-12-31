import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.engine.EngineContext;
import it.units.sdm.quoridor.controller.engine.EngineContext.ContextBuilder;
import it.units.sdm.quoridor.controller.engine.LocalEngineFactory;
import it.units.sdm.quoridor.controller.engine.ServerEngineFactory;
import it.units.sdm.quoridor.controller.engine.abstracts.AbstractEngineFactory;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.controller.parser.StandardQuoridorParser;
import it.units.sdm.quoridor.exceptions.FactoryException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class EngineFactoryTest {
  private ContextBuilder contextBuilder;

  @BeforeEach
  public void setup() {
    contextBuilder = new ContextBuilder();
  }

  @Test
  void contextTest_contextIsCorrectlyBuilt() throws InvalidParameterException {
    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2));

    Assertions.assertInstanceOf(EngineContext.class, contextBuilder.build());
  }

  @Test
  void contextTest_parserIsSetByCore() throws InvalidParameterException {
    QuoridorParser parser = new StandardQuoridorParser();

    contextBuilder.setCore(parser, new StatisticsCounter(), new StandardQuoridorBuilder(2));

    Assertions.assertEquals(parser, contextBuilder.build().getParser());
  }

  @Test
  void contextTest_builderIsSetByCore() throws InvalidParameterException {
    AbstractQuoridorBuilder builder = new StandardQuoridorBuilder(2);

    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), builder);

    Assertions.assertEquals(builder, contextBuilder.build().getBuilder());
  }

  @Test
  void contextTest_statisticsCounterIsSetByCore() throws InvalidParameterException {
    StatisticsCounter counter = new StatisticsCounter();

    contextBuilder.setCore(new StandardQuoridorParser(), counter, new StandardQuoridorBuilder(2));

    Assertions.assertEquals(counter, contextBuilder.build().getStatisticsCounter());
  }

  @Test
  void contextTest_readerIsNotSetByCore() throws InvalidParameterException {
    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2));

    Assertions.assertFalse(contextBuilder.build().getReader().isPresent());
  }

  @Test
  void contextTest_socketIsNotSetByCore() throws InvalidParameterException {
    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2));

    Assertions.assertFalse(contextBuilder.build().getSocketReader().isPresent() || contextBuilder.build().getSocketWriter().isPresent());
  }

  @Test
  void contextTest_readerIsSet() throws InvalidParameterException {
    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2))
            .setSocket(new BufferedReader(new InputStreamReader(System.in)), new BufferedWriter(new OutputStreamWriter(System.out)));

    Assertions.assertTrue(contextBuilder.build().getSocketReader().isPresent() && contextBuilder.build().getSocketWriter().isPresent());
  }

  // ---

  @Test
  void localFactoryTest_createCLIEngine_correctContextDoesNotThrowFactoryException() throws InvalidParameterException {
    AbstractEngineFactory factory = new LocalEngineFactory();

    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2))
            .setReader(new BufferedReader(new InputStreamReader(System.in)));

    Assertions.assertDoesNotThrow(() -> factory.createCLIEngine(contextBuilder.build()));
  }

  @Test
  void localFactoryTest_createCLIEngine_wrongContextThrowsFactoryException() throws InvalidParameterException {
    AbstractEngineFactory factory = new LocalEngineFactory();

    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2));

    Assertions.assertThrows(FactoryException.class, () -> factory.createCLIEngine(contextBuilder.build()));
  }

  @Test
  void localFactoryTest_createCLIEngine_richerContextDoesNotThrowFactoryException() throws InvalidParameterException {
    AbstractEngineFactory factory = new LocalEngineFactory();

    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2))
            .setReader(new BufferedReader(new InputStreamReader(System.in)))
            .setSocket(new BufferedReader(new InputStreamReader(System.in)), new BufferedWriter(new OutputStreamWriter(System.out)));

    Assertions.assertDoesNotThrow(() -> factory.createCLIEngine(contextBuilder.build()));
  }

  @Test
  void localFactoryTest_createGUIEngine_correctContextDoesNotThrowFactoryException() throws InvalidParameterException {
    AbstractEngineFactory factory = new LocalEngineFactory();

    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2));

    Assertions.assertDoesNotThrow(() -> factory.createGUIEngine(contextBuilder.build()));
  }

  @Test
  void serverFactoryTest_createCLIEngine_correctContextDoesNotThrowFactoryException() throws InvalidParameterException {
    AbstractEngineFactory factory = new ServerEngineFactory();

    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2))
            .setReader(new BufferedReader(new InputStreamReader(System.in)))
            .setSocket(new BufferedReader(new InputStreamReader(System.in)), new BufferedWriter(new OutputStreamWriter(System.out)));

    Assertions.assertDoesNotThrow(() -> factory.createCLIEngine(contextBuilder.build()));
  }

  @Test
  void serverFactoryTest_createCLIEngine_wrongContextThrowsFactoryException() throws InvalidParameterException {
    AbstractEngineFactory factory = new ServerEngineFactory();

    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2));

    Assertions.assertThrows(FactoryException.class, () -> factory.createCLIEngine(contextBuilder.build()));
  }

  @Test
  void serverFactoryTest_createGUIEngine_correctContextDoesNotThrowFactoryException() throws InvalidParameterException {
    AbstractEngineFactory factory = new ServerEngineFactory();

    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2))
            .setSocket(new BufferedReader(new InputStreamReader(System.in)), new BufferedWriter(new OutputStreamWriter(System.out)));

    Assertions.assertDoesNotThrow(() -> factory.createGUIEngine(contextBuilder.build()));
  }

  @Test
  void serverFactoryTest_createGUIEngine_wrongContextThrowsFactoryException() throws InvalidParameterException {
    AbstractEngineFactory factory = new ServerEngineFactory();

    contextBuilder.setCore(new StandardQuoridorParser(), new StatisticsCounter(), new StandardQuoridorBuilder(2));

    Assertions.assertThrows(FactoryException.class, () -> factory.createGUIEngine(contextBuilder.build()));
  }

}
