package it.units.sdm.quoridor.gui;

import it.units.sdm.quoridor.gui.panels.PanelsManager;

import javax.swing.*;

public class GameGUI {
  private final int numberOfPlayers;
  private final GameController controller;
  private PanelsManager panelsManager;

  public GameGUI(int numberOfPlayers, GameController controller) {
    this.numberOfPlayers = numberOfPlayers;
    this.controller = controller;
  }


  public void showGUI() {
    JFrame mainFrame = new JFrame("Quoridor");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    DialogManager dialogManager = new DialogManager(mainFrame, numberOfPlayers, controller);

    panelsManager = new PanelsManager(controller, numberOfPlayers, dialogManager);

    JPanel rootPanel = panelsManager.createRootPanel();
    mainFrame.setContentPane(rootPanel);

    controller.connectComponents(this, panelsManager.getGameBoardPanel(), dialogManager);

    mainFrame.setVisible(true);

    dialogManager.showHelpDialog();
  }

  public void onWallPlaced(int playerIndex, int remainingWalls) {
    panelsManager.updateWallLabel(playerIndex, remainingWalls);
  }

  public void onTurnComplete() {
    panelsManager.removeCurrentActionPanel(controller.getPlayingPawnIndex());
    controller.changeRound();
    panelsManager.showActionButtonsForPlayer(controller.getPlayingPawnIndex());
  }
}