package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.GUI.managers.BorderManager;
import it.units.sdm.quoridor.GUI.managers.DialogManager;
import it.units.sdm.quoridor.GUI.managers.GameGUIManager;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GameBoardGUI extends JPanel {

  public enum Action {
    MOVE, PLACE_VERTICAL_WALL, PLACE_HORIZONTAL_WALL, DO_NOTHING
  }

  private final DialogManager dialogManager;
  private final String[] PAWN_COLORS;
  private final JButton[][] tiles;
  private final GameGUIManager gameManager;
  private final Map<JButton, BorderManager> borderManagers = new HashMap<>();
  private final ImageIcon[] pawnIcons;

  protected Action currentAction = Action.DO_NOTHING;

  public GameBoardGUI(GameGUIManager gameManager, DialogManager dialogManager, Position... pawnPositions) {
    this.gameManager = gameManager;
    this.PAWN_COLORS = getPawnColors();
    this.pawnIcons = loadPawnIcons(pawnPositions.length);
    int gameBoardSize = gameManager.getGame().getGameBoard().getSideLength();
    this.tiles = new JButton[gameBoardSize][gameBoardSize];
    this.dialogManager = dialogManager;

    setLayout(new GridLayout(gameBoardSize, gameBoardSize, 0, 0));
    initializeTiles(pawnPositions);
  }

  public String[] getPawnColors() {
    String[] pawnColors = new String[gameManager.getGame().getPawns().size()];
    for (int i = 0; i < pawnColors.length; i++) {
      pawnColors[i] = gameManager.getGame().getPawns().get(i).getPawnAppearance().color().toString();
    }
    return pawnColors;
  }

  private ImageIcon[] loadPawnIcons(int numPawns) {
    ImageIcon[] icons = new ImageIcon[numPawns];

    for (int i = 0; i < numPawns; i++) {
      String resourcePath = "/" + PAWN_COLORS[i] + "-pawn.png";
      ImageIcon icon = new ImageIcon(Objects.requireNonNull(
              GameBoardGUI.class.getResource(resourcePath)));

      Image img = icon.getImage();
      Image scaledImg = img.getScaledInstance(GUIConstants.ICON_SIZE, GUIConstants.ICON_SIZE, Image.SCALE_SMOOTH);
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
    List<Position> validPositions = gameManager.getValidMovePositions();
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

  private void handleTileClick(Position targetPosition) {
    switch (currentAction) {
      case MOVE -> gameManager.attemptPawnMove(targetPosition);
      case PLACE_HORIZONTAL_WALL -> gameManager.attemptPlaceWall(targetPosition, WallOrientation.HORIZONTAL);
      case PLACE_VERTICAL_WALL -> gameManager.attemptPlaceWall(targetPosition, WallOrientation.VERTICAL);
      case DO_NOTHING -> dialogManager.showNotificationDialog("Choose an action!", true);
    }
  }

  private void updateButtonBorder(JButton button, int side) {
    BorderManager manager = borderManagers.computeIfAbsent(button, k -> new BorderManager());
    manager.setBorderSide(side, GUIConstants.WALL_COLOR, 3);
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