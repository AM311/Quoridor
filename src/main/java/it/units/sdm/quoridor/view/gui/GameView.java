package it.units.sdm.quoridor.view.gui;

import it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;
import it.units.sdm.quoridor.view.gui.managers.DialogManager;
import it.units.sdm.quoridor.view.gui.managers.PanelsManager;

import javax.swing.*;

public class GameView {
  private final GUIQuoridorGameEngine gameEngine;
  private final JFrame mainFrame;
  private PanelsManager panelsManager;
  private DialogManager dialogManager;

  public GameView(GUIQuoridorGameEngine gameEngine) {
    this.gameEngine = gameEngine;

    this.mainFrame = new JFrame("Quoridor");
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
  }

  public void displayGUI(boolean showControlsForPlayer1) {
    dialogManager = new DialogManager(mainFrame, gameEngine);
    panelsManager = new PanelsManager(gameEngine, dialogManager);

    JPanel rootPanel = panelsManager.createRootPanel(showControlsForPlayer1);
    mainFrame.setContentPane(rootPanel);

    if (!mainFrame.isVisible()) {
      mainFrame.setVisible(true);
    } else {
      mainFrame.revalidate();
      mainFrame.repaint();
    }

    dialogManager.displayHelpDialog();
    displayNotification("Player " + (gameEngine.getPlayingPawnIndex() + 1) + "'s round", false);
  }

  public void onWallPlaced(Position position, WallOrientation orientation, int playerIndex, int remainingWalls) {
    panelsManager.updateWallVisualization(position, orientation);
    panelsManager.updateWallLabel(playerIndex, remainingWalls);
  }

  public void displayNotification(String message, boolean isError) {
    dialogManager.displayNotificationDialog(message, isError);
  }

  public void displayQuitRestartDialog() {
    dialogManager.displayQuitRestartDialog();
  }

  public void displayCommandsForCurrentPlayer() {
    panelsManager.displayActionsPanelForPlayingPlayer(gameEngine.getPlayingPawnIndex());
  }

  public void highlightValidMoves() {
    panelsManager.highlightValidMoves();
  }

  public void disposeMainFrame() {
    mainFrame.dispose();
  }

  public void onPawnMoved(Position oldPosition, Position newPosition, int playerIndex) {
    panelsManager.updatePawnPosition(oldPosition, newPosition, playerIndex);
  }

  public void clearHighlights() {
    panelsManager.clearHighlights();
  }

  public void displayWallDirectionButtons(int playerIndex) {
    panelsManager.displayWallDirectionButtons(playerIndex);
  }

  public void displayStatistics() {
    dialogManager.displayStatisticsDialog();
  }

  public void onRoundFinished(boolean showControls) {
    int oldPlayerIndex = gameEngine.getPlayingPawnIndex();
    gameEngine.changeRound();
    panelsManager.updatePlayerPanel(gameEngine.getPlayingPawnIndex(), oldPlayerIndex);

    if (showControls) {
      displayCommandsForCurrentPlayer();
    }

    displayNotification("Player " + (gameEngine.getPlayingPawnIndex() + 1) + "'s round", false);
  }

  public void onGameFinished() {
    panelsManager.disposeActionsPanelForPlayingPlayer(gameEngine.getPlayingPawnIndex());
    displayStatistics();
  }
}