package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameGUI implements GameEventListener {

  private final int numberOfPlayers;
  private AbstractGame game;
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

    showHelpDialog();
  }

  private JPanel createRootPanel() {
    JPanel rootPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridy = 0;
    gbc.weighty = 1;

    JPanel leftPanel = new JPanel(new GridBagLayout());
    leftPanel.setBackground(Color.DARK_GRAY);
    gbc.gridx = 0;
    gbc.weightx = 3.5;
    rootPanel.add(leftPanel, gbc);

    JPanel centerWrapper = new JPanel(new GameBoardPanelLayoutManager());
    centerWrapper.setBackground(Color.DARK_GRAY);
    gbc.gridx = 1;
    gbc.weightx = 9;
    rootPanel.add(centerWrapper, gbc);

    Position[] pawnPositions = createInitialPawnPositions();

    gameBoardPanel = new GameBoardPanel(game, this, pawnPositions);
    centerWrapper.add(gameBoardPanel);

    JPanel rightPanel = new JPanel(new GridBagLayout());
    rightPanel.setBackground(Color.DARK_GRAY);
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
    String[] pawnColors = gameBoardPanel.getPawnColors();

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
    playerPanel.setBackground(Color.DARK_GRAY);

    JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
    nameLabel.setForeground(Color.ORANGE);
    nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel wallsLabel = new JLabel("Remaining walls: " + wallCount, SwingConstants.CENTER);
    wallsLabel.setForeground(Color.ORANGE);
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
    currentActionPanel.setBackground(Color.DARK_GRAY);
    currentActionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel actionButtonsPanel = createActionButtonsPanel(playerIndex);
    addActionButtonsPanel(playerIndex, actionButtonsPanel);
  }

  private void addActionButtonsPanel(int playerIndex, JPanel actionButtonsPanel) {
    currentActionPanel.add(actionButtonsPanel);
    currentActionPanel.add(Box.createVerticalStrut(10));

    JPanel helpQuitPanel = createHelpQuitPanel();
    currentActionPanel.add(helpQuitPanel);

    playerPanels[playerIndex].add(currentActionPanel);
    playerPanels[playerIndex].revalidate();
    playerPanels[playerIndex].repaint();
  }

  private JPanel createActionButtonsPanel(int playerIndex) {
    JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    actionButtonsPanel.setBackground(Color.DARK_GRAY);

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

    JButton placeWallButton = new JButton("Place Wall");
    placeWallButton.addActionListener(e -> {
      gameBoardPanel.setCurrentAction(GameBoardPanel.Action.DO_NOTHING);
      moveButton.setBackground(UIManager.getColor("Button.background"));
      gameBoardPanel.clearHighlights();
      if (game.getPlayingPawn().getNumberOfWalls() > 0) {
        showWallDirectionButtons(playerIndex);
      } else {
        showNotification("No walls available!", playerIndex);
      }

    });

    actionButtonsPanel.add(moveButton);
    actionButtonsPanel.add(placeWallButton);
    return actionButtonsPanel;
  }

  private void showWallDirectionButtons(int playerIndex) {
    playerPanels[playerIndex].remove(currentActionPanel);

    currentActionPanel = new JPanel();
    currentActionPanel.setLayout(new BoxLayout(currentActionPanel, BoxLayout.Y_AXIS));
    currentActionPanel.setBackground(Color.DARK_GRAY);
    currentActionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel directionButtonsPanel = createWallDirectionButtonsPanel(playerIndex);
    addActionButtonsPanel(playerIndex, directionButtonsPanel);
  }

  private JPanel createWallDirectionButtonsPanel(int playerIndex) {
    JPanel directionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    directionButtonsPanel.setBackground(Color.DARK_GRAY);

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

  private JPanel createHelpQuitPanel() {
    JPanel helpQuitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    helpQuitPanel.setBackground(Color.DARK_GRAY);

    JButton helpButton = new JButton("Help");
    helpButton.addActionListener(e -> showHelpDialog());

    JButton quitButton = new JButton("Quit");
    quitButton.addActionListener(e -> confirmQuit());

    helpQuitPanel.add(helpButton);
    helpQuitPanel.add(quitButton);
    return helpQuitPanel;
  }


  private void showHelpDialog() {
    JPanel helpPanel = new JPanel();
    helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));

    JLabel instructionsLabel = getInstructionsLabel();
    helpPanel.add(instructionsLabel);

    helpPanel.add(Box.createVerticalStrut(10));
    helpPanel.add(new JSeparator());
    helpPanel.add(Box.createVerticalStrut(10));

    JLabel wallExplanationTitle = new JLabel("Wall Placement Convention:");
    wallExplanationTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    wallExplanationTitle.setFont(new Font("Arial", Font.PLAIN, 14));
    helpPanel.add(wallExplanationTitle);
    helpPanel.add(Box.createVerticalStrut(10));

    JPanel visualPanel = getVisualPanel();

    JPanel visualWrapperPanel = new JPanel();
    visualWrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    visualWrapperPanel.add(visualPanel);
    helpPanel.add(visualWrapperPanel);

    JLabel explanationLabel = getWallConventionLabel();
    helpPanel.add(Box.createVerticalStrut(10));
    helpPanel.add(explanationLabel);

    JOptionPane.showMessageDialog(
            mainFrame,
            helpPanel,
            "Quoridor Help",
            JOptionPane.INFORMATION_MESSAGE
    );
  }

  private JPanel getVisualPanel() {
    JPanel visualPanel = new JPanel(new GridLayout(2, 2, 0, 0));
    visualPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    visualPanel.setMaximumSize(new Dimension(200, 200));

    JButton topLeft = new JButton();
    JButton topRight = new JButton();
    JButton bottomLeft = new JButton();
    JButton bottomRight = new JButton();

    bottomLeft.setBackground(Color.GREEN);

    Dimension buttonSize = new Dimension(60, 60);
    topLeft.setPreferredSize(buttonSize);
    topRight.setPreferredSize(buttonSize);
    bottomLeft.setPreferredSize(buttonSize);
    bottomRight.setPreferredSize(buttonSize);

    Map<JButton, BorderManager> borderManagers = new HashMap<>();

    BorderManager blBorder = borderManagers.computeIfAbsent(bottomLeft, k -> new BorderManager());
    blBorder.setBorderSide(BorderManager.LEFT, Color.BLACK, 5);
    blBorder.applyTo(bottomLeft);

    BorderManager tlBorder = borderManagers.computeIfAbsent(topLeft, k -> new BorderManager());
    tlBorder.setBorderSide(BorderManager.LEFT, Color.BLACK, 5);
    tlBorder.applyTo(topLeft);

    BorderManager brBorder = borderManagers.computeIfAbsent(bottomRight, k -> new BorderManager());
    brBorder.setBorderSide(BorderManager.BOTTOM, Color.BLACK, 5);
    brBorder.applyTo(bottomRight);

    BorderManager blBottomBorder = borderManagers.computeIfAbsent(bottomLeft, k -> new BorderManager());
    blBottomBorder.setBorderSide(BorderManager.BOTTOM, Color.BLACK, 5);
    blBottomBorder.applyTo(bottomLeft);

    visualPanel.add(topLeft);
    visualPanel.add(topRight);
    visualPanel.add(bottomLeft);
    visualPanel.add(bottomRight);
    return visualPanel;
  }

  private JLabel getWallConventionLabel() {
    JLabel explanationLabel = new JLabel(
            "<html><div style='width: 300px; text-align: left;'>" +
                    "The chosen tile is in the bottom-left.<br>" +
                    "Walls are represented with black lines and they occupy two tiles:<br>" +
                    "<ul>" +
                    "<li>Vertical walls appear on the left side of tiles<br>" +
                    "<li>Horizontal walls appear on the bottom side of tiles" +
                    "</ul>" +
                    "You cannot place walls in the margins</div></html>"
    );
    explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    return explanationLabel;
  }

  private JLabel getInstructionsLabel() {
    JLabel instructionsLabel = new JLabel(
            "<html><div style='width: 300px; text-align: left;'>" +
                    "<h3>How to play Quoridor:</h3>" +
                    "<ul>" +
                    "<li>On your turn you can move your pawn or place a wall</li>" +
                    "<li>You win by reaching the opposite side of the board</li>" +
                    "<li>Walls can be placed vertically or horizontally</li>" +
                    "<li>You cannot completely block a path to the goal</li>" +
                    "</ul></div></html>"
    );
    instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    return instructionsLabel;
  }

  private void confirmQuit() {
    int choice = JOptionPane.showConfirmDialog(mainFrame,
            "Are you sure you want to quit?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);

    if (choice == JOptionPane.YES_OPTION) {
      showGameFinishedDialog(1 - game.getPlayingPawnIndex());
    }
  }


  protected void showNotification(String message, int playerIndex) {
    JDialog popup = new JDialog(mainFrame);
    popup.setUndecorated(true);
    popup.setSize(310, 150);

    if (playerIndex == 0 || playerIndex == 2) {
      popup.setLocation(30, 350);
    } else {
      popup.setLocation(1180, 350);
    }

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    panel.setBackground(Color.WHITE);

    JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.add(messageLabel, BorderLayout.CENTER);

    popup.add(panel);
    popup.setVisible(true);

    Timer timer = new Timer(3000, e -> popup.dispose());
    timer.setRepeats(false);
    timer.start();
  }

  private void showGameFinishedDialog(int winnerIndex) {
    JDialog popup = new JDialog(mainFrame, true);
    popup.setUndecorated(true);
    popup.setSize(400, 200);
    popup.setLocationRelativeTo(mainFrame);
    popup.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    panel.setBackground(Color.GREEN);

    JLabel messageLabel = new JLabel("<html>" +
            (numberOfPlayers == 2 || game.isGameFinished() ? "Player " + (winnerIndex + 1) + " WINS<br><br>" : "") +
            "Restart the game?<br><br></html>",
            SwingConstants.CENTER);
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
    messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
    panel.add(messageLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setBackground(Color.GREEN);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

    JButton restartButton = new JButton("RESTART");
    restartButton.setFont(new Font("Arial", Font.BOLD, 16));
    restartButton.setPreferredSize(new Dimension(120, 40));
    restartButton.addActionListener(e -> {
      popup.dispose();
      restartGame();
    });

    JButton exitButton = new JButton("EXIT");
    exitButton.setFont(new Font("Arial", Font.BOLD, 16));
    exitButton.setPreferredSize(new Dimension(120, 40));
    exitButton.addActionListener(e -> System.exit(0));

    buttonPanel.add(restartButton);
    buttonPanel.add(exitButton);
    panel.add(buttonPanel, BorderLayout.CENTER);

    popup.add(panel);
    popup.setVisible(true);
  }


  private void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(mainFrame,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
  }

  private void restartGame() {
    if (mainFrame != null) {
      mainFrame.dispose();
    }

    try {
      BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(numberOfPlayers));
      this.game = builderDirector.makeGame();

      showGUI();
    } catch (InvalidParameterException | BuilderException e) {
      showErrorDialog("Error restarting game: " + e.getMessage());
    }
  }


  @Override
  public void onWallPlaced(int playerIndex, int remainingWalls) {
    wallLabels[playerIndex].setText("Remaining walls: " + remainingWalls);
  }

  @Override
  public void onInvalidWallPlacement(Position position, WallOrientation orientation) {
    String orientationStr = orientation.toString().toLowerCase();
    String article = orientation.equals(WallOrientation.HORIZONTAL) ? "an " : "a ";
    String message = "Can't place " + article + orientationStr + " wall at "
            + (position.row() + 1) + "," + (position.column() + 1);
    showNotification(message, game.getPlayingPawnIndex());
  }

  @Override
  public void onInvalidMove(Position position) {
    String message = "Can't move to " + (position.row() + 1) + "," + (position.column() + 1);
    showNotification(message, game.getPlayingPawnIndex());
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