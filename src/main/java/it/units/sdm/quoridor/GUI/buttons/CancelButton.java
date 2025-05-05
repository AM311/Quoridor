package it.units.sdm.quoridor.GUI.buttons;

import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.GUI.GameController;
import it.units.sdm.quoridor.GUI.panels.PanelsManager;

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
