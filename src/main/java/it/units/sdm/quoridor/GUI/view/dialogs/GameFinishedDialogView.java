package it.units.sdm.quoridor.GUI.view.dialogs;

import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.cli.engine.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.utils.GUIConstants;

import javax.swing.*;
import java.awt.*;

public class GameFinishedDialogView implements DialogView {
  private final GUIQuoridorGameEngine gameEngine;
  private final JFrame mainFrame;
  private final JDialog gameFinishedDialog;
  private final StatisticsCounter statistics;

  public GameFinishedDialogView(GUIQuoridorGameEngine gameEngine, JFrame mainFrame, StatisticsCounter statistics) {
    this.gameEngine = gameEngine;
    this.mainFrame = mainFrame;
    this.statistics = statistics;
    this.gameFinishedDialog = new JDialog(mainFrame, true);
  }


  @Override
  public void displayDialog() {
    gameFinishedDialog.setUndecorated(true);
    gameFinishedDialog.setSize(600,  gameEngine.getNumberOfPlayers() == 4 ? 500 : 400);
    gameFinishedDialog.setLocationRelativeTo(mainFrame);
    gameFinishedDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    JPanel panel = createDialog();

    gameFinishedDialog.add(panel);
    gameFinishedDialog.setVisible(true);
  }

  private JPanel createDialog() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(GUIConstants.POPUP_BORDER);
    panel.setBackground(GUIConstants.SCREEN_BACKGROUND);

    JLabel messageLabel = getMessageLabel();
    panel.add(messageLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setBackground(GUIConstants.SCREEN_BACKGROUND);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

    JButton restartButton = new JButton("RESTART");
    restartButton.setFont(GUIConstants.BUTTON_FONT);
    restartButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    restartButton.addActionListener(e -> {
      gameFinishedDialog.dispose();
      if (mainFrame != null) {
        mainFrame.dispose();
      }
      gameEngine.restartGame();
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
    int numberOfPlayers = gameEngine.getNumberOfPlayers();
    boolean isGameFinished = gameEngine.isGameFinished();
    JLabel messageLabel = getMessageLabel(numberOfPlayers, isGameFinished);

    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.NORMAL_FONT);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
    return messageLabel;
  }

  private JLabel getMessageLabel(int numberOfPlayers, boolean isGameFinished) {
    int playingPawnIndex = gameEngine.getPlayingPawnIndex();

    JLabel messageLabel;
    if (numberOfPlayers == 2) {
      messageLabel = new JLabel("<html>Player " + (isGameFinished ? (playingPawnIndex + 1) : (2 - playingPawnIndex)) + " WINS!<br><br><br>" + getStatistics() + "</html>", SwingConstants.CENTER);
    } else {
      messageLabel = new JLabel("<html>Player " + (isGameFinished ? (playingPawnIndex + 1) + " WINS!" : (playingPawnIndex + 1) + " QUIT!") + "<br><br><br><br>" + getStatistics() + "</html>", SwingConstants.CENTER);
    }
    messageLabel.setForeground(GUIConstants.TEXT_COLOR);
    return messageLabel;
  }

  private String getStatistics() {
    StringBuilder statisticsString = new StringBuilder();
    statisticsString.append("============= GAME STATISTICS ============= <br><br>");

    statisticsString.append("<table style='width:100%; text-align:right;'>");

    statisticsString.append("<tr>")
            .append("<th style='padding:6px;'>PLAYER</th>")
            .append("<th style='padding:6px;'>WINS</th>")
            .append("<th style='padding:6px;'>WIN RATE</th>")
            .append("<th style='padding:6px;'>TOTAL MOVES</th>")
            .append("<th style='padding:6px;'>TOTAL WALLS</th>")
            .append("</tr>");

    for (AbstractPawn pawn : gameEngine.getPawns()) {
      String playerColor = pawn.getPawnAppearance().color().toString();
      String playerIdentifier = pawn.getPawnAppearance().toString();

      statisticsString.append("<tr>")
              .append("<th style='padding:6px;'>").append(playerColor).append("</td>")
              .append("<th style='padding:6px;'>").append(statistics.getTotalWins(playerIdentifier)).append("</td>")
              .append("<th style='padding:6px;'>").append(statistics.getWinRate(playerIdentifier)).append("</td>")
              .append("<th style='padding:6px;'>").append(statistics.getTotalMoves(playerIdentifier)).append("</td>")
              .append("<th style='padding:6px;'>").append(statistics.getTotalWalls(playerIdentifier)).append("</td>")
              .append("</tr>");
    }

    statisticsString.append("</table>");
    return statisticsString.toString();
  }
}