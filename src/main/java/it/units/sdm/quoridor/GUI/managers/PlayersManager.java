package it.units.sdm.quoridor.GUI.managers;

import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.GUI.panels.PlayerPanelComponent;
import it.units.sdm.quoridor.model.AbstractPawn;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayersManager {
  private final int numberOfPlayers;
  private final String[] pawnColors;
  private final List<PlayerPanelComponent> playerComponents;

  public PlayersManager(int numberOfPlayers, GameBoardGUI gameBoardGUI) {
    this.numberOfPlayers = numberOfPlayers;
    this.pawnColors = gameBoardGUI.getPawnColors();
    this.playerComponents = new ArrayList<>(numberOfPlayers);
  }

  public void configureSidePanels(JPanel leftPanel, JPanel rightPanel, GameGUIManager gameManager) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.NORTH;

    AbstractPawn[] pawns = gameManager.getGame().getPawns().toArray(new AbstractPawn[0]);

    // Player 1 (left panel)
    PlayerPanelComponent player1 = new PlayerPanelComponent(
            "Player 1",
            pawns[0].getNumberOfWalls(),
            pawnColors[0],
            0);
    playerComponents.add(player1);

    // Player 2 (right panel)
    PlayerPanelComponent player2 = new PlayerPanelComponent(
            "Player 2",
            pawns[1].getNumberOfWalls(),
            pawnColors[1],
            1);
    playerComponents.add(player2);

    gbc.gridy = 0;
    gbc.weighty = 0.2;
    leftPanel.add(player1.createPanel(), gbc);
    rightPanel.add(player2.createPanel(), gbc);

    if (numberOfPlayers == 4) {
      // Add spacing
      gbc.gridy = 1;
      gbc.weighty = 0.4;
      leftPanel.add(Box.createVerticalStrut(1), gbc);
      rightPanel.add(Box.createVerticalStrut(1), gbc);

      // Player 3 (left panel)
      PlayerPanelComponent player3 = new PlayerPanelComponent(
              "Player 3",
              pawns[2].getNumberOfWalls(),
              pawnColors[2],
              2);
      playerComponents.add(player3);

      // Player 4 (right panel)
      PlayerPanelComponent player4 = new PlayerPanelComponent(
              "Player 4",
              pawns[3].getNumberOfWalls(),
              pawnColors[3],
              3);
      playerComponents.add(player4);

      gbc.gridy = 2;
      gbc.weighty = 0.4;
      gbc.anchor = GridBagConstraints.CENTER;
      leftPanel.add(player3.createPanel(), gbc);
      rightPanel.add(player4.createPanel(), gbc);
    }
  }

  public void updateWallLabel(int playerIndex, int remainingWalls) {
    for (PlayerPanelComponent component : playerComponents) {
      if (component.getPlayerIndex() == playerIndex) {
        component.updateWallLabel(remainingWalls);
        break;
      }
    }
  }

  public void updateActivePlayer(int activePlayerIndex) {
    for (PlayerPanelComponent component : playerComponents) {
      component.setActive(component.getPlayerIndex() == activePlayerIndex);
    }
  }

  public void addActionPanel(int playerIndex, JPanel actionPanel) {
    for (PlayerPanelComponent component : playerComponents) {
      if (component.getPlayerIndex() == playerIndex) {
        component.addActionPanel(actionPanel);
        break;
      }
    }
  }

  public void removeActionPanel(int playerIndex, JPanel actionPanel) {
    for (PlayerPanelComponent component : playerComponents) {
      if (component.getPlayerIndex() == playerIndex) {
        component.removeActionPanel(actionPanel);
        break;
      }
    }
  }
}