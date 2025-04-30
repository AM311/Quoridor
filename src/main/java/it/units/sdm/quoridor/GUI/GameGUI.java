package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import javax.swing.*;
import java.awt.*;

public class GameGUI implements GameEventListener {

  private static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
  private static final Color TEXT_COLOR = Color.ORANGE;

  private final int numberOfPlayers;
  private final AbstractGame game;
  private GameBoardPanel gameBoardPanel;
  private JFrame mainFrame;

  private final JPanel[] playerPanels;
  private final JLabel[] wallLabels;
  private JPanel currentActionPanel;

  public GameGUI(int numberOfPlayers, AbstractGame game) {
    this.numberOfPlayers = numberOfPlayers;
    this.game = game;
    this.playerPanels = new JPanel[numberOfPlayers];
    this.wallLabels = new JLabel[numberOfPlayers];
  }

  public void showGUI() {
    mainFrame = new JFrame("Quoridor");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    JPanel rootPanel = createRootPanel();
    mainFrame.setContentPane(rootPanel);
    mainFrame.setVisible(true);
  }


  private JPanel createRootPanel() {
    JPanel rootPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridy = 0;
    gbc.weighty = 1;

    JPanel leftPanel = new JPanel(new GridBagLayout());
    leftPanel.setBackground(BACKGROUND_COLOR);
    gbc.gridx = 0;
    gbc.weightx = 3.5;
    rootPanel.add(leftPanel, gbc);

    JPanel centerWrapper = new JPanel(new GameBoardPanelLayoutManager());
    centerWrapper.setBackground(BACKGROUND_COLOR);
    gbc.gridx = 1;
    gbc.weightx = 9;
    rootPanel.add(centerWrapper, gbc);

    Position[] pawnPositions = createInitialPawnPositions();

    gameBoardPanel = new GameBoardPanel(game, this, pawnPositions);
    centerWrapper.add(gameBoardPanel);

    JPanel rightPanel = new JPanel(new GridBagLayout());
    rightPanel.setBackground(BACKGROUND_COLOR);
    gbc.gridx = 2;
    gbc.weightx = 3.5;
    rootPanel.add(rightPanel, gbc);

    configureSidePanels(leftPanel, rightPanel);

    showActionButtonsForPlayer(game.getPlayingPawnIndex());

    return rootPanel;
  }


  private Position[] createInitialPawnPositions() {
    Position[] positions = new Position[numberOfPlayers];
    positions[0] = new Position(0, 4);
    positions[1] = new Position(8, 4);

    if (numberOfPlayers == 4) {
      positions[2] = new Position(4, 0);
      positions[3] = new Position(4, 8);
    }

    return positions;
  }


  private void configureSidePanels(JPanel leftPanel, JPanel rightPanel) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.NORTH;

    AbstractPawn[] pawns = game.getPawns().toArray(new AbstractPawn[0]);

    gbc.gridy = 0;
    gbc.weighty = 0.2;
    leftPanel.add(createPlayerPanel("Player 1 (RED)", pawns[0].getNumberOfWalls(), 0), gbc);

    rightPanel.add(createPlayerPanel("Player 2 (BLUE)", pawns[1].getNumberOfWalls(), 1), gbc);

