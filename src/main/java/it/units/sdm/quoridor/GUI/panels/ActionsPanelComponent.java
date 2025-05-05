package it.units.sdm.quoridor.GUI.panels;

import it.units.sdm.quoridor.GUI.*;

import javax.swing.*;
import java.awt.*;

public class ActionsPanelComponent implements PanelComponent {
  private final GameController controller;
  private final GameBoardGUI gameBoardGUI;
  private final DialogManager dialogManager;
  private final PanelsManager panelsManager;
  private final JFrame mainFrame;

  public ActionsPanelComponent(GameController controller, GameBoardGUI gameBoardGUI,
                               DialogManager dialogManager, PanelsManager panelsManager, JFrame mainFrame) {
    this.controller = controller;
    this.gameBoardGUI = gameBoardGUI;
    this.dialogManager = dialogManager;
    this.panelsManager = panelsManager;
    this.mainFrame = mainFrame;
  }

  @Override
  public JPanel createPanel() {
    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    actionsPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton moveButton = new JButton("Move");
    moveButton.addActionListener(e -> {
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.MOVE);
      moveButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      try {
        gameBoardGUI.highlightValidMoves();
      } catch (Exception ex) {
        showErrorDialog("Error highlighting moves: " + ex.getMessage());
      }
    });

    JButton placeWallButton = getPlaceWallButton(moveButton);

    actionsPanel.add(moveButton);
    actionsPanel.add(placeWallButton);
    return actionsPanel;
  }

  private JButton getPlaceWallButton(JButton moveButton) {
    JButton placeWallButton = new JButton("Place Wall");
    placeWallButton.addActionListener(e -> {
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.DO_NOTHING);
      moveButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      gameBoardGUI.clearHighlights();
      if (controller.getGame().getPlayingPawn().getNumberOfWalls() > 0) {
        panelsManager.showWallDirectionButtons(controller.getPlayingPawnIndex());
      } else {
        dialogManager.showNotificationDialog("No walls available!", controller.getPlayingPawnIndex(), 3000);
      }
    });
    return placeWallButton;
  }

  public void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(mainFrame,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
  }

}