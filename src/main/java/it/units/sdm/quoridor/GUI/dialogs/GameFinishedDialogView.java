package it.units.sdm.quoridor.GUI.dialogs;

import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameController;

import javax.swing.*;
import java.awt.*;

public class GameFinishedDialogView implements DialogView {
  private final GameController gameController;
  private final JFrame mainFrame;
  private final JDialog gameFinishedDialog;

  public GameFinishedDialogView(GameController gameController, JFrame mainFrame) {
    this.gameController = gameController;
    this.mainFrame = mainFrame;
    this.gameFinishedDialog = new JDialog(mainFrame, true);
  }


  @Override
  public void displayDialog() {
    gameFinishedDialog.setUndecorated(true);
    gameFinishedDialog.setSize(400, 200);
    gameFinishedDialog.setLocationRelativeTo(mainFrame);
    gameFinishedDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    JPanel panel = createDialog();

    gameFinishedDialog.add(panel);
    gameFinishedDialog.setVisible(true);
  }

  private JPanel createDialog() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(GUIConstants.POPUP_BORDER);
    panel.setBackground(gameController.isGameFinished() ? GUIConstants.WIN_SCREEN_BACKGROUND : GUIConstants.QUIT_SCREEN_BACKGROUND);

    JLabel messageLabel = getMessageLabel();
    panel.add(messageLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setBackground(gameController.isGameFinished() ? GUIConstants.WIN_SCREEN_BACKGROUND : GUIConstants.QUIT_SCREEN_BACKGROUND);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

    JButton restartButton = new JButton("RESTART");
    restartButton.setFont(GUIConstants.BUTTON_FONT);
    restartButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    restartButton.addActionListener(e -> {
      gameFinishedDialog.dispose();
      if (mainFrame != null) {
        mainFrame.dispose();
      }
      gameController.restartGame();
    });


    JButton exitButton = new JButton("EXIT");

    exitButton.setFont(GUIConstants.BUTTON_FONT);
    exitButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    exitButton.addActionListener(e -> System.exit(0));

    buttonPanel.add(restartButton);
    buttonPanel.add(exitButton);
    panel.add(buttonPanel, BorderLayout.CENTER);
    return panel;
  }

  private JLabel getMessageLabel() {
    int numberOfPlayers = gameController.getNumberOfPlayers();
    boolean isGameFinished = gameController.isGameFinished();
    JLabel messageLabel = getMessageLabel(numberOfPlayers, isGameFinished);

    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.NORMAL_FONT);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
    return messageLabel;
  }

  private JLabel getMessageLabel(int numberOfPlayers, boolean isGameFinished) {
    int playingPawnIndex = gameController.getPlayingPawnIndex();

    JLabel messageLabel;
    if (numberOfPlayers == 2) {
      messageLabel = new JLabel("<html>Player " + (isGameFinished ? (playingPawnIndex + 1) : (2 - playingPawnIndex)) + " WINS!<br><br></html>", SwingConstants.CENTER);
    } else {
      messageLabel = new JLabel("<html>Player " + (isGameFinished ? (playingPawnIndex + 1) + " WINS!" : (playingPawnIndex + 1) + " QUIT!") + "<br><br></html>", SwingConstants.CENTER);
    }
    messageLabel.setForeground(isGameFinished ? Color.BLACK : GUIConstants.TEXT_COLOR);
    return messageLabel;
  }
}