    if (numberOfPlayers == 4) {
      gbc.gridy = 1;
      gbc.weighty = 0.4;
      leftPanel.add(Box.createVerticalStrut(1), gbc);
      rightPanel.add(Box.createVerticalStrut(1), gbc);

      gbc.gridy = 2;
      gbc.weighty = 0.4;
      gbc.anchor = GridBagConstraints.CENTER;
      leftPanel.add(createPlayerPanel("Player 3 (GREEN)", pawns[2].getNumberOfWalls(), 2), gbc);

      rightPanel.add(createPlayerPanel("Player 4 (MAGENTA)", pawns[3].getNumberOfWalls(), 3), gbc);
    }
  }

  private JPanel createPlayerPanel(String playerName, int wallCount, int playerIndex) {
    JPanel playerPanel = new JPanel();
    playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
    playerPanel.setBackground(BACKGROUND_COLOR);

    JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
    nameLabel.setForeground(TEXT_COLOR);
    nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel wallsLabel = new JLabel("Remaining walls: " + wallCount, SwingConstants.CENTER);
    wallsLabel.setForeground(TEXT_COLOR);
    wallsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
    wallsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    wallLabels[playerIndex] = wallsLabel;
    playerPanels[playerIndex] = playerPanel;

    playerPanel.add(Box.createVerticalStrut(20));
    playerPanel.add(nameLabel);
    playerPanel.add(Box.createVerticalStrut(10));
    playerPanel.add(wallsLabel);
    playerPanel.add(Box.createVerticalStrut(20));

    return playerPanel;
  }


  private void showActionButtonsForPlayer(int playerIndex) {
    if (currentActionPanel != null) {
      playerPanels[playerIndex].remove(currentActionPanel);
    }

    currentActionPanel = new JPanel();
    currentActionPanel.setLayout(new BoxLayout(currentActionPanel, BoxLayout.Y_AXIS));
    currentActionPanel.setBackground(BACKGROUND_COLOR);
    currentActionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel actionButtonsPanel = createActionButtonsPanel(playerIndex);
    currentActionPanel.add(actionButtonsPanel);
    currentActionPanel.add(Box.createVerticalStrut(10));


    playerPanels[playerIndex].add(currentActionPanel);
    playerPanels[playerIndex].revalidate();
    playerPanels[playerIndex].repaint();
  }


  private JPanel createActionButtonsPanel(int playerIndex) {
    JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    actionButtonsPanel.setBackground(BACKGROUND_COLOR);

    JButton moveButton = new JButton("Move");
    moveButton.addActionListener(e -> {
      gameBoardPanel.setCurrentAction(GameBoardPanel.Action.MOVE);
      moveButton.setBackground(Color.GREEN);
      try {
        gameBoardPanel.highlightValidMoves();
      } catch (InvalidParameterException ex) {
        showErrorDialog("Error highlighting moves: " + ex.getMessage());
      }
    });

    JButton placeWallButton = getPlaceWallButton(playerIndex, moveButton);

    actionButtonsPanel.add(moveButton);
    actionButtonsPanel.add(placeWallButton);
    return actionButtonsPanel;
  }

  private JButton getPlaceWallButton(int playerIndex, JButton moveButton) {
    JButton placeWallButton = new JButton("Place Wall");
    placeWallButton.addActionListener(e -> {
      gameBoardPanel.setCurrentAction(GameBoardPanel.Action.DO_NOTHING);
      moveButton.setBackground(UIManager.getColor("Button.background"));
      gameBoardPanel.clearHighlights();
      if (game.getPlayingPawn().getNumberOfWalls() > 0) {
        showWallDirectionButtons(playerIndex);
      } else {
        JOptionPane.showMessageDialog(mainFrame,
                "No walls available!",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
      }

    });
    return placeWallButton;
  }


  private void showWallDirectionButtons(int playerIndex) {
    playerPanels[playerIndex].remove(currentActionPanel);

    currentActionPanel = new JPanel();
    currentActionPanel.setLayout(new BoxLayout(currentActionPanel, BoxLayout.Y_AXIS));
    currentActionPanel.setBackground(BACKGROUND_COLOR);
    currentActionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel directionButtonsPanel = createWallDirectionButtonsPanel(playerIndex);
    currentActionPanel.add(directionButtonsPanel);
    currentActionPanel.add(Box.createVerticalStrut(10));

    playerPanels[playerIndex].add(currentActionPanel);
    playerPanels[playerIndex].revalidate();
    playerPanels[playerIndex].repaint();
  }


  private JPanel createWallDirectionButtonsPanel(int playerIndex) {
    JPanel directionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    directionButtonsPanel.setBackground(BACKGROUND_COLOR);

    JButton verticalButton = new JButton("Vertical");
    verticalButton.addActionListener(e -> {
      verticalButton.setBackground(Color.GREEN);
      gameBoardPanel.setCurrentAction(GameBoardPanel.Action.PLACE_VERTICAL_WALL);
    });

    JButton horizontalButton = new JButton("Horizontal");
    horizontalButton.addActionListener(e -> {
      verticalButton.setBackground(UIManager.getColor("Button.background"));
      horizontalButton.setBackground(Color.GREEN);
      gameBoardPanel.setCurrentAction(GameBoardPanel.Action.PLACE_HORIZONTAL_WALL);
    });

    JButton cancelButton = new JButton("X");
    cancelButton.addActionListener(e -> {
      verticalButton.setBackground(UIManager.getColor("Button.background"));
      horizontalButton.setBackground(UIManager.getColor("Button.background"));
      gameBoardPanel.setCurrentAction(GameBoardPanel.Action.DO_NOTHING);
      showActionButtonsForPlayer(playerIndex);
    });

    directionButtonsPanel.add(verticalButton);
    directionButtonsPanel.add(horizontalButton);
    directionButtonsPanel.add(cancelButton);
    return directionButtonsPanel;
  }


  private void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(mainFrame,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
  }


  private void showGameFinishedDialog(int winnerIndex) {
    JOptionPane.showMessageDialog(mainFrame,
            "Player " + (winnerIndex + 1) + " wins!",
            "Game Over",
            JOptionPane.INFORMATION_MESSAGE);
    System.exit(0);
  }



  @Override
  public void onWallPlaced(int playerIndex, int remainingWalls) {
    wallLabels[playerIndex].setText("Remaining walls: " + remainingWalls);
  }

  @Override
  public void onInvalidWallPlacement(Position position, WallOrientation orientation) {
    String orientationStr = orientation.toString().toLowerCase();
    String message = "Cannot place " + orientationStr + " wall at " +
            position.row() + "," + position.column();
    JOptionPane.showMessageDialog(mainFrame,
            message,
            "Invalid Wall Placement",
            JOptionPane.WARNING_MESSAGE);
  }

  @Override
  public void onInvalidMove(Position position) {
    String message = "Cannot move to " + position.row() + "," + position.column();
    JOptionPane.showMessageDialog(mainFrame,
            message,
            "Invalid Move",
            JOptionPane.WARNING_MESSAGE);
  }

  @Override
  public void onTurnComplete() {
    playerPanels[game.getPlayingPawnIndex()].remove(currentActionPanel);
    game.changeRound();
    showActionButtonsForPlayer(game.getPlayingPawnIndex());
  }

  @Override
  public void onGameFinished(int winnerIndex) {
    showGameFinishedDialog(winnerIndex);
  }
}