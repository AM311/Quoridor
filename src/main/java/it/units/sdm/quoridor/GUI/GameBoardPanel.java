package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;


public class GameBoardPanel extends JPanel {

  public enum Action {
    MOVE, PLACE_VERTICAL_WALL, PLACE_HORIZONTAL_WALL, DO_NOTHING
  }

  private static final int ICON_SIZE = 50;
  private static final Color WALL_COLOR = Color.BLACK;
  private static final String[] PAWN_COLORS = {"red", "blue", "green", "magenta"};

  private final JButton[][] tiles;
  private final AbstractGame game;
  private final Map<JButton, BorderManager> borderManagers = new HashMap<>();
  private final ImageIcon[] pawnIcons;

  protected Action currentAction = Action.DO_NOTHING;


  public GameBoardPanel(AbstractGame game, Position... pawnPositions) {
    this.game = game;
    this.pawnIcons = loadPawnIcons(pawnPositions.length);
    int gameBoardSize = game.getGameBoard().getSideLength();
    this.tiles = new JButton[gameBoardSize][gameBoardSize];

    setLayout(new GridLayout(gameBoardSize, gameBoardSize, 0, 0));
    initializeTiles(pawnPositions);
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

  private void handleTileClick(Position targetPosition) {
    switch (currentAction) {
      case MOVE -> attemptPawnMove(targetPosition);
      case PLACE_HORIZONTAL_WALL -> attemptPlaceWall(targetPosition, HORIZONTAL);
      case PLACE_VERTICAL_WALL -> attemptPlaceWall(targetPosition, VERTICAL);
      case DO_NOTHING -> {}
    }
  }

  private void attemptPawnMove(Position targetPosition) {
    try {
      Position oldPosition = new Position(
              game.getPlayingPawn().getCurrentTile().getRow(),
              game.getPlayingPawn().getCurrentTile().getColumn()
      );
      int playerIndex = game.getPlayingPawnIndex();

      game.movePlayingPawn(targetPosition);

      updatePawnPosition(oldPosition, targetPosition, playerIndex);
      setCurrentAction(Action.DO_NOTHING);

      if (game.isGameFinished()) {
        JOptionPane.showMessageDialog(GameBoardPanel.this, "Game finished!");
        System.exit(0);
      }
      game.changeRound();
    } catch (InvalidParameterException | InvalidActionException e) {
      JOptionPane.showMessageDialog(this,
              "Invalid move to " + targetPosition.row() + "," + targetPosition.column(),
              "Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  private void attemptPlaceWall(Position position, WallOrientation orientation) {
    try {
      game.placeWall(position, orientation);

      updateWallVisualization(position, orientation);
      setCurrentAction(Action.DO_NOTHING);

      game.changeRound();
    } catch (InvalidActionException | InvalidParameterException e) {
      JOptionPane.showMessageDialog(this,
              "Invalid wall placement at " + position.row() + "," + position.column(),
              "Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  public void updateWallVisualization(Position position, WallOrientation orientation) {
    if (orientation.equals(HORIZONTAL)) {
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