package it.units.sdm.quoridor.GUI.view.panels;

import it.units.sdm.quoridor.utils.GUIConstants;
import it.units.sdm.quoridor.GUI.controller.GameController;
import it.units.sdm.quoridor.GUI.view.managers.BorderManager;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GameBoardPanelComponent implements PanelComponent {

  private final String[] PAWN_COLORS;
  private final JButton[][] tiles;
  private final GameController gameController;
  private final Map<JButton, BorderManager> borderManagers = new HashMap<>();
  private final ImageIcon[] pawnIcons;
  private JPanel gameBoardPanel;


  public GameBoardPanelComponent(GameController gameController) {
    this.gameController = gameController;
    this.PAWN_COLORS = gameController.getPawns().stream()
            .map(pawn -> pawn.getPawnAppearance().color().toString())
            .toArray(String[]::new);
    this.pawnIcons = loadPawnIcons(gameController.getPawns().size());
    this.tiles = new JButton[gameController.getSideLength()][gameController.getSideLength()];
  }

  @Override
  public JPanel createPanel() {
    gameBoardPanel = new JPanel();
    gameBoardPanel.setLayout(new GridLayout(gameController.getSideLength(), gameController.getSideLength(), 0, 0));
    initializeTiles();

    return gameBoardPanel;
  }

  private ImageIcon[] loadPawnIcons(int numPawns) {
    ImageIcon[] icons = new ImageIcon[numPawns];

    for (int i = 0; i < numPawns; i++) {
      String resourcePath = "/" + PAWN_COLORS[i] + "-pawn.png";
      ImageIcon icon = new ImageIcon(Objects.requireNonNull(
              GameBoardPanelComponent.class.getResource(resourcePath)));

      Image img = icon.getImage();
      Image scaledImg = img.getScaledInstance(GUIConstants.ICON_SIZE, GUIConstants.ICON_SIZE, Image.SCALE_SMOOTH);
      icons[i] = new ImageIcon(scaledImg);
    }

    return icons;
  }

  private void initializeTiles() {
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles.length; j++) {
        final int row = i;
        final int col = j;

        JButton tile = new JButton();
        tile.setMargin(new Insets(0, 0, 0, 0));
        tile.setBorder(new LineBorder(Color.GRAY, 1));

        ActionListener tileClickListener = e -> gameController.handleTileClick(new Position(row, col));
        tile.addActionListener(tileClickListener);

        tiles[i][j] = tile;
        gameBoardPanel.add(tile);
      }
    }

    List<AbstractPawn> pawns = gameController.getPawns();

    for (int i = 0; i < pawns.size(); i++) {
      int row = pawns.get(i).getCurrentTile().getRow();
      int column = pawns.get(i).getCurrentTile().getColumn();

      tiles[row][column].setIcon(pawnIcons[i]);
    }
  }


  public void highlightValidMoves() {
    clearHighlights();
    List<Position> validPositions = gameController.getValidMovePositions();
    for (Position position : validPositions) {
      tiles[position.row()][position.column()].setBackground(GUIConstants.HIGHLIGHT_COLOR);
    }
  }

  public void clearHighlights() {
    for (JButton[] row : tiles) {
      for (JButton tile : row) {
        tile.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      }
    }
  }

  public void updateWallVisualization(Position position, WallOrientation orientation) {
    if (orientation.equals(WallOrientation.HORIZONTAL)) {
      updateButtonBorder(tiles[position.row()][position.column()], BorderManager.BOTTOM);
      updateButtonBorder(tiles[position.row()][position.column() + 1], BorderManager.BOTTOM);
      updateButtonBorder(tiles[position.row() + 1][position.column()], BorderManager.TOP);
      updateButtonBorder(tiles[position.row() + 1][position.column() + 1], BorderManager.TOP);
    } else {
      updateButtonBorder(tiles[position.row()][position.column()], BorderManager.LEFT);
      updateButtonBorder(tiles[position.row() - 1][position.column()], BorderManager.LEFT);
      updateButtonBorder(tiles[position.row()][position.column() - 1], BorderManager.RIGHT);
      updateButtonBorder(tiles[position.row() - 1][position.column() - 1], BorderManager.RIGHT);
    }
  }

  public void updatePawnPosition(Position oldPosition, Position newPosition, int pawnIndex) {
    tiles[oldPosition.row()][oldPosition.column()].setIcon(null);
    tiles[newPosition.row()][newPosition.column()].setIcon(pawnIcons[pawnIndex]);
  }


  private void updateButtonBorder(JButton button, int side) {
    BorderManager manager = borderManagers.computeIfAbsent(button, k -> new BorderManager());
    manager.setBorderSide(side, GUIConstants.WALL_COLOR, 3);
    manager.applyTo(button);
  }

}