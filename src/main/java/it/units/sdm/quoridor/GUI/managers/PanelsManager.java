package it.units.sdm.quoridor.GUI.managers;

import it.units.sdm.quoridor.GUI.panels.GameBoardPanelComponent;
import it.units.sdm.quoridor.GUI.GameController;
import it.units.sdm.quoridor.GUI.panels.*;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import javax.swing.*;

public class PanelsManager {

  private final GameController gameController;
  private final PlayerPanelsManager playerPanelsManager;
  private final RootPanelComponent rootPanelComponent;
  private final GameBoardPanelComponent gameBoardPanelComponent;

  private JPanel currentActionPanel;

  public PanelsManager(GameController gameController, DialogManager dialogManager) {
    this.gameController = gameController;
    this.gameBoardPanelComponent = new GameBoardPanelComponent(gameController);
    this.playerPanelsManager = new PlayerPanelsManager(gameController, dialogManager);
    this.rootPanelComponent = new RootPanelComponent(gameController, gameBoardPanelComponent, playerPanelsManager);
  }

  public JPanel createRootPanel() {
    JPanel rootPanel = rootPanelComponent.createPanel();
    updatePlayerPanel(gameController.getPlayingPawnIndex());
    displayActionsPanelForPlayingPlayer(gameController.getPlayingPawnIndex());
    return rootPanel;
  }

  public GameBoardPanelComponent getGameBoardPanel() {
    return gameBoardPanelComponent;
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

  public void removeCurrentActionPanel(int playerIndex) {
    if (currentActionPanel != null) {
      playerPanelsManager.removeActionPanel(playerIndex, currentActionPanel);
      currentActionPanel = null;
    }
  }

  public void updatePlayerPanel(int playerIndex) {
    playerPanelsManager.updateActivePlayer(playerIndex);
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