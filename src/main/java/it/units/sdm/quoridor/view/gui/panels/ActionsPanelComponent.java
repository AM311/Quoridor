package it.units.sdm.quoridor.view.gui.panels;

import it.units.sdm.quoridor.utils.GUIConstants;
import it.units.sdm.quoridor.view.gui.managers.DialogManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ActionsPanelComponent implements PanelComponent {
  private JPanel currentActionPanel;
  private final DialogManager dialogManager;
  private final List<PlayerPanelComponent> playerPanelComponents;

  private final Runnable onMoveSelected;
  private final Runnable onPlaceWallSelected;

  public ActionsPanelComponent(
          DialogManager dialogManager,
          List<PlayerPanelComponent> playerPanelComponents,
          Runnable onMoveSelected,
          Runnable onPlaceWallSelected
  ) {
    this.dialogManager = dialogManager;
    this.playerPanelComponents = playerPanelComponents;
    this.onMoveSelected = onMoveSelected;
    this.onPlaceWallSelected = onPlaceWallSelected;
  }

  @Override
  public JPanel createPanel() {
    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    actionsPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton moveButton = new JButton("Move");
    JButton placeWallButton = new JButton("Place Wall");

    moveButton.addActionListener(e -> {
      setButtonState(moveButton, placeWallButton);
      onMoveSelected.run();
    });

    placeWallButton.addActionListener(e -> {
      setButtonState(placeWallButton, moveButton);
      onPlaceWallSelected.run();
    });

    actionsPanel.add(moveButton);
    actionsPanel.add(placeWallButton);
    return actionsPanel;
  }

  private void setButtonState(JButton selected, JButton unselected) {
    selected.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
    unselected.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
  }

  public void displayActionsPanelForPlayingPlayer(int playerIndex) {
    removeCurrentActionPanel(playerIndex);

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
