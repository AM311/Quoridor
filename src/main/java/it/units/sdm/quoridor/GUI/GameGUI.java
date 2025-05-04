package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.GUI.panel.PanelsManager;


import javax.swing.*;

public class GameGUI {
  private final int numberOfPlayers;
  private final GameController controller;
  private PanelsManager panelManager;

  public GameGUI(int numberOfPlayers, GameController controller) {
    this.numberOfPlayers = numberOfPlayers;
    this.controller = controller;
  }


  public void showGUI() {
    JFrame mainFrame = new JFrame("Quoridor");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    DialogManager dialogManager = new DialogManager(mainFrame, numberOfPlayers, controller);

    panelManager = new PanelsManager(controller, numberOfPlayers, dialogManager);

    JPanel rootPanel = panelManager.createRootPanel();
    mainFrame.setContentPane(rootPanel);

    controller.connectComponents(this, panelManager.getGameBoardPanel(), dialogManager);

    mainFrame.setVisible(true);

    dialogManager.showHelpDialog();
  }

  public void onWallPlaced(int playerIndex, int remainingWalls) {
    panelManager.updateWallLabel(playerIndex, remainingWalls);
  }

  public void onTurnComplete() {
    panelManager.removeCurrentActionPanel(controller.getPlayingPawnIndex());
    controller.changeRound();
    panelManager.showActionButtonsForPlayer(controller.getPlayingPawnIndex());
  }
}