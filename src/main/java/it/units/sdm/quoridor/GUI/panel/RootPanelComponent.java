package it.units.sdm.quoridor.GUI.panel;

import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.GUI.GameBoardGUILayoutManager;

import javax.swing.*;
import java.awt.*;

public class RootPanelComponent implements PanelComponent {
  private final GameBoardGUI gameBoardGUI;
  private final PlayerPanelsComponent playerPanelsComponent;

  public RootPanelComponent(GameBoardGUI gameBoardGUI, PlayerPanelsComponent playerPanelsComponent) {
    this.gameBoardGUI = gameBoardGUI;
    this.playerPanelsComponent = playerPanelsComponent;
  }

  @Override
  public JPanel createPanel() {
    JPanel rootPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridy = 0;
    gbc.weighty = 1;

    JPanel leftPanel = new JPanel(new GridBagLayout());
    leftPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    gbc.gridx = 0;
    gbc.weightx = 3.5;
    rootPanel.add(leftPanel, gbc);

    JPanel centerWrapper = new JPanel(new GameBoardGUILayoutManager());
    centerWrapper.setBackground(GUIConstants.BACKGROUND_COLOR);
    gbc.gridx = 1;
    gbc.weightx = 9;
    rootPanel.add(centerWrapper, gbc);
    centerWrapper.add(gameBoardGUI);

    JPanel rightPanel = new JPanel(new GridBagLayout());
    rightPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    gbc.gridx = 2;
    gbc.weightx = 3.5;
    rootPanel.add(rightPanel, gbc);

    playerPanelsComponent.configureSidePanels(leftPanel, rightPanel);

    return rootPanel;
  }
}