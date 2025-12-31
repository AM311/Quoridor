package it.units.sdm.quoridor.controller.engine;

import it.units.sdm.quoridor.controller.engine.abstracts.AbstractEngineFactory;
import it.units.sdm.quoridor.controller.engine.abstracts.CLIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.exceptions.FactoryException;

public class ServerEngineFactory implements AbstractEngineFactory {
  @Override
  public CLIQuoridorGameEngine createCLIEngine(EngineContext context) throws FactoryException {
    return new ServerStandardCLIQuoridorGameEngine(
            context.getBuilder(), context.getParser(), context.getStatisticsCounter(), context.getReader().orElseThrow(() -> new FactoryException("Missing 'Reader' parameter in EngineContext")),
            context.getSocketReader().orElseThrow(() -> new FactoryException("Missing 'Socket Reader' parameter in EngineContext")), context.getSocketWriter().orElseThrow(() -> new FactoryException("Missing 'Socket Writer' parameter in EngineContext"))
    );
  }

  @Override
  public GUIQuoridorGameEngine createGUIEngine(EngineContext context) throws FactoryException {
    return new ServerStandardGUIQuoridorGameEngine(
            context.getBuilder(),
            context.getParser(), context.getStatisticsCounter(),
            context.getSocketReader().orElseThrow(() -> new FactoryException("Missing 'Socket Reader' parameter in EngineContext")),
            context.getSocketWriter().orElseThrow(() -> new FactoryException("Missing 'Socket Writer' parameter in EngineContext"))
    );
  }
}
