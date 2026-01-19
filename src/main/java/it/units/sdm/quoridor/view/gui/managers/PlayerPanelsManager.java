package it.units.sdm.quoridor.view.gui.managers;

import it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.model.abstracts.AbstractPawn;
import it.units.sdm.quoridor.view.gui.panels.ActionsPanelComponent;
import it.units.sdm.quoridor.view.gui.panels.PlayerPanelComponent;
import it.units.sdm.quoridor.view.gui.panels.WallDirectionsPanelComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static it.units.sdm.quoridor.controller.engine.abstracts.GUIQuoridorGameEngine.GUIAction;

public class PlayerPanelsManager {
  private final List<PlayerPanelComponent> playerPanelComponents;
  private final ActionsPanelComponent actionsPanelComponent;
  private final WallDirectionsPanelComponent wallDirectionsPanelComponent;

  public PlayerPanelsManager(GUIQuoridorGameEngine gameEngine, DialogManager dialogManager) {
    this.playerPanelComponents = new ArrayList<>(gameEngine.getNumberOfPlayers());

    this.actionsPanelComponent = new ActionsPanelComponent(
            dialogManager,
            playerPanelComponents,
            gameEngine::setMoveAction,
            gameEngine::setPlaceWallAction
    );

    this.wallDirectionsPanelComponent = new WallDirectionsPanelComponent(
            actionsPanelComponent,
            () -> gameEngine.setCurrentAction(GUIAction.PLACE_VERTICAL_WALL),
            () -> gameEngine.setCurrentAction(GUIAction.PLACE_HORIZONTAL_WALL),
            () -> gameEngine.setCurrentAction(GUIAction.DO_NOTHING)
    );
  }

  public void displayPlayerPanels(JPanel leftPanel, JPanel rightPanel, GUIQuoridorGameEngine gameEngine) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.NORTH;

    AbstractPawn[] pawns = gameEngine.getPawns().toArray(new AbstractPawn[0]);
    int numPlayers = gameEngine.getNumberOfPlayers();

    for (int i = 0; i < numPlayers; i++) {
      AbstractPawn pawn = pawns[i];
      PlayerPanelComponent playerPanel = new PlayerPanelComponent(
              "Player " + (i + 1),
              pawn.getNumberOfWalls(),
              pawn.getPawnAppearance().color(),
              i
      );
      playerPanelComponents.add(playerPanel);
    }

    gbc.gridy = 0;
    gbc.weighty = 0.2;
    leftPanel.add(playerPanelComponents.get(0).createPanel(), gbc);
    rightPanel.add(playerPanelComponents.get(1).createPanel(), gbc);

    if (numPlayers == 4) {
      gbc.gridy = 1;
      gbc.weighty = 0.4;
      leftPanel.add(Box.createVerticalStrut(1), gbc);
      rightPanel.add(Box.createVerticalStrut(1), gbc);

      gbc.gridy = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      leftPanel.add(playerPanelComponents.get(2).createPanel(), gbc);
      rightPanel.add(playerPanelComponents.get(3).createPanel(), gbc);
    }
  }

  public void updateWallLabel(int playerIndex, int remainingWalls) {
    playerPanelComponents.stream()
            .filter(p -> p.getPlayerIndex() == playerIndex)
            .findFirst()
            .ifPresent(p -> p.updateWallLabel(remainingWalls));
  }

  public void updateActivePlayer(int activePlayerIndex, int oldPlayerIndex) {
    actionsPanelComponent.removeCurrentActionPanel(oldPlayerIndex);
    playerPanelComponents.forEach(p -> p.setActive(p.getPlayerIndex() == activePlayerIndex));
  }

  public ActionsPanelComponent getActionsPanelComponent() {
    return actionsPanelComponent;
  }

  public WallDirectionsPanelComponent getWallDirectionsPanelComponent() {
    return wallDirectionsPanelComponent;
  }
}
