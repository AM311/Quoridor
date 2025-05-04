package it.units.sdm.quoridor.GUI.panel;

import it.units.sdm.quoridor.GUI.DialogManager;
import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.GUI.GameController;
import it.units.sdm.quoridor.GUI.button.MoveButton;
import it.units.sdm.quoridor.GUI.button.PlaceWallButton;

import javax.swing.*;
import java.awt.*;

public class ActionsPanelComponent implements PanelComponent {
  private final GameController controller;
  private final GameBoardGUI gameBoardGUI;
  private final DialogManager dialogManager;
  private final PanelsManager panelManager;

  public ActionsPanelComponent(GameController controller, GameBoardGUI gameBoardGUI,
                               DialogManager dialogManager, PanelsManager panelManager) {
    this.controller = controller;
    this.gameBoardGUI = gameBoardGUI;
    this.dialogManager = dialogManager;
    this.panelManager = panelManager;
  }

  @Override
  public JPanel createPanel() {
    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    actionsPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton moveButton = new MoveButton(gameBoardGUI, dialogManager);
    JButton placeWallButton = new PlaceWallButton(gameBoardGUI, moveButton, controller, dialogManager, panelManager);

    actionsPanel.add(moveButton);
    actionsPanel.add(placeWallButton);
    return actionsPanel;
  }

}