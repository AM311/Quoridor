package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.GUI.view.GameEventListener;
import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;

import java.util.List;

public abstract class GUIQuoridorGameEngine extends QuoridorGameEngine {
  protected GameEventListener eventListener;

  public GUIQuoridorGameEngine(AbstractQuoridorBuilder builder, StatisticsCounter statisticsCounter) {
    super(builder, statisticsCounter);
  }

  public void setEventListener(GameEventListener eventListener) {
    this.eventListener = eventListener;
  }

  abstract public void setCurrentAction(GUIAction currentGUIAction);

  abstract public void handleTileClick(Position position);

  abstract public void setMoveAction();

  abstract public void setPlaceWallAction();

  abstract public int getNumberOfPlayers();

  abstract public int getPlayingPawnIndex();

  abstract public int getSideLength();

  abstract public void changeRound();

  abstract public List<AbstractPawn> getPawns();

  abstract public boolean isGameFinished();

  abstract public void restartGame();

  abstract public List<Position> getValidMovePositions();

  //todo SERVE ??? -- UNIFORMARE CON CLI...
  public enum GUIAction {
    MOVE, PLACE_VERTICAL_WALL, PLACE_HORIZONTAL_WALL, DO_NOTHING
  }
}