package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.GUI.managers.DialogManager;
import it.units.sdm.quoridor.GUI.managers.GameGUIManager;
import it.units.sdm.quoridor.GUI.managers.PanelsManager;

import javax.swing.*;

public class GameGUI {
  private final int numberOfPlayers;
  private final GameGUIManager gameManager;
  private PanelsManager panelsManager;
  private DialogManager dialogManager;

  public GameGUI(int numberOfPlayers, GameGUIManager gameManager) {
    this.numberOfPlayers = numberOfPlayers;
    this.gameManager = gameManager;
  }

  public void showGUI() {
    JFrame mainFrame = new JFrame("Quoridor");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    dialogManager = new DialogManager(mainFrame, gameManager);
    panelsManager = new PanelsManager(gameManager, numberOfPlayers, dialogManager, mainFrame);

    JPanel rootPanel = panelsManager.createRootPanel();
    mainFrame.setContentPane(rootPanel);

    gameManager.connectComponents(this, panelsManager.getGameBoardPanel(), dialogManager);

    mainFrame.setVisible(true);

    dialogManager.showHelpDialog();
    dialogManager.showNotificationDialog("Player " + (gameManager.getPlayingPawnIndex() + 1) + "'s turn", gameManager.getPlayingPawnIndex(), 1500);
  }

  public void onWallPlaced(int playerIndex, int remainingWalls) {
    panelsManager.updateWallLabel(playerIndex, remainingWalls);
  }

  public void onTurnComplete() {
    panelsManager.removeCurrentActionPanel(gameManager.getPlayingPawnIndex());
    gameManager.changeRound();
    panelsManager.updatePlayerPanel(gameManager.getPlayingPawnIndex());
    panelsManager.showActionButtonsForPlayer(gameManager.getPlayingPawnIndex());
    dialogManager.showNotificationDialog("Player " + (gameManager.getPlayingPawnIndex() + 1) + "'s turn", gameManager.getPlayingPawnIndex(), 1500);

  }
}