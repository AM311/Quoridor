package it.units.sdm.quoridor.GUI.view;

import it.units.sdm.quoridor.GUI.view.managers.DialogManager;
import it.units.sdm.quoridor.GUI.view.managers.PanelsManager;
import it.units.sdm.quoridor.cli.engine.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import javax.swing.*;

public class GameView implements GameEventListener {
  private final GUIQuoridorGameEngine gameEngine;
  private PanelsManager panelsManager;
  private DialogManager dialogManager;

  public GameView(GUIQuoridorGameEngine gameEngine) {
    this.gameEngine = gameEngine;
    gameEngine.setEventListener(this);
  }

  public void displayGUI(boolean showControlsForPlayer1) {
    JFrame mainFrame = new JFrame("Quoridor");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    dialogManager = new DialogManager(mainFrame, gameEngine);
    panelsManager = new PanelsManager(gameEngine, dialogManager);

    JPanel rootPanel = panelsManager.createRootPanel(showControlsForPlayer1);
    mainFrame.setContentPane(rootPanel);
    mainFrame.setVisible(true);

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
  public void highlightValidMoves() {
    panelsManager.highlightValidMoves();
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
  public void onGameFinished() {
    dialogManager.displayGameFinishedDialog();
  }

  @Override
  public void displayCommandsForCurrentPlayer() {
    panelsManager.displayActionsPanelForPlayingPlayer(gameEngine.getPlayingPawnIndex());
  }

  @Override
  public void onRoundFinished(boolean showControls) {           //todo CHECK
    //panelsManager.removeCurrentActionPanel(gameEngine.getPlayingPawnIndex());
    int oldPlayerIndex = gameEngine.getPlayingPawnIndex();
    gameEngine.changeRound();
    panelsManager.updatePlayerPanel(gameEngine.getPlayingPawnIndex(), oldPlayerIndex);

    if (showControls) {
      displayCommandsForCurrentPlayer();
    }

    displayNotification("Player " + (gameEngine.getPlayingPawnIndex() + 1) + "'s round", false);
  }
}