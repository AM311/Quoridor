package it.units.sdm.quoridor.GUI.panel;

import it.units.sdm.quoridor.GUI.DialogManager;
import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.GUI.GameController;

import javax.swing.*;
import java.awt.*;

public class ActionsPanelComponent implements PanelComponent {
  private final GameController controller;
  private final GameBoardGUI gameBoardGUI;
  private final DialogManager dialogManager;
  private final PanelsManager panelManager;

  public ActionsPanelComponent(GameController controller, GameBoardGUI gameBoardGUI,
                               DialogManager dialogManager, PanelsManager panelManager) {
    this.controller = controller;
    this.gameBoardGUI = gameBoardGUI;
    this.dialogManager = dialogManager;
    this.panelManager = panelManager;
  }

  @Override
  public JPanel createPanel() {
    JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    actionButtonsPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton moveButton = new JButton("Move");
    moveButton.addActionListener(e -> {
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.MOVE);
      moveButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      try {
        gameBoardGUI.highlightValidMoves();
      } catch (Exception ex) {
        dialogManager.showErrorDialog("Error highlighting moves: " + ex.getMessage());
      }
    });

    JButton placeWallButton = getPlaceWallButton(controller.getPlayingPawnIndex(), moveButton);

    actionButtonsPanel.add(moveButton);
    actionButtonsPanel.add(placeWallButton);
    return actionButtonsPanel;
  }

  private JButton getPlaceWallButton(int playerIndex, JButton moveButton) {
    JButton placeWallButton = new JButton("Place Wall");
    placeWallButton.addActionListener(e -> {
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.DO_NOTHING);
      moveButton.setBackground(UIManager.getColor("Button.background"));
      gameBoardGUI.clearHighlights();
      if (controller.getGame().getPlayingPawn().getNumberOfWalls() > 0) {
        panelManager.showWallDirectionButtons(playerIndex);
      } else {
        dialogManager.showNotificationDialog("No walls available!", playerIndex);
      }
    });
    return placeWallButton;
  }
}