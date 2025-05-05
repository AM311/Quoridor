package it.units.sdm.quoridor.gui.panels;

import it.units.sdm.quoridor.gui.DialogManager;
import it.units.sdm.quoridor.gui.GUIConstants;
import it.units.sdm.quoridor.gui.GameBoardGUI;
import it.units.sdm.quoridor.gui.GameController;
import it.units.sdm.quoridor.gui.buttons.MoveButton;
import it.units.sdm.quoridor.gui.buttons.PlaceWallButton;

import javax.swing.*;
import java.awt.*;

public class ActionsPanelComponent implements PanelComponent {
  private final GameController controller;
  private final GameBoardGUI gameBoardGUI;
  private final DialogManager dialogManager;
  private final PanelsManager panelsManager;

  public ActionsPanelComponent(GameController controller, GameBoardGUI gameBoardGUI,
                               DialogManager dialogManager, PanelsManager panelsManager) {
    this.controller = controller;
    this.gameBoardGUI = gameBoardGUI;
    this.dialogManager = dialogManager;
    this.panelsManager = panelsManager;
  }

  @Override
  public JPanel createPanel() {
    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    actionsPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton moveButton = new MoveButton(gameBoardGUI, dialogManager);
    JButton placeWallButton = new PlaceWallButton(gameBoardGUI, moveButton, controller, dialogManager, panelsManager);

    actionsPanel.add(moveButton);
    actionsPanel.add(placeWallButton);
    return actionsPanel;
  }

}