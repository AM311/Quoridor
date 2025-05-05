package it.units.sdm.quoridor.GUI.buttons;

import it.units.sdm.quoridor.GUI.DialogManager;
import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.GUI.GameController;
import it.units.sdm.quoridor.GUI.panels.PanelsManager;

import javax.swing.*;

public class PlaceWallButton extends JButton {
  private final GameBoardGUI gameBoardGUI;
  private final JButton moveButton;
  private final GameController controller;
  private final DialogManager dialogManager;
  private final PanelsManager panelsManager;

  public PlaceWallButton(GameBoardGUI gameBoardGUI, JButton moveButton, GameController controller, DialogManager dialogManager, PanelsManager panelsManager) {
    this.gameBoardGUI = gameBoardGUI;
    this.moveButton = moveButton;
    this.controller = controller;
    this.dialogManager = dialogManager;
    this.panelsManager = panelsManager;
    this.setText("Place Wall");
    this.setActionListener();
  }

  public void setActionListener() {
    this.addActionListener(e -> {
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.DO_NOTHING);
      moveButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      gameBoardGUI.clearHighlights();
      if (controller.getGame().getPlayingPawn().getNumberOfWalls() > 0) {
        panelsManager.showWallDirectionButtons(controller.getPlayingPawnIndex());
      } else {
        dialogManager.showNotificationDialog("No walls available!", controller.getPlayingPawnIndex());
      }
    });

  }
}
