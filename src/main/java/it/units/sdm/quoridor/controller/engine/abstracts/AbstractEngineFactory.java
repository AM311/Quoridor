package it.units.sdm.quoridor.controller.engine.abstracts;


import it.units.sdm.quoridor.controller.engine.EngineContext;
import it.units.sdm.quoridor.exceptions.FactoryException;

public interface AbstractEngineFactory {
  CLIQuoridorGameEngine createCLIEngine(EngineContext context) throws FactoryException;

  GUIQuoridorGameEngine createGUIEngine(EngineContext context) throws FactoryException;
}
