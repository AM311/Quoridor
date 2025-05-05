package it.units.sdm.quoridor.GUI.panels;

import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.GUI.GameController;
import it.units.sdm.quoridor.GUI.PanelsManager;

import javax.swing.*;
import java.awt.*;

public class WallDirectionsPanelComponent implements PanelComponent {
  private final GameController controller;
  private final GameBoardGUI gameBoardGUI;
  private final PanelsManager panelsManager;

  public WallDirectionsPanelComponent(GameController controller, GameBoardGUI gameBoardGUI,
                                      PanelsManager panelsManager) {
    this.controller = controller;
    this.gameBoardGUI = gameBoardGUI;
    this.panelsManager = panelsManager;
  }

  @Override
  public JPanel createPanel() {
    JPanel directionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    directionsPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton verticalButton = new JButton("Vertical");
    JButton horizontalButton = new JButton("Horizontal");

    verticalButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      horizontalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.PLACE_VERTICAL_WALL);
    });

    horizontalButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      horizontalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.PLACE_HORIZONTAL_WALL);
    });


    JButton cancelButton = new JButton("X");
    cancelButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      horizontalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.DO_NOTHING);
      panelsManager.showActionButtonsForPlayer(controller.getPlayingPawnIndex());
    });

    directionsPanel.add(verticalButton);
    directionsPanel.add(horizontalButton);
    directionsPanel.add(cancelButton);

    return directionsPanel;
  }


  public void showWallDirectionButtons(int playerIndex) {
    panelsManager.removeCurrentActionPanelForPlayer(playerIndex);

    JPanel wallDirectionPanel = new JPanel();
    wallDirectionPanel.setLayout(new BoxLayout(wallDirectionPanel, BoxLayout.Y_AXIS));
    wallDirectionPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    wallDirectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel directionButtonsPanel = createPanel();

    panelsManager.addActionButtonsPanel(playerIndex, directionButtonsPanel, wallDirectionPanel);
  }
}