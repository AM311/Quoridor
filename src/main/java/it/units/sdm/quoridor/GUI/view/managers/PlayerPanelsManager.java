package it.units.sdm.quoridor.GUI.view.managers;

import it.units.sdm.quoridor.GUI.view.panels.ActionsPanelComponent;
import it.units.sdm.quoridor.GUI.view.panels.PlayerPanelComponent;
import it.units.sdm.quoridor.GUI.view.panels.WallDirectionsPanelComponent;
import it.units.sdm.quoridor.cli.engine.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.model.AbstractPawn;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerPanelsManager {
  private final int numberOfPlayers;
  private final String[] pawnColors;
  private final List<PlayerPanelComponent> playerPanelComponents;
  private final ActionsPanelComponent actionsPanelComponent;
  private final WallDirectionsPanelComponent wallDirectionsPanelComponent;


  public PlayerPanelsManager(GUIQuoridorGameEngine gameEngine, DialogManager dialogManager) {
    this.numberOfPlayers = gameEngine.getNumberOfPlayers();
    this.pawnColors = gameEngine.getPawns().stream()
            .map(pawn -> pawn.getPawnAppearance().color().toString())
            .toArray(String[]::new);
    this.playerPanelComponents = new ArrayList<>(this.numberOfPlayers);

    this.actionsPanelComponent = new ActionsPanelComponent(gameEngine, dialogManager, playerPanelComponents);
    this.wallDirectionsPanelComponent = new WallDirectionsPanelComponent(gameEngine, actionsPanelComponent);
  }

  public void displayPlayerPanels(JPanel leftPanel, JPanel rightPanel, GUIQuoridorGameEngine gameEngine) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.NORTH;

    AbstractPawn[] pawns = gameEngine.getPawns().toArray(new AbstractPawn[0]);

    PlayerPanelComponent player1 = new PlayerPanelComponent(
            "Player 1",
            pawns[0].getNumberOfWalls(),
            pawnColors[0],
            0);
    playerPanelComponents.add(player1);

    PlayerPanelComponent player2 = new PlayerPanelComponent(
            "Player 2",
            pawns[1].getNumberOfWalls(),
            pawnColors[1],
            1);
    playerPanelComponents.add(player2);

    gbc.gridy = 0;
    gbc.weighty = 0.2;
    leftPanel.add(player1.createPanel(), gbc);
    rightPanel.add(player2.createPanel(), gbc);

    if (numberOfPlayers == 4) {
      gbc.gridy = 1;
      gbc.weighty = 0.4;
      leftPanel.add(Box.createVerticalStrut(1), gbc);
      rightPanel.add(Box.createVerticalStrut(1), gbc);

      PlayerPanelComponent player3 = new PlayerPanelComponent(
              "Player 3",
              pawns[2].getNumberOfWalls(),
              pawnColors[2],
              2);
      playerPanelComponents.add(player3);

      PlayerPanelComponent player4 = new PlayerPanelComponent(
              "Player 4",
              pawns[3].getNumberOfWalls(),
              pawnColors[3],
              3);
      playerPanelComponents.add(player4);

      gbc.gridy = 2;
      gbc.weighty = 0.4;
      gbc.anchor = GridBagConstraints.CENTER;
      leftPanel.add(player3.createPanel(), gbc);
      rightPanel.add(player4.createPanel(), gbc);
    }
  }

  public void updateWallLabel(int playerIndex, int remainingWalls) {
    for (PlayerPanelComponent playerPanelComponent : playerPanelComponents) {
      if (playerPanelComponent.getPlayerIndex() == playerIndex) {
        playerPanelComponent.updateWallLabel(remainingWalls);
        break;
      }
    }
  }

  public void updateActivePlayer(int activePlayerIndex) {
    actionsPanelComponent.removeActionPanel();

    for (PlayerPanelComponent playerPanelComponent : playerPanelComponents) {
      playerPanelComponent.setActive(playerPanelComponent.getPlayerIndex() == activePlayerIndex);
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

  public ActionsPanelComponent getActionsPanelComponent() {
    return actionsPanelComponent;
  }

  public WallDirectionsPanelComponent getWallDirectionsPanelComponent() {
    return wallDirectionsPanelComponent;
  }
}