package it.units.sdm.quoridor.GUI.managers;

import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.GUI.panels.*;
import it.units.sdm.quoridor.utils.Position;

import javax.swing.*;
import java.awt.*;

public class PanelsManager {
  private final GameGUIManager gameManager;
  private final int numberOfPlayers;
  private final GameBoardGUI gameBoardGUI;

  private final PlayersManager playersManager;
  private final ActionsPanelComponent actionsPanelComponent;
  private final WallDirectionsPanelComponent wallDirectionsPanelComponent;
  private final HelpQuitPanelComponent helpQuitPanelComponent;
  private final RootPanelComponent rootPanelComponent;

  private JPanel currentActionPanel;
  private int currentPlayerIndex;

  public PanelsManager(GameGUIManager gameManager, int numberOfPlayers, DialogManager dialogManager, JFrame mainFrame) {
    this.gameManager = gameManager;
    this.numberOfPlayers = numberOfPlayers;

    Position[] pawnPositions = createInitialPawnPositions();
    this.gameBoardGUI = new GameBoardGUI(gameManager, dialogManager, pawnPositions);

    this.playersManager = new PlayersManager(numberOfPlayers, gameBoardGUI);
    this.actionsPanelComponent = new ActionsPanelComponent(gameManager, gameBoardGUI, dialogManager, this, mainFrame);
    this.wallDirectionsPanelComponent = new WallDirectionsPanelComponent(gameManager, gameBoardGUI, this);
    this.helpQuitPanelComponent = new HelpQuitPanelComponent(dialogManager);

    this.rootPanelComponent = new RootPanelComponent(gameManager, gameBoardGUI, playersManager);
  }

  public JPanel createRootPanel() {
    JPanel rootPanel = rootPanelComponent.createPanel();
    updatePlayerPanel(gameManager.getPlayingPawnIndex());
    showActionButtonsForPlayer(gameManager.getPlayingPawnIndex());
    return rootPanel;
  }

  public GameBoardGUI getGameBoardPanel() {
    return gameBoardGUI;
  }

  public void showActionButtonsForPlayer(int playerIndex) {
    if (currentActionPanel != null) {
      removeCurrentActionPanel(currentPlayerIndex);
    }

    currentPlayerIndex = playerIndex;
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
    if (currentActionPanel != null) {
      playersManager.removeActionPanel(playerIndex, currentActionPanel);
    }
  }

  public void addActionButtonsPanel(int playerIndex, JPanel actionButtonsPanel, JPanel containerPanel) {
    containerPanel.add(actionButtonsPanel);
    containerPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));

    JPanel helpQuitPanel = helpQuitPanelComponent.createPanel();
    containerPanel.add(helpQuitPanel);

    playersManager.addActionPanel(playerIndex, containerPanel);
    currentActionPanel = containerPanel;
    currentPlayerIndex = playerIndex;
  }

  public void updateWallLabel(int playerIndex, int remainingWalls) {
    playersManager.updateWallLabel(playerIndex, remainingWalls);
  }

  public void removeCurrentActionPanel(int playerIndex) {
    if (currentActionPanel != null) {
      playersManager.removeActionPanel(playerIndex, currentActionPanel);
      currentActionPanel = null;
    }
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

  public void updatePlayerPanel(int playerIndex) {
    playersManager.updateActivePlayer(playerIndex);
    currentPlayerIndex = playerIndex;
  }
}