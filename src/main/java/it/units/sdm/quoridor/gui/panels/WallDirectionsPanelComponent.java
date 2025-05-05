package it.units.sdm.quoridor.gui.panels;

import it.units.sdm.quoridor.gui.GUIConstants;
import it.units.sdm.quoridor.gui.GameBoardGUI;
import it.units.sdm.quoridor.gui.GameController;
import it.units.sdm.quoridor.gui.buttons.CancelButton;
import it.units.sdm.quoridor.gui.buttons.OrientationButton;
import it.units.sdm.quoridor.utils.WallOrientation;

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

    OrientationButton verticalButton = new OrientationButton(gameBoardGUI, WallOrientation.VERTICAL);
    OrientationButton horizontalButton = new OrientationButton(gameBoardGUI, WallOrientation.HORIZONTAL);

    verticalButton.setActionListener(horizontalButton);
    horizontalButton.setActionListener(verticalButton);

    JButton cancelButton = new CancelButton(verticalButton, horizontalButton, gameBoardGUI, panelsManager, controller);

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