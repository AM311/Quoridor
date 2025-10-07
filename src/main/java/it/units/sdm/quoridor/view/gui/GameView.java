package it.units.sdm.quoridor.view.gui;

import it.units.sdm.quoridor.controller.engine.gui.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;
import it.units.sdm.quoridor.view.gui.managers.DialogManager;
import it.units.sdm.quoridor.view.gui.managers.PanelsManager;

import javax.swing.*;

public class GameView implements GameEventListener {
  private final GUIQuoridorGameEngine gameEngine;
  private final JFrame mainFrame;
  private PanelsManager panelsManager;
  private DialogManager dialogManager;

  public GameView(GUIQuoridorGameEngine gameEngine) {
    this.gameEngine = gameEngine;
    gameEngine.setEventListener(this);

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

  @Override
  public void onWallPlaced(Position position, WallOrientation orientation, int playerIndex, int remainingWalls) {
    panelsManager.updateWallVisualization(position, orientation);
    panelsManager.updateWallLabel(playerIndex, remainingWalls);
  }

  @Override
  public void displayNotification(String message, boolean isError) {
    dialogManager.displayNotificationDialog(message, isError);
  }

  @Override
  public void displayQuitRestartDialog() {
    dialogManager.displayQuitRestartDialog();
  }

  @Override
  public void displayCommandsForCurrentPlayer() {
    panelsManager.displayActionsPanelForPlayingPlayer(gameEngine.getPlayingPawnIndex());
  }

  @Override
  public void highlightValidMoves() {
    panelsManager.highlightValidMoves();
  }

  @Override
  public void disposeMainFrame() {
    mainFrame.dispose();
  }

  @Override
  public void onPawnMoved(Position oldPosition, Position newPosition, int playerIndex) {
    panelsManager.updatePawnPosition(oldPosition, newPosition, playerIndex);
  }

  @Override
  public void clearHighlights() {
    panelsManager.clearHighlights();
  }

  @Override
  public void displayWallDirectionButtons(int playerIndex) {
    panelsManager.displayWallDirectionButtons(playerIndex);
  }

  @Override
  public void displayStatistics() {
    dialogManager.displayStatisticsDialog();
  }

  @Override
  public void onRoundFinished(boolean showControls) {
    int oldPlayerIndex = gameEngine.getPlayingPawnIndex();
    gameEngine.changeRound();
    panelsManager.updatePlayerPanel(gameEngine.getPlayingPawnIndex(), oldPlayerIndex);

    if (showControls) {
      displayCommandsForCurrentPlayer();
    }

    displayNotification("Player " + (gameEngine.getPlayingPawnIndex() + 1) + "'s round", false);
  }

  @Override
  public void onGameFinished() {
    panelsManager.disposeActionsPanelForPlayingPlayer(gameEngine.getPlayingPawnIndex());
    displayStatistics();
    displayQuitRestartDialog();
  }
}