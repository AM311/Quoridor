package it.units.sdm.quoridor.GUI.panel;

import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.GUI.GameController;

import javax.swing.*;
import java.awt.*;

public class WallDirectionsPanelComponent implements PanelComponent {
  private final GameController controller;
  private final GameBoardGUI gameBoardGUI;
  private final PanelsManager panelManager;

  public WallDirectionsPanelComponent(GameController controller, GameBoardGUI gameBoardGUI,
                                      PanelsManager panelManager) {
    this.controller = controller;
    this.gameBoardGUI = gameBoardGUI;
    this.panelManager = panelManager;
  }

  @Override
  public JPanel createPanel() {
    JPanel directionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    directionButtonsPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton verticalButton = new JButton("Vertical");
    verticalButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.PLACE_VERTICAL_WALL);
    });

    JButton horizontalButton = new JButton("Horizontal");
    horizontalButton.addActionListener(e -> {
      verticalButton.setBackground(UIManager.getColor("Button.background"));
      horizontalButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.PLACE_HORIZONTAL_WALL);
    });

    JButton cancelButton = new JButton("X");
    cancelButton.addActionListener(e -> {
      verticalButton.setBackground(UIManager.getColor("Button.background"));
      horizontalButton.setBackground(UIManager.getColor("Button.background"));
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.DO_NOTHING);
      panelManager.showActionButtonsForPlayer(controller.getPlayingPawnIndex());
    });

    directionButtonsPanel.add(verticalButton);
    directionButtonsPanel.add(horizontalButton);
    directionButtonsPanel.add(cancelButton);
    return directionButtonsPanel;
  }


  public void showWallDirectionButtons(int playerIndex) {
    panelManager.removeCurrentActionPanelForPlayer(playerIndex);

    JPanel wallDirectionPanel = new JPanel();
    wallDirectionPanel.setLayout(new BoxLayout(wallDirectionPanel, BoxLayout.Y_AXIS));
    wallDirectionPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    wallDirectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel directionButtonsPanel = createPanel();

    panelManager.addActionButtonsPanel(playerIndex, directionButtonsPanel, wallDirectionPanel);
  }
}