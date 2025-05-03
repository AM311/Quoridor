package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.utils.Position;

import javax.swing.*;
import java.awt.*;

public class PanelManager {
  private final GameController controller;
  private final int numberOfPlayers;
  private final GameBoardGUI gameBoardGUI;
  private final DialogManager dialogManager;

  private final JPanel[] playerPanels;
  private final JLabel[] wallLabels;
  private JPanel currentActionPanel;

  public PanelManager(GameController controller, int numberOfPlayers, DialogManager dialogManager) {
    this.controller = controller;
    this.numberOfPlayers = numberOfPlayers;
    this.dialogManager = dialogManager;
    this.playerPanels = new JPanel[numberOfPlayers];
    this.wallLabels = new JLabel[numberOfPlayers];

    Position[] pawnPositions = createInitialPawnPositions();
    this.gameBoardGUI = new GameBoardGUI(controller, pawnPositions);
  }

  public JPanel createRootPanel() {
    JPanel rootPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridy = 0;
    gbc.weighty = 1;

    JPanel leftPanel = new JPanel(new GridBagLayout());
    leftPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    gbc.gridx = 0;
    gbc.weightx = 3.5;
    rootPanel.add(leftPanel, gbc);

    JPanel centerWrapper = new JPanel(new GameBoardPanelLayoutManager());
    centerWrapper.setBackground(GUIConstants.BACKGROUND_COLOR);
    gbc.gridx = 1;
    gbc.weightx = 9;
    rootPanel.add(centerWrapper, gbc);
    centerWrapper.add(gameBoardGUI);

    JPanel rightPanel = new JPanel(new GridBagLayout());
    rightPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    gbc.gridx = 2;
    gbc.weightx = 3.5;
    rootPanel.add(rightPanel, gbc);

    configureSidePanels(leftPanel, rightPanel);

    showActionButtonsForPlayer(controller.getPlayingPawnIndex());

    return rootPanel;
  }


  public GameBoardGUI getGameBoardPanel() {
    return gameBoardGUI;
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

    AbstractPawn[] pawns = controller.getGame().getPawns().toArray(new AbstractPawn[0]);
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


  public void showActionButtonsForPlayer(int playerIndex) {
    if (currentActionPanel != null) {
      playerPanels[playerIndex].remove(currentActionPanel);
    }

    currentActionPanel = new JPanel();
    currentActionPanel.setLayout(new BoxLayout(currentActionPanel, BoxLayout.Y_AXIS));
    currentActionPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    currentActionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel actionButtonsPanel = createActionButtonsPanel(playerIndex);
    addActionButtonsPanel(playerIndex, actionButtonsPanel);
  }


  private void addActionButtonsPanel(int playerIndex, JPanel actionButtonsPanel) {
    currentActionPanel.add(actionButtonsPanel);
    currentActionPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));

    JPanel helpQuitPanel = createHelpQuitPanel();
    currentActionPanel.add(helpQuitPanel);

    playerPanels[playerIndex].add(currentActionPanel);
    playerPanels[playerIndex].revalidate();
    playerPanels[playerIndex].repaint();
  }


  private JPanel createActionButtonsPanel(int playerIndex) {
    JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    actionButtonsPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton moveButton = new JButton("Move");
    moveButton.addActionListener(e -> {
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.MOVE);
      moveButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      try {
        gameBoardGUI.highlightValidMoves();
      } catch (Exception ex) {
        dialogManager.showErrorDialog("Error highlighting moves: " + ex.getMessage());
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
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.DO_NOTHING);
      moveButton.setBackground(UIManager.getColor("Button.background"));
      gameBoardGUI.clearHighlights();
      if (controller.getGame().getPlayingPawn().getNumberOfWalls() > 0) {
        showWallDirectionButtons(playerIndex);
      } else {
        dialogManager.showNotificationDialog("No walls available!", playerIndex);
      }
    });
    return placeWallButton;
  }


  private void showWallDirectionButtons(int playerIndex) {
    playerPanels[playerIndex].remove(currentActionPanel);

    currentActionPanel = new JPanel();
    currentActionPanel.setLayout(new BoxLayout(currentActionPanel, BoxLayout.Y_AXIS));
    currentActionPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    currentActionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel directionButtonsPanel = createWallDirectionButtonsPanel(playerIndex);
    addActionButtonsPanel(playerIndex, directionButtonsPanel);
  }


  private JPanel createWallDirectionButtonsPanel(int playerIndex) {
    JPanel directionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    directionButtonsPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton verticalButton = new JButton("Vertical");
    verticalButton.addActionListener(e -> {
      verticalButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.PLACE_VERTICAL_WALL);
    });

    JButton horizontalButton = new JButton("Horizontal");
    horizontalButton.addActionListener(e -> {
      verticalButton.setBackground(UIManager.getColor("Button.background"));
      horizontalButton.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.PLACE_HORIZONTAL_WALL);
    });

    JButton cancelButton = new JButton("X");
    cancelButton.addActionListener(e -> {
      verticalButton.setBackground(UIManager.getColor("Button.background"));
      horizontalButton.setBackground(UIManager.getColor("Button.background"));
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.DO_NOTHING);
      showActionButtonsForPlayer(playerIndex);
    });

    directionButtonsPanel.add(verticalButton);
    directionButtonsPanel.add(horizontalButton);
    directionButtonsPanel.add(cancelButton);
    return directionButtonsPanel;
  }


  private JPanel createHelpQuitPanel() {
    JPanel helpQuitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    helpQuitPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton helpButton = new JButton("Help");
    helpButton.addActionListener(e -> dialogManager.showHelpDialog());

    JButton quitButton = new JButton("Quit");
    quitButton.addActionListener(e -> dialogManager.confirmQuitDialog());

    helpQuitPanel.add(helpButton);
    helpQuitPanel.add(quitButton);
    return helpQuitPanel;
  }


  public void updateWallLabel(int playerIndex, int remainingWalls) {
    wallLabels[playerIndex].setText("Remaining walls: " + remainingWalls);
  }


  public void removeCurrentActionPanel(int playerIndex) {
    playerPanels[playerIndex].remove(currentActionPanel);
  }
}