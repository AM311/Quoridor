package it.units.sdm.quoridor.view.gui.panels;

import it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.utils.GUIConstants;

import javax.swing.*;
import java.awt.*;

import static it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine.GUIAction;

public class WallDirectionsPanelComponent implements PanelComponent {
  private final GUIQuoridorGameEngine gameEngine;
  private final ActionsPanelComponent actionsPanelComponent;

  public WallDirectionsPanelComponent(GUIQuoridorGameEngine gameEngine, ActionsPanelComponent actionsPanelComponent) {
    this.gameEngine = gameEngine;
    this.actionsPanelComponent = actionsPanelComponent;
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
      gameEngine.setCurrentAction(GUIAction.PLACE_VERTICAL_WALL);
    });

    horizontalButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      horizontalButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      gameEngine.setCurrentAction(GUIAction.PLACE_HORIZONTAL_WALL);
    });


    JButton cancelButton = new JButton("X");
    cancelButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      horizontalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      gameEngine.setCurrentAction(GUIAction.DO_NOTHING);
      actionsPanelComponent.displayActionsPanelForPlayingPlayer(gameEngine.getPlayingPawnIndex());
    });

    directionsPanel.add(verticalButton);
    directionsPanel.add(horizontalButton);
    directionsPanel.add(cancelButton);

    return directionsPanel;
  }

  public void displayWallDirectionButtons(int playerIndex) {
    actionsPanelComponent.removeCurrentActionPanel(playerIndex);

    JPanel wallDirectionPanel = new JPanel();
    wallDirectionPanel.setLayout(new BoxLayout(wallDirectionPanel, BoxLayout.Y_AXIS));
    wallDirectionPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    wallDirectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel directionButtonsPanel = createPanel();

    actionsPanelComponent.addActionButtonsPanelForPlayingPlayer(playerIndex, directionButtonsPanel, wallDirectionPanel);
  }
}