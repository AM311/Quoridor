package it.units.sdm.quoridor.view.gui.panels;

import it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.utils.GUIConstants;
import it.units.sdm.quoridor.view.gui.managers.GameBoardPanelLayoutManager;
import it.units.sdm.quoridor.view.gui.managers.PlayerPanelsManager;

import javax.swing.*;
import java.awt.*;

public class RootPanelComponent implements PanelComponent {
  private final GUIQuoridorGameEngine gameEngine;
  private final GameBoardPanelComponent gameBoardPanelComponent;
  private final PlayerPanelsManager playerPanelsManager;

  public RootPanelComponent(GUIQuoridorGameEngine gameEngine, GameBoardPanelComponent gameBoardPanelComponent, PlayerPanelsManager playerPanelsManager) {
    this.gameBoardPanelComponent = gameBoardPanelComponent;
    this.playerPanelsManager = playerPanelsManager;
    this.gameEngine = gameEngine;
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

    JPanel centerWrapper = new JPanel(new GameBoardPanelLayoutManager());
    centerWrapper.setBackground(GUIConstants.BACKGROUND_COLOR);
    gbc.gridx = 1;
    gbc.weightx = 9;
    rootPanel.add(centerWrapper, gbc);
    centerWrapper.add(gameBoardPanelComponent.createPanel());

    JPanel rightPanel = new JPanel(new GridBagLayout());
    rightPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    gbc.gridx = 2;
    gbc.weightx = 3.5;
    rootPanel.add(rightPanel, gbc);

    playerPanelsManager.displayPlayerPanels(leftPanel, rightPanel, gameEngine);

    return rootPanel;
  }
}