package it.units.sdm.quoridor.GUI.view.managers;

import it.units.sdm.quoridor.GUI.view.panels.GameBoardPanelComponent;
import it.units.sdm.quoridor.GUI.view.panels.RootPanelComponent;
import it.units.sdm.quoridor.cli.engine.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import javax.swing.*;

public class PanelsManager {

  private final GUIQuoridorGameEngine gameEngine;
  private final PlayerPanelsManager playerPanelsManager;
  private final RootPanelComponent rootPanelComponent;
  private final GameBoardPanelComponent gameBoardPanelComponent;

  public PanelsManager(GUIQuoridorGameEngine gameEngine, DialogManager dialogManager) {
    this.gameEngine = gameEngine;
    this.gameBoardPanelComponent = new GameBoardPanelComponent(gameEngine);
    this.playerPanelsManager = new PlayerPanelsManager(gameEngine, dialogManager);
    this.rootPanelComponent = new RootPanelComponent(gameEngine, gameBoardPanelComponent, playerPanelsManager);
  }

  public JPanel createRootPanel(boolean showControlsForPlayer1) {
    JPanel rootPanel = rootPanelComponent.createPanel();
    updatePlayerPanel(gameEngine.getPlayingPawnIndex(), gameEngine.getPlayingPawnIndex());
    if (showControlsForPlayer1) {
      displayActionsPanelForPlayingPlayer(gameEngine.getPlayingPawnIndex());
    }
    return rootPanel;
  }

  public void updatePlayerPanel(int playerIndex, int oldPlayerIndex) {
    playerPanelsManager.updateActivePlayer(playerIndex, oldPlayerIndex);
  }

  public void displayActionsPanelForPlayingPlayer(int playerIndex) {
    playerPanelsManager.getActionsPanelComponent().displayActionsPanelForPlayingPlayer(playerIndex);
  }

  public void displayWallDirectionButtons(int playerIndex) {
    playerPanelsManager.getWallDirectionsPanelComponent().displayWallDirectionButtons(playerIndex);
  }

  public void updateWallLabel(int playerIndex, int remainingWalls) {
    playerPanelsManager.updateWallLabel(playerIndex, remainingWalls);
  }

  public void highlightValidMoves() {
    gameBoardPanelComponent.highlightValidMoves();
  }

  public void clearHighlights() {
    gameBoardPanelComponent.clearHighlights();
  }

  public void updatePawnPosition(Position oldPosition, Position targetPosition, int pawnIndex) {
    gameBoardPanelComponent.updatePawnPosition(oldPosition, targetPosition, pawnIndex);
    gameBoardPanelComponent.clearHighlights();
  }

  public void updateWallVisualization(Position position, WallOrientation orientation) {
    gameBoardPanelComponent.updateWallVisualization(position, orientation);
  }
}