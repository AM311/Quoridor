package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GameBoardPanel extends JPanel {

  public enum Action {
    MOVE, PLACE_VERTICAL_WALL, PLACE_HORIZONTAL_WALL, DO_NOTHING
  }

  private static final int ICON_SIZE = GUIConstants.ICON_SIZE;
  private static final Color HIGHLIGHT_COLOR = GUIConstants.HIGHLIGHT_COLOR;
  private static final Color WALL_COLOR = GUIConstants.WALL_COLOR;

  private final String[] PAWN_COLORS;
  private final JButton[][] tiles;
  private final GameController controller;
  private final Map<JButton, BorderManager> borderManagers = new HashMap<>();
  private final ImageIcon[] pawnIcons;

  protected Action currentAction = Action.DO_NOTHING;

  public GameBoardPanel(GameController controller, Position... pawnPositions) {
    this.controller = controller;
    this.PAWN_COLORS = getPawnColors();
    this.pawnIcons = loadPawnIcons(pawnPositions.length);
    int gameBoardSize = controller.getGame().getGameBoard().getSideLength();
    this.tiles = new JButton[gameBoardSize][gameBoardSize];

    setLayout(new GridLayout(gameBoardSize, gameBoardSize, 0, 0));
    initializeTiles(pawnPositions);
  }

  public String[] getPawnColors() {
    String[] pawnColors = new String[controller.getGame().getPawns().size()];
    for (int i = 0; i < pawnColors.length; i++) {
      pawnColors[i] = controller.getGame().getPawns().get(i).getPawnAppearance().color().toString();
    }
    return pawnColors;
  }

  private ImageIcon[] loadPawnIcons(int numPawns) {
    ImageIcon[] icons = new ImageIcon[numPawns];

    for (int i = 0; i < numPawns; i++) {
      String resourcePath = "/" + PAWN_COLORS[i] + "-pawn.png";
      ImageIcon icon = new ImageIcon(Objects.requireNonNull(
              GameBoardPanel.class.getResource(resourcePath)));

      Image img = icon.getImage();
      Image scaledImg = img.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
      icons[i] = new ImageIcon(scaledImg);
    }

    return icons;
  }

  private void initializeTiles(Position... pawnPositions) {
    for (int i = 0; i < tiles.length; i++) {
      for (int j = 0; j < tiles.length; j++) {
        final int row = i;
        final int col = j;

        JButton tile = new JButton();
        tile.setMargin(new Insets(0, 0, 0, 0));
        tile.setBorder(new LineBorder(Color.GRAY, 1));

        ActionListener tileClickListener = e -> handleTileClick(new Position(row, col));
        tile.addActionListener(tileClickListener);

        tiles[i][j] = tile;
        add(tile);
      }
    }

    for (int i = 0; i < pawnPositions.length; i++) {
      Position pos = pawnPositions[i];
      tiles[pos.row()][pos.column()].setIcon(pawnIcons[i]);
    }
  }

  public void setCurrentAction(Action currentAction) {
    this.currentAction = currentAction;
  }

  public void highlightValidMoves() throws InvalidParameterException {
    clearHighlights();
    List<Position> validPositions = controller.getValidMovePositions();
    for (Position position : validPositions) {
      tiles[position.row()][position.column()].setBackground(HIGHLIGHT_COLOR);
    }
  }

  public void clearHighlights() {
    for (JButton[] row : tiles) {
      for (JButton tile : row) {
        tile.setBackground(UIManager.getColor("Button.background"));
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

  private void handleTileClick(Position targetPosition) {
    switch (currentAction) {
      case MOVE -> controller.attemptPawnMove(targetPosition);
      case PLACE_HORIZONTAL_WALL -> controller.attemptPlaceWall(targetPosition, WallOrientation.HORIZONTAL);
      case PLACE_VERTICAL_WALL -> controller.attemptPlaceWall(targetPosition, WallOrientation.VERTICAL);
      case DO_NOTHING -> {}
    }
  }

  private void updateButtonBorder(JButton button, int side) {
    BorderManager manager = borderManagers.computeIfAbsent(button, k -> new BorderManager());
    manager.setBorderSide(side, WALL_COLOR, 3);
    manager.applyTo(button);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(400, 400);
  }

  @Override
  public Dimension getMinimumSize() {
    return new Dimension(100, 100);
  }
}