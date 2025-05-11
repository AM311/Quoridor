package it.units.sdm.quoridor.GUI.view.panels;

import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.controller.GameController;

import javax.swing.*;
import java.awt.*;

public class WallDirectionsPanelComponent implements PanelComponent {
  private final GameController gameController;
  private final ActionsPanelComponent actionsPanelComponent;

  public WallDirectionsPanelComponent(GameController gameController, ActionsPanelComponent actionsPanelComponent) {
    this.gameController = gameController;
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
      gameController.setCurrentAction(GameController.Action.PLACE_VERTICAL_WALL);
    });

    horizontalButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      horizontalButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      gameController.setCurrentAction(GameController.Action.PLACE_HORIZONTAL_WALL);
    });


    JButton cancelButton = new JButton("X");
    cancelButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      horizontalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      gameController.setCurrentAction(GameController.Action.DO_NOTHING);
      actionsPanelComponent.displayActionsPanelForPlayingPlayer(gameController.getPlayingPawnIndex());
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