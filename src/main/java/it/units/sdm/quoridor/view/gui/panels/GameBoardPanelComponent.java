package it.units.sdm.quoridor.view.gui.panels;

import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.utils.GUIConstants;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;
import it.units.sdm.quoridor.view.gui.managers.BorderManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class GameBoardPanelComponent implements PanelComponent {
  private final int sideLength;
  private final List<AbstractPawn> initialPawns;
  private final Consumer<Position> onTileClick;

  private final JButton[][] tiles;
  private final Map<JButton, BorderManager> borderManagers = new HashMap<>();
  private final ImageIcon[] pawnIcons;

  private JPanel gameBoardPanel;

  public GameBoardPanelComponent(int sideLength, List<AbstractPawn> pawns, Consumer<Position> onTileClick) {
    this.sideLength = sideLength;
    this.initialPawns = List.copyOf(pawns);
    this.onTileClick = onTileClick;

    this.pawnIcons = loadPawnIcons(this.initialPawns);
    this.tiles = new JButton[sideLength][sideLength];
  }

  @Override
  public JPanel createPanel() {
    gameBoardPanel = new JPanel();
    gameBoardPanel.setLayout(new GridLayout(sideLength, sideLength, 0, 0));
    initializeTiles();
    placeInitialPawns();
    return gameBoardPanel;
  }

  private ImageIcon[] loadPawnIcons(List<AbstractPawn> pawns) {
    ImageIcon[] icons = new ImageIcon[pawns.size()];

    for (int i = 0; i < pawns.size(); i++) {
      String resourcePath = pawns.get(i).getPawnAppearance().getGuiMarkerPath();
      ImageIcon icon = new ImageIcon(Objects.requireNonNull(
              GameBoardPanelComponent.class.getResource(resourcePath),
              "Missing resource: " + resourcePath
      ));

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

        tile.addActionListener(e -> onTileClick.accept(new Position(row, col)));

        tiles[i][j] = tile;
        gameBoardPanel.add(tile);
      }
    }
  }

  private void placeInitialPawns() {
    for (int i = 0; i < initialPawns.size(); i++) {
      int row = initialPawns.get(i).getCurrentTile().getRow();
      int column = initialPawns.get(i).getCurrentTile().getColumn();
      tiles[row][column].setIcon(pawnIcons[i]);
    }
  }

  public void highlightValidMoves(List<Position> validPositions) {
    clearHighlights();
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
