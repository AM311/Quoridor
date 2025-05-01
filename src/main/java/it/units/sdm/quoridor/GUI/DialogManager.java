package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DialogManager {
  private final JFrame mainFrame;
  private final int numberOfPlayers;
  private final GameController controller;

  public DialogManager(JFrame mainFrame, int numberOfPlayers, GameController controller) {
    this.mainFrame = mainFrame;
    this.numberOfPlayers = numberOfPlayers;
    this.controller = controller;
  }

  public void showHelpDialog() {
    JPanel helpPanel = new JPanel();
    helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));

    JLabel instructionsLabel = getInstructionsLabel();
    helpPanel.add(instructionsLabel);

    helpPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));
    helpPanel.add(new JSeparator());
    helpPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));

    JLabel wallExplanationTitle = new JLabel("Wall Placement Convention:");
    wallExplanationTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    wallExplanationTitle.setFont(GUIConstants.SMALL_FONT);
    helpPanel.add(wallExplanationTitle);
    helpPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));

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

    bottomLeft.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);

    Dimension buttonSize = new Dimension(60, 60);
    topLeft.setPreferredSize(buttonSize);
    topRight.setPreferredSize(buttonSize);
    bottomLeft.setPreferredSize(buttonSize);
    bottomRight.setPreferredSize(buttonSize);

    Map<JButton, BorderManager> borderManagers = new HashMap<>();

    BorderManager blBorder = borderManagers.computeIfAbsent(bottomLeft, k -> new BorderManager());
    blBorder.setBorderSide(BorderManager.LEFT, GUIConstants.WALL_COLOR, 5);
    blBorder.applyTo(bottomLeft);

    BorderManager tlBorder = borderManagers.computeIfAbsent(topLeft, k -> new BorderManager());
    tlBorder.setBorderSide(BorderManager.LEFT, GUIConstants.WALL_COLOR, 5);
    tlBorder.applyTo(topLeft);

    BorderManager brBorder = borderManagers.computeIfAbsent(bottomRight, k -> new BorderManager());
    brBorder.setBorderSide(BorderManager.BOTTOM, GUIConstants.WALL_COLOR, 5);
    brBorder.applyTo(bottomRight);

    BorderManager blBottomBorder = borderManagers.computeIfAbsent(bottomLeft, k -> new BorderManager());
    blBottomBorder.setBorderSide(BorderManager.BOTTOM, GUIConstants.WALL_COLOR, 5);
    blBottomBorder.applyTo(bottomLeft);

    visualPanel.add(topLeft);
    visualPanel.add(topRight);
    visualPanel.add(bottomLeft);
    visualPanel.add(bottomRight);
    return visualPanel;
  }

  private JLabel getWallConventionLabel() {
    JLabel explanationLabel = new JLabel(GUIConstants.WALL_CONVENTION);
    explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    return explanationLabel;
  }

  private JLabel getInstructionsLabel() {
    JLabel instructionsLabel = new JLabel(GUIConstants.INSTRUCTION);
    instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    return instructionsLabel;
  }

  public void confirmQuitDialog() {
    JDialog popup = new JDialog(mainFrame, true);
    popup.setUndecorated(true);
    popup.setSize(400, 200);
    popup.setLocationRelativeTo(mainFrame);
    popup.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(GUIConstants.POPUP_BORDER);
    panel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JLabel messageLabel = new JLabel("<html>Are you sure you want to quit?<br><br></html>",
            SwingConstants.CENTER);
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.NORMAL_FONT);
    messageLabel.setForeground(GUIConstants.TEXT_COLOR);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
    panel.add(messageLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

    JButton yesButton = new JButton("YES");
    yesButton.setFont(GUIConstants.BUTTON_FONT);
    yesButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    yesButton.addActionListener(e -> {
      popup.dispose();
      showGameFinishedDialog(1 - controller.getPlayingPawnIndex());
    });

    JButton noButton = new JButton("NO");
    noButton.setFont(GUIConstants.BUTTON_FONT);
    noButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    noButton.addActionListener(e -> popup.dispose());

    buttonPanel.add(yesButton);
    buttonPanel.add(noButton);
    panel.add(buttonPanel, BorderLayout.CENTER);

    popup.add(panel);
    popup.setVisible(true);
  }

  public void showGameFinishedDialog(int winnerIndex) {
    JDialog popup = new JDialog(mainFrame, true);
    popup.setUndecorated(true);
    popup.setSize(400, 200);
    popup.setLocationRelativeTo(mainFrame);
    popup.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(GUIConstants.POPUP_BORDER);
    panel.setBackground(GUIConstants.WIN_SCREEN_BACKGROUND);

    JLabel messageLabel = new JLabel("<html>" +
            (numberOfPlayers == 2 || controller.isGameFinished() ? "Player " + (winnerIndex + 1) + " WINS<br><br>" : "") +
            "Restart the game?<br><br></html>",
            SwingConstants.CENTER);
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.NORMAL_FONT);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
    panel.add(messageLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setBackground(GUIConstants.WIN_SCREEN_BACKGROUND);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

    JButton restartButton = new JButton("RESTART");
    restartButton.setFont(GUIConstants.BUTTON_FONT);
    restartButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    restartButton.addActionListener(e -> {
      popup.dispose();
      restartGame();
    });

    JButton exitButton = new JButton("EXIT");
    exitButton.setFont(GUIConstants.BUTTON_FONT);
    exitButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    exitButton.addActionListener(e -> System.exit(0));

    buttonPanel.add(restartButton);
    buttonPanel.add(exitButton);
    panel.add(buttonPanel, BorderLayout.CENTER);

    popup.add(panel);
    popup.setVisible(true);
  }

  public void showNotificationDialog(String message, int playerIndex) {
    JDialog popup = new JDialog(mainFrame);
    popup.setUndecorated(true);
    popup.setSize(310, 150);

    if (playerIndex == 0 || playerIndex == 2) {
      popup.setLocation(30, 350);
    } else {
      popup.setLocation(1180, 350);
    }

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(GUIConstants.POPUP_BORDER);
    panel.setBackground(GUIConstants.POPUP_BACKGROUND);

    JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.SMALL_FONT);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.add(messageLabel, BorderLayout.CENTER);

    popup.add(panel);
    popup.setVisible(true);

    Timer timer = new Timer(GUIConstants.POPUP_DURATION, e -> popup.dispose());
    timer.setRepeats(false);
    timer.start();
  }

  public void showErrorDialog(String message) {
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
      var game = builderDirector.makeGame();
      GameController newController = new GameController(game);
      GameGUI gameGUI = new GameGUI(numberOfPlayers, newController);
      gameGUI.showGUI();
    } catch (InvalidParameterException | BuilderException e) {
      showErrorDialog("Error restarting game: " + e.getMessage());
    }
  }
}