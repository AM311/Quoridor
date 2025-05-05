package it.units.sdm.quoridor.gui.panels;

import it.units.sdm.quoridor.gui.DialogManager;
import it.units.sdm.quoridor.gui.GameBoardGUI;
import it.units.sdm.quoridor.gui.GameController;
import it.units.sdm.quoridor.gui.GUIConstants;
import it.units.sdm.quoridor.utils.Position;

import javax.swing.*;
import java.awt.*;

public class PanelsManager {
  private final GameController controller;
  private final int numberOfPlayers;
  private final GameBoardGUI gameBoardGUI;

  private final PlayerPanelsComponent playerPanelsComponent;
  private final ActionsPanelComponent actionsPanelComponent;
  private final WallDirectionsPanelComponent wallDirectionsPanelComponent;
  private final HelpQuitPanelComponent helpQuitPanelComponent;
  private final RootPanelComponent rootPanelComponent;

  private JPanel currentActionPanel;

  public PanelsManager(GameController controller, int numberOfPlayers, DialogManager dialogManager) {
    this.controller = controller;
    this.numberOfPlayers = numberOfPlayers;

    Position[] pawnPositions = createInitialPawnPositions();
    this.gameBoardGUI = new GameBoardGUI(controller, pawnPositions);

    this.playerPanelsComponent = new PlayerPanelsComponent(controller, numberOfPlayers, gameBoardGUI);
    this.actionsPanelComponent = new ActionsPanelComponent(controller, gameBoardGUI, dialogManager, this);
    this.wallDirectionsPanelComponent = new WallDirectionsPanelComponent(controller, gameBoardGUI, this);
    this.helpQuitPanelComponent = new HelpQuitPanelComponent(dialogManager);
    this.rootPanelComponent = new RootPanelComponent(gameBoardGUI, playerPanelsComponent);
  }

  public JPanel createRootPanel() {
    JPanel rootPanel = rootPanelComponent.createPanel();
    showActionButtonsForPlayer(controller.getPlayingPawnIndex());
    return rootPanel;
  }

  public GameBoardGUI getGameBoardPanel() {
    return gameBoardGUI;
  }

  public void showActionButtonsForPlayer(int playerIndex) {
    if (currentActionPanel != null) {
      playerPanelsComponent.removeActionPanel(playerIndex, currentActionPanel);
    }

    currentActionPanel = new JPanel();
    currentActionPanel.setLayout(new BoxLayout(currentActionPanel, BoxLayout.Y_AXIS));
    currentActionPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    currentActionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel actionButtonsPanel = actionsPanelComponent.createPanel();
    addActionButtonsPanel(playerIndex, actionButtonsPanel, currentActionPanel);
  }

  public void showWallDirectionButtons(int playerIndex) {
    wallDirectionsPanelComponent.showWallDirectionButtons(playerIndex);
  }

  public void removeCurrentActionPanelForPlayer(int playerIndex) {
    playerPanelsComponent.removeActionPanel(playerIndex, currentActionPanel);
  }

  public void addActionButtonsPanel(int playerIndex, JPanel actionButtonsPanel, JPanel containerPanel) {
    containerPanel.add(actionButtonsPanel);
    containerPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));

    JPanel helpQuitPanel = helpQuitPanelComponent.createPanel();
    containerPanel.add(helpQuitPanel);

    playerPanelsComponent.addActionPanel(playerIndex, containerPanel);
    currentActionPanel = containerPanel;
  }

  public void updateWallLabel(int playerIndex, int remainingWalls) {
    playerPanelsComponent.updateWallLabel(playerIndex, remainingWalls);
  }

  public void removeCurrentActionPanel(int playerIndex) {
    playerPanelsComponent.removeActionPanel(playerIndex, currentActionPanel);
  }

  private Position[] createInitialPawnPositions() {
    Position[] positions = new Position[numberOfPlayers];
    positions[0] = new Position(0, 4);
    positions[1] = new Position(8, 4);

    if (numberOfPlayers == 4) {
      positions[2] = new Position(4, 0);
      positions[3] = new Position(4, 8);
    }

    return positions;
  }
}