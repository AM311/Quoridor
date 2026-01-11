package it.units.sdm.quoridor.view.gui.panels;

import it.units.sdm.quoridor.utils.GUIConstants;

import javax.swing.*;
import java.awt.*;

public class WallDirectionsPanelComponent implements PanelComponent {
  private final ActionsPanelComponent actionsPanelComponent;

  private final Runnable onSelectVertical;
  private final Runnable onSelectHorizontal;
  private final Runnable onCancelAction;

  private int currentPlayerIndex = -1;

  public WallDirectionsPanelComponent(
          ActionsPanelComponent actionsPanelComponent,
          Runnable onSelectVertical,
          Runnable onSelectHorizontal,
          Runnable onCancelAction
  ) {
    this.actionsPanelComponent = actionsPanelComponent;
    this.onSelectVertical = onSelectVertical;
    this.onSelectHorizontal = onSelectHorizontal;
    this.onCancelAction = onCancelAction;
  }

  @Override
  public JPanel createPanel() {
    JButton verticalButton = new JButton("Vertical");
    JButton horizontalButton = new JButton("Horizontal");
    JButton cancelButton = new JButton("Back");

    cancelButton.setMargin(new Insets(2, 10, 2, 10));

    verticalButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      horizontalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      onSelectVertical.run();
    });

    horizontalButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      horizontalButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      onSelectHorizontal.run();
    });

    cancelButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      horizontalButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);

      onCancelAction.run();

      if (currentPlayerIndex >= 0) {
        actionsPanelComponent.displayActionsPanelForPlayingPlayer(currentPlayerIndex);
      }
    });

    JPanel directionRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
    directionRow.setOpaque(false);
    directionRow.add(verticalButton);
    directionRow.add(horizontalButton);

    JPanel cancelRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
    cancelRow.setOpaque(false);
    cancelRow.add(cancelButton);

    JPanel directionsPanel = new JPanel();
    directionsPanel.setLayout(new BoxLayout(directionsPanel, BoxLayout.Y_AXIS));
    directionsPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    directionsPanel.add(directionRow);
    directionsPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));
    directionsPanel.add(cancelRow);

    return directionsPanel;
  }


  public void displayWallDirectionButtons(int playerIndex) {
    this.currentPlayerIndex = playerIndex;

    actionsPanelComponent.removeCurrentActionPanel(playerIndex);

    JPanel wallDirectionPanel = new JPanel();
    wallDirectionPanel.setLayout(new BoxLayout(wallDirectionPanel, BoxLayout.Y_AXIS));
    wallDirectionPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    wallDirectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel directionButtonsPanel = createPanel();

    actionsPanelComponent.addActionButtonsPanelForPlayingPlayer(playerIndex, directionButtonsPanel, wallDirectionPanel);
  }
}
