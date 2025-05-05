package it.units.sdm.quoridor.GUI.panels;

import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.GUI.managers.GameGUIManager;
import it.units.sdm.quoridor.model.AbstractPawn;

import javax.swing.*;
import java.awt.*;

public class PlayerPanelsComponent implements PanelComponent {
  private final int numberOfPlayers;
  private final GameBoardGUI gameBoardGUI;

  private final JPanel[] playerPanels;
  private final JLabel[] wallLabels;
  private final JLabel[] nameLabels;


  public PlayerPanelsComponent(int numberOfPlayers, GameBoardGUI gameBoardGUI) {
    this.numberOfPlayers = numberOfPlayers;
    this.gameBoardGUI = gameBoardGUI;
    this.playerPanels = new JPanel[numberOfPlayers];
    this.wallLabels = new JLabel[numberOfPlayers];
    this.nameLabels = new JLabel[numberOfPlayers]; // Array per le etichette dei nomi
  }

  @Override
  public JPanel createPanel() {
    return new JPanel();
  }

  public void configureSidePanels(JPanel leftPanel, JPanel rightPanel, GameGUIManager gameManager) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.NORTH;

    AbstractPawn[] pawns = gameManager.getGame().getPawns().toArray(new AbstractPawn[0]);
    String[] pawnColors = gameBoardGUI.getPawnColors();

    gbc.gridy = 0;
    gbc.weighty = 0.2;
    leftPanel.add(createPlayerPanel("Player 1 (" + pawnColors[0] + ")", pawns[0].getNumberOfWalls(), 0), gbc);
    rightPanel.add(createPlayerPanel("Player 2 (" + pawnColors[1] + ")", pawns[1].getNumberOfWalls(), 1), gbc);

    if (numberOfPlayers == 4) {
      gbc.gridy = 1;
      gbc.weighty = 0.4;
      leftPanel.add(Box.createVerticalStrut(1), gbc);
      rightPanel.add(Box.createVerticalStrut(1), gbc);

      gbc.gridy = 2;
      gbc.weighty = 0.4;
      gbc.anchor = GridBagConstraints.CENTER;
      leftPanel.add(createPlayerPanel("Player 3 (" + pawnColors[2] + ")", pawns[2].getNumberOfWalls(), 2), gbc);
      rightPanel.add(createPlayerPanel("Player 4 (" + pawnColors[3] + ")", pawns[3].getNumberOfWalls(), 3), gbc);
    }
  }

  private JPanel createPlayerPanel(String playerName, int wallCount, int playerIndex) {
    JPanel playerPanel = new JPanel();
    playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
    playerPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
    nameLabel.setForeground(GUIConstants.TEXT_COLOR);
    nameLabel.setFont(GUIConstants.HEADER_FONT);
    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    nameLabels[playerIndex] = nameLabel;

    JLabel wallsLabel = new JLabel("Remaining walls: " + wallCount, SwingConstants.CENTER);
    wallsLabel.setForeground(GUIConstants.TEXT_COLOR);
    wallsLabel.setFont(GUIConstants.NORMAL_FONT);
    wallsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    wallLabels[playerIndex] = wallsLabel;
    playerPanels[playerIndex] = playerPanel;

    playerPanel.add(Box.createVerticalStrut(20));
    playerPanel.add(nameLabel);
    playerPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));
    playerPanel.add(wallsLabel);
    playerPanel.add(Box.createVerticalStrut(20));

    return playerPanel;
  }

  public void updateWallLabel(int playerIndex, int remainingWalls) {
    wallLabels[playerIndex].setText("Remaining walls: " + remainingWalls);
  }

  public void removeActionPanel(int playerIndex, JPanel actionPanel) {
    playerPanels[playerIndex].remove(actionPanel);
  }

  public void addActionPanel(int playerIndex, JPanel actionPanel) {
    playerPanels[playerIndex].add(actionPanel);
    playerPanels[playerIndex].revalidate();
    playerPanels[playerIndex].repaint();
  }

  public void updatePlayerPanel(int activePlayerIndex) {
    for (int i = 0; i < numberOfPlayers; i++) {
      if (nameLabels[i] != null) {
        nameLabels[i].setForeground(GUIConstants.TEXT_COLOR);
        wallLabels[i].setForeground(GUIConstants.TEXT_COLOR);
      }
    }

    if (activePlayerIndex >= 0 && activePlayerIndex < numberOfPlayers && nameLabels[activePlayerIndex] != null) {
      nameLabels[activePlayerIndex].setForeground(Color.GREEN);
      wallLabels[activePlayerIndex].setForeground(Color.GREEN);
    }

    for (JPanel panel : playerPanels) {
      if (panel != null) {
        panel.revalidate();
        panel.repaint();
      }
    }
  }
}