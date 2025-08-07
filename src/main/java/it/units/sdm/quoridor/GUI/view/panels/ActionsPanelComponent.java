package it.units.sdm.quoridor.GUI.view.panels;

import it.units.sdm.quoridor.GUI.view.managers.DialogManager;
import it.units.sdm.quoridor.cli.engine.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.utils.GUIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ActionsPanelComponent implements PanelComponent {
  private final GUIQuoridorGameEngine gameEngine;
  private JPanel currentActionPanel;
  private int currentPlayerIndex;
  private final DialogManager dialogManager;
  private final List<PlayerPanelComponent> playerPanelComponents;


  public ActionsPanelComponent(GUIQuoridorGameEngine gameEngine, DialogManager dialogManager, List<PlayerPanelComponent> playerPanelComponents) {
    this.gameEngine = gameEngine;
    this.dialogManager = dialogManager;
    this.playerPanelComponents = playerPanelComponents;
  }

  @Override
  public JPanel createPanel() {
    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    actionsPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton moveButton = new JButton("Move");
    moveButton.addActionListener(e -> {
      moveButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      gameEngine.setMoveAction();
    });

    JButton placeWallButton = new JButton("Place Wall");
    placeWallButton.addActionListener(e -> {
      moveButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      gameEngine.setPlaceWallAction();
    });

    actionsPanel.add(moveButton);
    actionsPanel.add(placeWallButton);
    return actionsPanel;
  }

  public void removeActionPanel() {
    if (currentActionPanel != null) {                       //todo DA MONITORARE
      removeCurrentActionPanel(currentPlayerIndex);
    }
  }

  public void displayActionsPanelForPlayingPlayer(int playerIndex) {
    //todo QUI GIACEVA removeActionPanel

    currentPlayerIndex = playerIndex;
    currentActionPanel = new JPanel();
    currentActionPanel.setLayout(new BoxLayout(currentActionPanel, BoxLayout.Y_AXIS));
    currentActionPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    currentActionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel actionButtonsPanel = createPanel();
    addActionButtonsPanelForPlayingPlayer(playerIndex, actionButtonsPanel, currentActionPanel);
  }

  public void addActionButtonsPanelForPlayingPlayer(int playerIndex, JPanel actionButtonsPanel, JPanel containerPanel) {
    containerPanel.add(actionButtonsPanel);
    containerPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));

    JPanel helpQuitPanel = new HelpQuitPanelComponent(dialogManager).createPanel();
    containerPanel.add(helpQuitPanel);

    addActionPanel(playerIndex, containerPanel);
    currentActionPanel = containerPanel;
    currentPlayerIndex = playerIndex;
  }

  public void removeCurrentActionPanel(int playerIndex) {
    if (currentActionPanel != null) {
      removeActionPanel(playerIndex, currentActionPanel);
      currentActionPanel = null;
    }
  }

  public void addActionPanel(int playerIndex, JPanel actionPanel) {
    for (PlayerPanelComponent playerPanelComponent : playerPanelComponents) {
      if (playerPanelComponent.getPlayerIndex() == playerIndex) {
        playerPanelComponent.addActionPanel(actionPanel);
        break;
      }
    }
  }

  public void removeActionPanel(int playerIndex, JPanel actionPanel) {
    for (PlayerPanelComponent playerPanelComponent : playerPanelComponents) {
      if (playerPanelComponent.getPlayerIndex() == playerIndex) {
        playerPanelComponent.removeActionPanel(actionPanel);
        break;
      }
    }
  }
}