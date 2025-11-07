package it.units.sdm.quoridor.view.gui.panels;

import it.units.sdm.quoridor.utils.GUIConstants;

import javax.swing.*;
import java.awt.Component;
import it.units.sdm.quoridor.utils.Color;

public class PlayerPanelComponent implements PanelComponent {
  private final String playerName;
  private final int wallCount;
  private final Color color;
  private final int playerIndex;

  private JPanel playerPanel;
  private JLabel nameLabel;
  private JLabel wallsLabel;

  public PlayerPanelComponent(String playerName, int wallCount, Color color, int playerIndex) {
    this.playerName = playerName;
    this.wallCount = wallCount;
    this.color = color;
    this.playerIndex = playerIndex;
  }

  @Override
  public JPanel createPanel() {
    playerPanel = new JPanel();
    playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
    playerPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    nameLabel = new JLabel(playerName + " (" + color.getName() + ")", SwingConstants.CENTER);
    nameLabel.setForeground(GUIConstants.TEXT_COLOR);
    nameLabel.setFont(GUIConstants.HEADER_FONT);
    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    wallsLabel = new JLabel("Remaining walls: " + wallCount, SwingConstants.CENTER);
    wallsLabel.setForeground(GUIConstants.TEXT_COLOR);
    wallsLabel.setFont(GUIConstants.NORMAL_FONT);
    wallsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    playerPanel.add(Box.createVerticalStrut(20));
    playerPanel.add(nameLabel);
    playerPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));
    playerPanel.add(wallsLabel);
    playerPanel.add(Box.createVerticalStrut(20));

    return playerPanel;
  }

  public void updateWallLabel(int remainingWalls) {
    wallsLabel.setText("Remaining walls: " + remainingWalls);
  }

  public void setActive(boolean active) {
    java.awt.Color textColor = active ? color.getColor() : GUIConstants.TEXT_COLOR;
    nameLabel.setForeground(textColor);
    wallsLabel.setForeground(textColor);
    playerPanel.revalidate();
    playerPanel.repaint();
  }

  public void addActionPanel(JPanel actionPanel) {
    playerPanel.add(actionPanel);
    playerPanel.revalidate();
    playerPanel.repaint();
  }

  public void removeActionPanel(JPanel actionPanel) {
    playerPanel.remove(actionPanel);
    playerPanel.revalidate();
    playerPanel.repaint();
  }

  public int getPlayerIndex() {
    return playerIndex;
  }
}