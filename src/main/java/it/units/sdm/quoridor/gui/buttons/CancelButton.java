package it.units.sdm.quoridor.gui.buttons;

import it.units.sdm.quoridor.gui.GUIConstants;
import it.units.sdm.quoridor.gui.GameBoardGUI;
import it.units.sdm.quoridor.gui.GameController;
import it.units.sdm.quoridor.gui.panels.PanelsManager;

import javax.swing.*;

public class CancelButton extends JButton {
  private final JButton verticalButton;
  private final JButton horizontalButton;
  private final GameBoardGUI gameBoardGUI;
  private final PanelsManager panelsManager;
  private final GameController controller;


  public CancelButton(JButton verticalButton, JButton horizontalButton, GameBoardGUI gameBoardGUI, PanelsManager panelsManager, GameController controller) {
    this.verticalButton = verticalButton;
    this.horizontalButton = horizontalButton;
    this.gameBoardGUI = gameBoardGUI;
    this.panelsManager = panelsManager;
    this.controller = controller;
    this.setText("X");
    this.setActionListener();
  }

  public void setActionListener() {
    this.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      horizontalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.DO_NOTHING);
      panelsManager.showActionButtonsForPlayer(controller.getPlayingPawnIndex());
    });
  }
}
