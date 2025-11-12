package it.units.sdm.quoridor.controller.engine.gui;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.engine.QuoridorGameEngine;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.view.gui.GameEventListener;
import it.units.sdm.quoridor.view.gui.GameView;

import java.util.List;

public abstract class GUIQuoridorGameEngine extends QuoridorGameEngine {
  protected final GameView gameView;
  public GameEventListener eventListener;
  protected GUIAction currentGUIAction = GUIAction.DO_NOTHING;


  public GUIQuoridorGameEngine(AbstractQuoridorBuilder builder, StatisticsCounter statisticsCounter, QuoridorParser parser) {
    super(builder, statisticsCounter, parser);
    this.gameView = new GameView(this);
  }

  public void setEventListener(GameEventListener eventListener) {
    this.eventListener = eventListener;
  }

  public void setCurrentAction(GUIAction currentGUIAction) {
    this.currentGUIAction = currentGUIAction;
  }

  abstract public void handleTileClick(Position position);

  abstract public void setMoveAction();

  abstract public void setPlaceWallAction();

  public int getNumberOfPlayers() {
    return game.getPawns().size();
  }

  public int getSideLength() {
    return game.getGameBoard().getSideLength();
  }

  public int getPlayingPawnIndex() {
    return game.getPlayingPawnIndex();
  }

  public void changeRound() {
    game.changeRound();
  }

  @Override
  protected void printHelp() {
  }

  @Override
  public void handleQuitGame() {
    super.handleQuitGame();
  }

  @Override
  public void handleRestartGame() throws BuilderException {
    super.handleRestartGame();
  }

  public List<AbstractPawn> getPawns() {
    return game.getPawns();
  }

  abstract public List<Position> getValidMovePositions();

  public enum GUIAction {
    MOVE, PLACE_VERTICAL_WALL, PLACE_HORIZONTAL_WALL, DO_NOTHING
  }
}