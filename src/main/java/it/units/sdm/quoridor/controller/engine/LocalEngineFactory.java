package it.units.sdm.quoridor.controller.engine;

import it.units.sdm.quoridor.controller.engine.abstracts.AbstractEngineFactory;
import it.units.sdm.quoridor.controller.engine.abstracts.CLIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.exceptions.FactoryException;

public class LocalEngineFactory implements AbstractEngineFactory {
  @Override
  public CLIQuoridorGameEngine createCLIEngine(EngineContext context) throws FactoryException {
    return new StandardCLIQuoridorGameEngine(
            context.getBuilder(), context.getParser(), context.getStatisticsCounter(), context.getReader().orElseThrow(() -> new FactoryException("Missing 'Reader' parameter in EngineContext"))
    );
  }

  @Override
  public GUIQuoridorGameEngine createGUIEngine(EngineContext context) throws FactoryException {
    return new StandardGUIQuoridorGameEngine(
            context.getBuilder(),
            context.getParser(), context.getStatisticsCounter()
    );
  }
}
