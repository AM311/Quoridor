package it.units.sdm.quoridor.controller.engine.abstracts;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.controller.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.model.abstracts.AbstractPawn;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.view.gui.GameView;

import java.util.Collection;
import java.util.List;

public abstract class GUIQuoridorGameEngine extends QuoridorGameEngine {
  public final GameView gameView;
  protected GUIAction currentGUIAction = GUIAction.DO_NOTHING;

  public GUIQuoridorGameEngine(AbstractQuoridorBuilder builder, StatisticsCounter statisticsCounter, QuoridorParser parser) {
    super(builder, statisticsCounter, parser);
    this.gameView = new GameView(this);
  }

  public enum GUIAction {
    MOVE, PLACE_VERTICAL_WALL, PLACE_HORIZONTAL_WALL, DO_NOTHING
  }

  public int getNumberOfPlayers() {
    return game.getPawns().size();
  }

  public int getSideLength() {
    return game.getGameBoard().getSideLength();
  }

  public int getPlayingPawnIndex() {
    return game.getPlayingPawnIndex();
  }

  public List<AbstractPawn> getPawns() {
    return game.getPawns();
  }

  public Collection<Position> getValidMovePositions(){
    return game.getValidMovePositions();
  }

  public void setCurrentAction(GUIAction currentGUIAction) {
    this.currentGUIAction = currentGUIAction;
  }

  public void changeRound() {
    game.changeRound();
  }

  @Override
  public void handleQuitGame() {
    super.handleQuitGame();
  }

  @Override
  public void handleRestartGame() throws BuilderException {
    super.handleRestartGame();
  }

  @Override
  protected void printHelp() {
  }

  abstract public void handleTileClick(Position position);

  abstract public void setMoveAction();

  abstract public void setPlaceWallAction();
}